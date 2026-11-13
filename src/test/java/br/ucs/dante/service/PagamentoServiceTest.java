
package br.ucs.dante.service;

import br.ucs.dante.model.Pagamento;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoServiceTest {

    private final PagamentoService service =
        new PagamentoService();

    private Pagamento pagamentoValido() {
        return new Pagamento(
            1,
            LocalDate.of(2026, 11, 13),
            null,
            50.00,
            false
        );
    }

    @Test
    void deveAceitarPagamentoValido() {
        assertDoesNotThrow(
            () -> service.validar(pagamentoValido())
        );
    }

    @Test
    void deveImpedirClienteInvalido() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setCodigoCliente(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(pagamento)
        );
    }

    @Test
    void deveImpedirDataVazia() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setData(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(pagamento)
        );
    }

    @Test
    void deveImpedirSequenciaNegativa() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setSequencia(-1);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(pagamento)
        );
    }

    @Test
    void deveImpedirValorNegativo() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setValor(-10.00);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(pagamento)
        );
    }

    @Test
    void deveImpedirValorInvalido() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setValor(Double.NaN);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(pagamento)
        );
    }

    @Test
    void deveImpedirSituacaoNaoInformada() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setPagamentoTotal(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(pagamento)
        );
    }

    @Test
    void deveAceitarPagamentoTotal() {
        Pagamento pagamento = pagamentoValido();
        pagamento.setPagamentoTotal(true);

        assertDoesNotThrow(
            () -> service.validar(pagamento)
        );
    }
}
