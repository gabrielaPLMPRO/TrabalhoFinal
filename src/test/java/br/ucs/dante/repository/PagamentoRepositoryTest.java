
package br.ucs.dante.repository;

import br.ucs.dante.model.Cliente;
import br.ucs.dante.model.Pagamento;
import br.ucs.dante.service.PagamentoService;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoRepositoryTest {

    @Test
    void deveCadastrarAtualizarEExcluirPagamento()
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

        PagamentoRepository pagamentos =
            new PagamentoRepository(database);

        PagamentoService service =
            new PagamentoService();

        Cliente cliente = new Cliente(
            null,
            "Cliente Teste Pagamento",
            null,
            null,
            null
        );

        Integer codigoCliente = null;
        Integer sequencia = null;

        LocalDate data = LocalDate.of(2026, 11, 13);

        try {
            clientes.salvar(cliente);
            codigoCliente = cliente.getCodigo();

            Pagamento pagamento = new Pagamento(
                codigoCliente,
                data,
                null,
                50.00,
                false
            );

            service.validar(pagamento);
            pagamentos.cadastrar(pagamento);

            sequencia = pagamento.getSequencia();

            assertNotNull(sequencia);
            assertTrue(sequencia > 0);

            // Consultar.
            Pagamento encontrado = pagamentos.buscar(
                codigoCliente, data, sequencia
            );

            assertNotNull(encontrado);
            assertEquals(50.00, encontrado.getValor(), 0.001);
            assertFalse(encontrado.getPagamentoTotal());

            // Atualizar.
            pagamento.setValor(80.00);
            pagamento.setPagamentoTotal(true);

            service.validar(pagamento);
            assertTrue(pagamentos.atualizar(pagamento));

            Pagamento atualizado = pagamentos.buscar(
                codigoCliente, data, sequencia
            );

            assertNotNull(atualizado);
            assertEquals(80.00, atualizado.getValor(), 0.001);
            assertTrue(atualizado.getPagamentoTotal());

            // Verificar listagem.
            Integer clienteConsultado = codigoCliente;
            Integer sequenciaConsultada = sequencia;

            assertTrue(
                pagamentos.listar().stream().anyMatch(p ->
                    p.getCodigoCliente().equals(clienteConsultado)
                    && p.getData().equals(data)
                    && p.getSequencia().equals(sequenciaConsultada)
                )
            );

            // RN-02: cliente com pagamento não pode ser excluído.
            Integer clienteVinculado = codigoCliente;

            assertThrows(
                IllegalStateException.class,
                () -> clientes.excluir(clienteVinculado)
            );

            // Excluir pagamento.
            assertTrue(pagamentos.excluir(
                codigoCliente, data, sequencia
            ));

            sequencia = null;

            assertNull(pagamentos.buscar(
                codigoCliente, data, pagamento.getSequencia()
            ));

        } finally {
            if (codigoCliente != null && sequencia != null) {
                pagamentos.excluir(
                    codigoCliente, data, sequencia
                );
            }

            if (codigoCliente != null &&
                    clientes.existe(codigoCliente)) {
                clientes.excluir(codigoCliente);
            }
        }
    }
}
