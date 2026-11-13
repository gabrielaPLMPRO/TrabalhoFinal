
package br.ucs.dante.repository;
import br.ucs.dante.model.Cliente;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteRepositoryTest {

    @Test
    void deveCadastrarConsultarAtualizarEExcluirCliente()
            throws Exception {

        String url = System.getenv("DANTE_TEST_DB_URL");
        String usuario = System.getenv("DANTE_TEST_DB_USER");
        String senha = System.getenv("DANTE_TEST_DB_PASSWORD");

        assertNotNull(url, "Configure DANTE_TEST_DB_URL");
        assertNotNull(usuario, "Configure DANTE_TEST_DB_USER");
        assertNotNull(senha, "Configure DANTE_TEST_DB_PASSWORD");

        assertTrue(
                url.endsWith("/dante_squash_test"),
                "O teste deve usar exclusivamente o banco de testes.");

        DatabaseConnection database = new DatabaseConnection(url, usuario, senha);

        ClienteRepository repository = new ClienteRepository(database);

        Cliente cliente = new Cliente(
                null,
                "Cliente Teste Automatizado",
                "12345678901",
                LocalDate.of(2000, 8, 13),
                "54999999999");

        Integer codigo = null;

        try {
            // CADASTRAR
            repository.salvar(cliente);

            codigo = cliente.getCodigo();

            assertNotNull(codigo);
            assertTrue(codigo > 0);

            // CONSULTAR
            Cliente encontrado = repository.buscarPorCodigo(codigo);

            assertNotNull(encontrado);
            assertEquals(cliente.getNome(), encontrado.getNome());
            assertEquals(cliente.getCpf(), encontrado.getCpf());
            assertEquals(
                    cliente.getDataAniversario(),
                    encontrado.getDataAniversario());

            // ATUALIZAR
            cliente.setNome("Cliente Teste Atualizado");
            repository.salvar(cliente);

            Cliente atualizado = repository.buscarPorCodigo(codigo);

            assertEquals(
                    "Cliente Teste Atualizado",
                    atualizado.getNome());

            Integer codigoConsultado = codigo;

            assertTrue(
                    repository.listar().stream()
                            .anyMatch(c -> c.getCodigo().equals(codigoConsultado)));

            // EXCLUIR
            assertTrue(repository.excluir(codigo));

            // CONFIRMAR EXCLUSÃO
            assertNull(repository.buscarPorCodigo(codigo));

        } finally {
            if (codigo != null && repository.existe(codigo)) {
                repository.excluir(codigo);
            }
        }
    }


@Test
void deveImpedirExclusaoDeClienteComConsumo() throws Exception {

    String url = System.getenv("DANTE_TEST_DB_URL");
    String usuario = System.getenv("DANTE_TEST_DB_USER");
    String senha = System.getenv("DANTE_TEST_DB_PASSWORD");

    assertNotNull(url);
    assertNotNull(usuario);
    assertNotNull(senha);
    assertTrue(url.endsWith("/dante_squash_test"));

    DatabaseConnection database =
        new DatabaseConnection(url, usuario, senha);

    ClienteRepository repository =
        new ClienteRepository(database);

    Cliente cliente = new Cliente(
        null, "Cliente com Consumo", null, null, null
    );

    Integer codigoCliente = null;
    Integer codigoProduto = null;

    try {
        repository.salvar(cliente);
        codigoCliente = cliente.getCodigo();

        try (var conexao = database.conectar()) {

            // Cria um produto fictício para satisfazer a FK.
            try (var ps = conexao.prepareStatement("""
                
                INSERT INTO produtos
                (codigo, descricao, unidade, preco)
                VALUES (
                    (SELECT COALESCE(MAX(codigo), 0) + 1000
                     FROM produtos),
                    'Produto Teste RN02', 'UN', 10.0
                )
                RETURNING codigo
                """)) {

                try (var rs = ps.executeQuery()) {
                    rs.next();
                    codigoProduto = rs.getInt(1);
                }
            }

            // Vincula um consumo ao cliente.
            try (var ps = conexao.prepareStatement("""
                INSERT INTO consumos
                    (codigo_cliente, data, sequencia,
                     codigo_produto, quantidade, valor, pago)
                VALUES (?, 20261113, 1, ?, 1, 10.0, FALSE)
                """)) {

                ps.setInt(1, codigoCliente);
                ps.setInt(2, codigoProduto);
                ps.executeUpdate();
            }
        }

        Integer codigoTestado = codigoCliente;

        assertThrows(
            IllegalStateException.class,
            () -> repository.excluir(codigoTestado)
        );

        assertNotNull(
            repository.buscarPorCodigo(codigoCliente),
            "O cliente deve permanecer cadastrado."
        );

    } finally {
        try (var conexao = database.conectar()) {

            if (codigoCliente != null) {
                try (var ps = conexao.prepareStatement(
                    "DELETE FROM consumos WHERE codigo_cliente = ?")) {

                    ps.setInt(1, codigoCliente);
                    ps.executeUpdate();
                }
            }

            if (codigoProduto != null) {
                try (var ps = conexao.prepareStatement(
                    "DELETE FROM produtos WHERE codigo = ?")) {

                    ps.setInt(1, codigoProduto);
                    ps.executeUpdate();
                }
            }
        }

        if (codigoCliente != null &&
            repository.existe(codigoCliente)) {
            repository.excluir(codigoCliente);
        }
    }
}

}
