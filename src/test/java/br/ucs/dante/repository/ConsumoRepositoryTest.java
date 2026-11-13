
package br.ucs.dante.repository;

import br.ucs.dante.model.Cliente;
import br.ucs.dante.model.Produto;
import br.ucs.dante.model.Consumo;
import br.ucs.dante.service.ConsumoService;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumoRepositoryTest {

    @Test
    void deveCadastrarConsultarAtualizarEExcluirConsumo()
            throws Exception {

        String url = System.getenv("DANTE_TEST_DB_URL");
        String usuario = System.getenv("DANTE_TEST_DB_USER");
        String senha = System.getenv("DANTE_TEST_DB_PASSWORD");

        assertEquals(
            "jdbc:postgresql://localhost:5432/dante_squash_test",
            url,
            "Utilize somente o banco de testes."
        );

        assertNotNull(usuario);
        assertNotNull(senha);

        DatabaseConnection database =
            new DatabaseConnection(url, usuario, senha);

        ClienteRepository clientes =
            new ClienteRepository(database);

        ProdutoRepository produtos =
            new ProdutoRepository(database);

        ConsumoRepository consumos =
            new ConsumoRepository(database);

        ConsumoService service = new ConsumoService();

        Cliente cliente = new Cliente(
            null,
            "Cliente Teste Consumo",
            null,
            null,
            null
        );

        Produto produto = new Produto(
            null,
            "Produto Teste Consumo",
            10.00,
            "UN"
        );

        Integer codigoCliente = null;
        Integer codigoProduto = null;
        Integer sequencia = null;

        LocalDate data = LocalDate.of(2026, 11, 13);

        try {
            // Criar registros relacionados.
            clientes.salvar(cliente);
            codigoCliente = cliente.getCodigo();

            produtos.salvar(produto);
            codigoProduto = produto.getCodigo();

            // Cadastrar consumo.
            Consumo consumo = new Consumo(
                codigoCliente,
                data,
                null,
                codigoProduto,
                2,
                20.00,
                false
            );

            service.validar(consumo);
            consumos.cadastrar(consumo);

            sequencia = consumo.getSequencia();

            assertNotNull(sequencia);
            assertTrue(sequencia > 0);

            // Consultar consumo.
            Consumo encontrado = consumos.buscar(
                codigoCliente,
                data,
                sequencia
            );

            assertNotNull(encontrado);
            assertEquals(2, encontrado.getQuantidade());
            assertEquals(20.00, encontrado.getValor(), 0.001);
            assertFalse(encontrado.getPago());

            // Atualizar consumo.
            consumo.setQuantidade(3);
            consumo.setValor(30.00);
            consumo.setPago(true);

            service.validar(consumo);

            assertTrue(consumos.atualizar(consumo));

            Consumo atualizado = consumos.buscar(
                codigoCliente,
                data,
                sequencia
            );

            assertNotNull(atualizado);
            assertEquals(3, atualizado.getQuantidade());
            assertEquals(30.00, atualizado.getValor(), 0.001);
            assertTrue(atualizado.getPago());

            // Confirmar presença na listagem.
            Integer clienteConsultado = codigoCliente;
            Integer sequenciaConsultada = sequencia;

            assertTrue(
                consumos.listar().stream().anyMatch(c ->
                    c.getCodigoCliente().equals(clienteConsultado)
                    && c.getData().equals(data)
                    && c.getSequencia().equals(sequenciaConsultada)
                )
            );

            // Testar integridade referencial.
            Integer clienteVinculado = codigoCliente;
            Integer produtoVinculado = codigoProduto;

            assertThrows(
                IllegalStateException.class,
                () -> clientes.excluir(clienteVinculado)
            );

            assertThrows(
                IllegalStateException.class,
                () -> produtos.excluir(produtoVinculado)
            );

            // Excluir consumo.
            assertTrue(consumos.excluir(
                codigoCliente,
                data,
                sequencia
            ));

            assertNull(consumos.buscar(
                codigoCliente,
                data,
                sequencia
            ));

            sequencia = null;

        } finally {
            // Limpeza dos registros criados pelo teste.
            if (codigoCliente != null && sequencia != null) {
                consumos.excluir(codigoCliente, data, sequencia);
            }

            if (codigoProduto != null &&
                    produtos.existe(codigoProduto)) {
                produtos.excluir(codigoProduto);
            }

            if (codigoCliente != null &&
                    clientes.existe(codigoCliente)) {
                clientes.excluir(codigoCliente);
            }
        }
    }
}
