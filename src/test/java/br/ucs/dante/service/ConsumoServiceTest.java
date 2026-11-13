
package br.ucs.dante.service;

import br.ucs.dante.model.Consumo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumoServiceTest {

    private final ConsumoService service =
        new ConsumoService();

    private Consumo consumoValido() {
        return new Consumo(
            1,
            LocalDate.of(2026, 11, 13),
            null,
            1,
            2,
            10.00,
            false
        );
    }

    @Test
    void deveAceitarConsumoValido() {
        assertDoesNotThrow(
            () -> service.validar(consumoValido())
        );
    }

    @Test
    void deveImpedirClienteInvalido() {
        Consumo consumo = consumoValido();
        consumo.setCodigoCliente(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }

    @Test
    void deveImpedirDataVazia() {
        Consumo consumo = consumoValido();
        consumo.setData(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }

    @Test
    void deveImpedirProdutoInvalido() {
        Consumo consumo = consumoValido();
        consumo.setCodigoProduto(0);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }

    @Test
    void deveImpedirQuantidadeZero() {
        Consumo consumo = consumoValido();
        consumo.setQuantidade(0);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }

    @Test
    void deveImpedirValorNegativo() {
        Consumo consumo = consumoValido();
        consumo.setValor(-1.00);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }

    @Test
    void deveImpedirPagamentoNaoInformado() {
        Consumo consumo = consumoValido();
        consumo.setPago(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }

    @Test
    void deveImpedirSequenciaNegativa() {
        Consumo consumo = consumoValido();
        consumo.setSequencia(-1);

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(consumo)
        );
    }
}
