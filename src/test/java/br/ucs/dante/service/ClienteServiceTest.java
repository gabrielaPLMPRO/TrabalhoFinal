
package br.ucs.dante.service;

import br.ucs.dante.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteServiceTest {

    private final ClienteService service = new ClienteService();

    @Test
    void deveRejeitarNomeComApenasEspacosConformeRN01() {
        assertThrows(IllegalArgumentException.class,
            () -> service.validar(new Cliente(null, "   ", null, null, null)));
    }

    @Test
    void deveAceitarNomeNoLimiteDe50CaracteresConformeRN01() {
        assertDoesNotThrow(
            () -> service.validar(new Cliente(null, "A".repeat(50), null, null, null)));
    }

    @Test
    void deveRejeitarClienteSemNome() {

        Cliente cliente = new Cliente(
            null, "", null, null, null
        );

        IllegalArgumentException erro = assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(cliente)
        );

        assertEquals(
            "O nome do cliente é obrigatório.",
            erro.getMessage()
        );
    }

    @Test
    void deveRejeitarNomeComMaisDe50Caracteres() {

        Cliente cliente = new Cliente(
            null, "A".repeat(51), null, null, null
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(cliente)
        );
    }

    @Test
    void deveAceitarClienteValido() {

        Cliente cliente = new Cliente(
            null, "Cliente Teste", "12345678901", null, null
        );

        assertDoesNotThrow(
            () -> service.validar(cliente)
        );
    }
}
