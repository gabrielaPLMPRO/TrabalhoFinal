
package br.ucs.dante.repository;

import br.ucs.dante.model.Cliente;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private final DatabaseConnection database;

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("ddMMuuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public ClienteRepository(DatabaseConnection database) {
        this.database = database;
    }

    public List<Cliente> listar() throws SQLException {
        String sql = """
            SELECT codigo, nome, cpf, dataaniversario, telefone
            FROM clientes
            WHERE codigo > 0
            ORDER BY nome
            """;

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapear(rs));
            }
        }

        return clientes;
    }

    public Cliente buscarPorCodigo(int codigo) throws SQLException {
        String sql = """
            SELECT codigo, nome, cpf, dataaniversario, telefone
            FROM clientes
            WHERE codigo = ?
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean existe(int codigo) throws SQLException {
        return buscarPorCodigo(codigo) != null;
    }

    public boolean possuiVinculos(int codigo) throws SQLException {
        try (Connection conexao = database.conectar()) {
            return possuiVinculos(conexao, codigo);
        }
    }

    private boolean possuiVinculos(
            Connection conexao, int codigo) throws SQLException {

        String sql = """
            SELECT
                EXISTS(SELECT 1 FROM consumos
                       WHERE codigo_cliente = ?)
                OR EXISTS(SELECT 1 FROM pagamentos
                          WHERE codigo_cliente = ?)
                OR EXISTS(SELECT 1 FROM consumossauna
                          WHERE codigo_cliente = ?)
                OR EXISTS(SELECT 1 FROM horariossquash
                          WHERE codigo_cliente = ?)
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) {
                ps.setInt(i, codigo);
            }

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        }
    }

    public Cliente salvar(Cliente cliente) throws SQLException {

        try (Connection conexao = database.conectar()) {
            conexao.setAutoCommit(false);

            try {
                // Serializa cadastros e atualizações que utilizam
                // este repositório, evitando códigos duplicados.
                try (PreparedStatement ps = conexao.prepareStatement(
                        "SELECT pg_advisory_xact_lock(2026, 1)")) {
                    ps.execute();
                }

                Integer codigo = cliente.getCodigo();

                if (codigo == null) {
                    codigo = proximoCodigo(conexao);
                    inserir(conexao, codigo, cliente);
                } else if (existeNaConexao(conexao, codigo)) {
                    atualizar(conexao, codigo, cliente);
                } else {
                    inserir(conexao, codigo, cliente);
                }

                conexao.commit();
                cliente.setCodigo(codigo);
                return cliente;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }
        }
    }

    private int proximoCodigo(Connection conexao) throws SQLException {
        String sql = """
            SELECT COALESCE(MAX(codigo), 0) + 1
            FROM clientes
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return Math.max(1, rs.getInt(1));
        }
    }

    private boolean existeNaConexao(
            Connection conexao, int codigo) throws SQLException {

        try (PreparedStatement ps = conexao.prepareStatement(
                "SELECT 1 FROM clientes WHERE codigo = ?")) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void inserir(
            Connection conexao, int codigo, Cliente cliente)
            throws SQLException {

        String sql = """
            INSERT INTO clientes
                (codigo, nome, cpf, dataaniversario, telefone)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            preencherCampos(ps, codigo, cliente);
            ps.executeUpdate();
        }
    }

    private void atualizar(
            Connection conexao, int codigo, Cliente cliente)
            throws SQLException {

        String sql = """
            UPDATE clientes
            SET nome = ?, cpf = ?, dataaniversario = ?,
                telefone = ?
            WHERE codigo = ?
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, formatarData(cliente.getDataAniversario()));
            ps.setString(4, cliente.getTelefone());
            ps.setInt(5, codigo);
            ps.executeUpdate();
        }
    }

    private void preencherCampos(
            PreparedStatement ps, int codigo, Cliente cliente)
            throws SQLException {

        ps.setInt(1, codigo);
        ps.setString(2, cliente.getNome());
        ps.setString(3, cliente.getCpf());
        ps.setString(4, formatarData(cliente.getDataAniversario()));
        ps.setString(5, cliente.getTelefone());
    }

    public boolean excluir(int codigo) throws SQLException {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código inválido.");
        }

        try (Connection conexao = database.conectar()) {
            conexao.setAutoCommit(false);

            try {
                // Bloqueia o cliente durante a exclusão.
                try (PreparedStatement ps = conexao.prepareStatement(
                        "SELECT codigo FROM clientes " +
                        "WHERE codigo = ? FOR UPDATE")) {

                    ps.setInt(1, codigo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conexao.rollback();
                            return false;
                        }
                    }
                }

                if (possuiVinculos(conexao, codigo)) {
                    throw new IllegalStateException(
                        "O cliente possui registros vinculados " +
                        "e não pode ser excluído."
                    );
                }

                int linhas;

                try (PreparedStatement ps = conexao.prepareStatement(
                        "DELETE FROM clientes WHERE codigo = ?")) {

                    ps.setInt(1, codigo);
                    linhas = ps.executeUpdate();
                }

                conexao.commit();
                return linhas > 0;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }
        }
    }

    private String formatarData(LocalDate data) {
        return data == null ? null : data.format(formatoData);
    }

    private LocalDate converterData(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return LocalDate.parse(valor, formatoData);
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("codigo"),
            rs.getString("nome"),
            rs.getString("cpf"),
            converterData(rs.getString("dataaniversario")),
            rs.getString("telefone")
        );
    }
}
