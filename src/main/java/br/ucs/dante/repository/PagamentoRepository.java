
package br.ucs.dante.repository;

import br.ucs.dante.model.Pagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import java.util.ArrayList;
import java.util.List;

public class PagamentoRepository {

    private final DatabaseConnection database;

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("uuuuMMdd")
            .withResolverStyle(ResolverStyle.STRICT);

    public PagamentoRepository(DatabaseConnection database) {
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

    private Pagamento mapear(ResultSet rs) throws SQLException {
        return new Pagamento(
            rs.getInt("codigo_cliente"),
            converterData(rs.getInt("data")),
            rs.getInt("sequencia"),
            rs.getDouble("valor"),
            rs.getBoolean("pagto_total")
        );
    }

    public List<Pagamento> listar() throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();

        String sql = """
            SELECT codigo_cliente, data, sequencia,
                   valor, pagto_total
            FROM pagamentos
            ORDER BY data DESC, codigo_cliente, sequencia
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pagamentos.add(mapear(rs));
            }
        }

        return pagamentos;
    }

    public Pagamento buscar(
            int codigoCliente,
            LocalDate data,
            int sequencia) throws SQLException {

        String sql = """
            SELECT codigo_cliente, data, sequencia,
                   valor, pagto_total
            FROM pagamentos
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
            FROM pagamentos
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

    public Pagamento cadastrar(Pagamento pagamento)
            throws SQLException {

        try (Connection conexao = database.conectar()) {
            conexao.setAutoCommit(false);

            try {
                // Evita conflito entre cadastros feitos
                // simultaneamente por este repositório.
                try (PreparedStatement ps =
                        conexao.prepareStatement(
                            "SELECT pg_advisory_xact_lock(2026, 4)"
                        )) {
                    ps.execute();
                }

                int data = converterData(pagamento.getData());

                Integer sequencia = pagamento.getSequencia();

                if (sequencia == null) {
                    sequencia = proximaSequencia(
                        conexao,
                        pagamento.getCodigoCliente(),
                        data
                    );
                }

                String sql = """
                    INSERT INTO pagamentos
                        (codigo_cliente, data, sequencia,
                         valor, pagto_total)
                    VALUES (?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement ps =
                        conexao.prepareStatement(sql)) {

                    ps.setInt(1, pagamento.getCodigoCliente());
                    ps.setInt(2, data);
                    ps.setInt(3, sequencia);
                    ps.setDouble(4, pagamento.getValor());
                    ps.setBoolean(5, pagamento.getPagamentoTotal());

                    ps.executeUpdate();
                }

                conexao.commit();
                pagamento.setSequencia(sequencia);

                return pagamento;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }
        }
    }

    public boolean atualizar(Pagamento pagamento)
            throws SQLException {

        String sql = """
            UPDATE pagamentos
            SET valor = ?, pagto_total = ?
            WHERE codigo_cliente = ?
              AND data = ?
              AND sequencia = ?
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setDouble(1, pagamento.getValor());
            ps.setBoolean(2, pagamento.getPagamentoTotal());
            ps.setInt(3, pagamento.getCodigoCliente());
            ps.setInt(4, converterData(pagamento.getData()));
            ps.setInt(5, pagamento.getSequencia());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(
            int codigoCliente,
            LocalDate data,
            int sequencia) throws SQLException {

        String sql = """
            DELETE FROM pagamentos
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
        }
    }
}
