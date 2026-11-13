
package br.ucs.dante.repository;

import br.ucs.dante.model.Consumo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import java.util.ArrayList;
import java.util.List;

public class ConsumoRepository {

    private final DatabaseConnection database;

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("uuuuMMdd")
            .withResolverStyle(ResolverStyle.STRICT);

    public ConsumoRepository(DatabaseConnection database) {
        this.database = database;
    }

    private int converterData(LocalDate data) {
        return Integer.parseInt(data.format(formatoData));
    }

    private LocalDate converterData(int data) {
        return LocalDate.parse(
            String.format("%08d", data),
            formatoData
        );
    }

    private Consumo mapear(ResultSet rs) throws SQLException {
        return new Consumo(
            rs.getInt("codigo_cliente"),
            converterData(rs.getInt("data")),
            rs.getInt("sequencia"),
            rs.getInt("codigo_produto"),
            rs.getInt("quantidade"),
            rs.getDouble("valor"),
            rs.getBoolean("pago")
        );
    }

    public List<Consumo> listar() throws SQLException {
        List<Consumo> consumos = new ArrayList<>();

        String sql = """
            SELECT codigo_cliente, data, sequencia,
                   codigo_produto, quantidade, valor, pago
            FROM consumos
            ORDER BY data DESC, codigo_cliente, sequencia
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                consumos.add(mapear(rs));
            }
        }

        return consumos;
    }

    public Consumo buscar(
            int codigoCliente,
            LocalDate data,
            int sequencia) throws SQLException {

        String sql = """
            SELECT codigo_cliente, data, sequencia,
                   codigo_produto, quantidade, valor, pago
            FROM consumos
            WHERE codigo_cliente = ?
              AND data = ?
              AND sequencia = ?
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, codigoCliente);
            ps.setInt(2, converterData(data));
            ps.setInt(3, sequencia);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private int proximaSequencia(
            Connection conexao,
            int codigoCliente,
            int data) throws SQLException {

        String sql = """
            SELECT COALESCE(MAX(sequencia), 0) + 1
            FROM consumos
            WHERE codigo_cliente = ? AND data = ?
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, codigoCliente);
            ps.setInt(2, data);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public Consumo cadastrar(Consumo consumo) throws SQLException {
        try (Connection conexao = database.conectar()) {
            conexao.setAutoCommit(false);

            try {
                // Serializa cadastros feitos por este repositório.
                try (PreparedStatement ps = conexao.prepareStatement(
                        "SELECT pg_advisory_xact_lock(2026, 3)")) {
                    ps.execute();
                }

                int data = converterData(consumo.getData());

                Integer sequencia = consumo.getSequencia();

                if (sequencia == null) {
                    sequencia = proximaSequencia(
                        conexao,
                        consumo.getCodigoCliente(),
                        data
                    );
                }

                String sql = """
                    INSERT INTO consumos
                        (codigo_cliente, data, sequencia,
                         codigo_produto, quantidade, valor, pago)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement ps =
                        conexao.prepareStatement(sql)) {

                    ps.setInt(1, consumo.getCodigoCliente());
                    ps.setInt(2, data);
                    ps.setInt(3, sequencia);
                    ps.setInt(4, consumo.getCodigoProduto());
                    ps.setInt(5, consumo.getQuantidade());
                    ps.setDouble(6, consumo.getValor());
                    ps.setBoolean(7, consumo.getPago());

                    ps.executeUpdate();
                }

                conexao.commit();
                consumo.setSequencia(sequencia);

                return consumo;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }
        }
    }

    public boolean atualizar(Consumo consumo) throws SQLException {
        String sql = """
            UPDATE consumos
            SET codigo_produto = ?,
                quantidade = ?,
                valor = ?,
                pago = ?
            WHERE codigo_cliente = ?
              AND data = ?
              AND sequencia = ?
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, consumo.getCodigoProduto());
            ps.setInt(2, consumo.getQuantidade());
            ps.setDouble(3, consumo.getValor());
            ps.setBoolean(4, consumo.getPago());

            ps.setInt(5, consumo.getCodigoCliente());
            ps.setInt(6, converterData(consumo.getData()));
            ps.setInt(7, consumo.getSequencia());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(
            int codigoCliente,
            LocalDate data,
            int sequencia) throws SQLException {

        String sql = """
            DELETE FROM consumos
            WHERE codigo_cliente = ?
              AND data = ?
              AND sequencia = ?
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, codigoCliente);
            ps.setInt(2, converterData(data));
            ps.setInt(3, sequencia);

            return ps.executeUpdate() > 0;

        } catch (SQLException erro) {
            if ("23503".equals(erro.getSQLState())) {
                throw new IllegalStateException(
                    "Não é possível excluir um consumo " +
                    "que possui registros de sauna vinculados.",
                    erro
                );
            }

            throw erro;
        }
    }
}
