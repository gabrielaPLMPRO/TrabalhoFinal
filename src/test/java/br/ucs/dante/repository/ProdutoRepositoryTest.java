package br.ucs.dante.repository;

import br.ucs.dante.model.Produto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

public class ProdutoRepositoryTest {

    @Test
    void deveCadastrarConsultarAtualizarEExcluirProduto()
            throws Exception {

        String url = System.getenv("DANTE_TEST_DB_URL");
        String usuario = System.getenv("DANTE_TEST_DB_USER");
        String senha = System.getenv("DANTE_TEST_DB_PASSWORD");

        assertNotNull(url, "Configure DANTE_TEST_DB_URL");
        assertNotNull(usuario, "Configure DANTE_TEST_DB_USER");
        assertNotNull(senha, "Configure DANTE_TEST_DB_PASSWORD");

        assertEquals(
            "jdbc:postgresql://localhost:5432/dante_squash_test",
            url,
            "Os testes devem usar somente o banco de testes."
        );

        DatabaseConnection database =
            new DatabaseConnection(url, usuario, senha);

        ProdutoRepository repository =
            new ProdutoRepository(database);

        Produto produto = new Produto(
            null,
            "Produto Teste Automatizado",
            12.50,
            "UN"
        );

        Integer codigo = null;

        try {
            // Cadastrar
            repository.salvar(produto);
            codigo = produto.getCodigo();

            assertNotNull(codigo);
            assertTrue(codigo > 0);

            // Consultar
            Produto encontrado =
                repository.buscarPorCodigo(codigo);

            assertNotNull(encontrado);
            assertEquals(
                "Produto Teste Automatizado",
                encontrado.getDescricao()
            );
            assertEquals(12.50, encontrado.getPreco(), 0.001);
            assertEquals("UN", encontrado.getUnidade());

            // Atualizar
            produto.setDescricao("Produto Atualizado");
            produto.setPreco(19.90);

            repository.salvar(produto);

            Produto atualizado =
                repository.buscarPorCodigo(codigo);

            assertNotNull(atualizado);
            assertEquals(
                "Produto Atualizado",
                atualizado.getDescricao()
            );
            assertEquals(19.90, atualizado.getPreco(), 0.001);

            // Listar
            Integer codigoConsultado = codigo;

            assertTrue(
                repository.listar().stream().anyMatch(
                    p -> p.getCodigo().equals(codigoConsultado)
                )
            );

            // Excluir
            assertTrue(repository.excluir(codigo));
            assertNull(repository.buscarPorCodigo(codigo));

        } finally {
            // Remove o produto de teste caso ainda exista.
            if (codigo != null && repository.existe(codigo)) {
                repository.excluir(codigo);
            }
        }
    }


@Test
void deveImpedirExclusaoDeProdutoComConsumo() throws Exception {

    String url = System.getenv("DANTE_TEST_DB_URL");
    String usuario = System.getenv("DANTE_TEST_DB_USER");
    String senha = System.getenv("DANTE_TEST_DB_PASSWORD");

    assertEquals(
        "jdbc:postgresql://localhost:5432/dante_squash_test",
        url,
        "Este teste deve usar somente o banco de testes."
    );

    DatabaseConnection database =
        new DatabaseConnection(url, usuario, senha);

    ProdutoRepository repository =
        new ProdutoRepository(database);

    Integer codigoProduto = null;
    Integer codigoCliente = null;

    try {
        Produto produto = new Produto(
            null,
            "Produto com consumo de teste",
            8.50,
            "UN"
        );

        repository.salvar(produto);
        codigoProduto = produto.getCodigo();

        // Criar um cliente temporario para associar o consumo.
        try (Connection conexao = database.conectar()) {

            try (var ps = conexao.prepareStatement("""
                INSERT INTO clientes (codigo, nome)
                VALUES (
                    (SELECT COALESCE(MAX(codigo), 0) + 1000
                     FROM clientes),
                    'Cliente Teste Produto'
                )
                RETURNING codigo
                """)) {

                try (var rs = ps.executeQuery()) {
                    rs.next();
                    codigoCliente = rs.getInt(1);
                }
            }

            try (var ps = conexao.prepareStatement("""
                INSERT INTO consumos
                    (codigo_cliente, data, sequencia,
                     codigo_produto, quantidade, valor, pago)
                VALUES (?, 20261113, 1, ?, 1, 8.50, FALSE)
                """)) {

                ps.setInt(1, codigoCliente);
                ps.setInt(2, codigoProduto);
                ps.executeUpdate();
            }
        }

        Integer codigoParaExcluir = codigoProduto;

        IllegalStateException erro = assertThrows(
            IllegalStateException.class,
            () -> repository.excluir(codigoParaExcluir)
        );

        assertTrue(
            erro.getMessage().contains("consumos vinculados")
        );

        assertNotNull(repository.buscarPorCodigo(codigoProduto));

    } finally {
        try (Connection conexao = database.conectar()) {

            if (codigoCliente != null) {
                try (var ps = conexao.prepareStatement(
                        "DELETE FROM consumos WHERE codigo_cliente = ?")) {
                    ps.setInt(1, codigoCliente);
                    ps.executeUpdate();
                }

                try (var ps = conexao.prepareStatement(
                        "DELETE FROM clientes WHERE codigo = ?")) {
                    ps.setInt(1, codigoCliente);
                    ps.executeUpdate();
                }
            }
        }

        if (codigoProduto != null && repository.existe(codigoProduto)) {
            repository.excluir(codigoProduto);
        }
    }
}

}
