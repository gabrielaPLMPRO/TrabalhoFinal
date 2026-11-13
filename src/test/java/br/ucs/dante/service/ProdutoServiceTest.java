
package br.ucs.dante.service;

import br.ucs.dante.model.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoServiceTest {

    private final ProdutoService service = new ProdutoService();

    @Test
    void deveAceitarProdutoValido() {
        Produto produto = new Produto(
            null, "Água mineral", 5.50, "UN"
        );

        assertDoesNotThrow(() -> service.validar(produto));
    }

    @Test
    void deveImpedirDescricaoVazia() {
        Produto produto = new Produto(
            null, "", 5.50, "UN"
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(produto)
        );
    }

    @Test
    void deveImpedirDescricaoMaiorQue100Caracteres() {
        Produto produto = new Produto(
            null, "A".repeat(101), 5.50, "UN"
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(produto)
        );
    }

    @Test
    void deveImpedirPrecoNegativo() {
        Produto produto = new Produto(
            null, "Água mineral", -2.00, "UN"
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(produto)
        );
    }

    @Test
    void deveImpedirUnidadeVazia() {
        Produto produto = new Produto(
            null, "Água mineral", 5.50, ""
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> service.validar(produto)
        );
    }
}
