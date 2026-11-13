
package br.ucs.dante.repository;

import br.ucs.dante.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private final DatabaseConnection database;

    public ProdutoRepository(DatabaseConnection database) {
        this.database = database;
    }

    public List<Produto> listar() throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        String sql = """
            SELECT codigo, descricao, preco, unidade
            FROM produtos
            ORDER BY codigo
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapear(rs));
            }
        }

        return produtos;
    }

    public Produto buscarPorCodigo(int codigo) throws SQLException {
        String sql = """
            SELECT codigo, descricao, preco, unidade
            FROM produtos
            WHERE codigo = ?
            """;

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    public boolean existe(int codigo) throws SQLException {
        try (Connection conexao = database.conectar()) {
            return existeNaConexao(conexao, codigo);
        }
    }

    private boolean existeNaConexao(
            Connection conexao, int codigo) throws SQLException {

        String sql = "SELECT 1 FROM produtos WHERE codigo = ?";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int proximoCodigo(Connection conexao) throws SQLException {
        String sql = """
            SELECT COALESCE(MAX(codigo), 0) + 1
            FROM produtos
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public Produto salvar(Produto produto) throws SQLException {
        try (Connection conexao = database.conectar()) {
            conexao.setAutoCommit(false);

            try {
                // Impede conflito entre cadastros simultaneos.
                try (PreparedStatement ps = conexao.prepareStatement(
                        "SELECT pg_advisory_xact_lock(2026, 2)")) {
                    ps.execute();
                }

                Integer codigo = produto.getCodigo();

                if (codigo == null) {
                    codigo = proximoCodigo(conexao);
                    inserir(conexao, codigo, produto);

                } else if (existeNaConexao(conexao, codigo)) {
                    atualizar(conexao, codigo, produto);

                } else {
                    inserir(conexao, codigo, produto);
                }

                conexao.commit();
                produto.setCodigo(codigo);

                return produto;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }
        }
    }

    private void inserir(
            Connection conexao,
            int codigo,
            Produto produto) throws SQLException {

        String sql = """
            INSERT INTO produtos
                (codigo, descricao, preco, unidade)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.setString(2, produto.getDescricao());
            ps.setDouble(3, produto.getPreco());
            ps.setString(4, produto.getUnidade());
            ps.executeUpdate();
        }
    }

    private void atualizar(
            Connection conexao,
            int codigo,
            Produto produto) throws SQLException {

        String sql = """
            UPDATE produtos
            SET descricao = ?, preco = ?, unidade = ?
            WHERE codigo = ?
            """;

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, produto.getDescricao());
            ps.setDouble(2, produto.getPreco());
            ps.setString(3, produto.getUnidade());
            ps.setInt(4, codigo);
            ps.executeUpdate();
        }
    }

    public boolean excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM produtos WHERE codigo = ?";

        try (Connection conexao = database.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;

        } catch (SQLException erro) {
            if ("23503".equals(erro.getSQLState())) {
                throw new IllegalStateException(
                    "Não é possível excluir um produto " +
                    "que possui consumos vinculados.",
                    erro
                );
            }

            throw erro;
        }
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        return new Produto(
            rs.getInt("codigo"),
            rs.getString("descricao"),
            rs.getDouble("preco"),
            rs.getString("unidade")
        );
    }
}
