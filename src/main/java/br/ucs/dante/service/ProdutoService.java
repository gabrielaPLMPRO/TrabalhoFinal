
package br.ucs.dante.service;

import br.ucs.dante.model.Produto;

public class ProdutoService {

    public void validar(Produto produto) {

        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não informado."
            );
        }

        if (produto.getCodigo() != null
                && produto.getCodigo() <= 0) {
            throw new IllegalArgumentException(
                "O código deve ser positivo."
            );
        }

        if (produto.getDescricao() == null
                || produto.getDescricao().isBlank()) {
            throw new IllegalArgumentException(
                "A descrição é obrigatória."
            );
        }

        if (produto.getDescricao().length() > 100) {
            throw new IllegalArgumentException(
                "A descrição deve ter no máximo 100 caracteres."
            );
        }

        if (produto.getPreco() == null
                || !Double.isFinite(produto.getPreco())
                || produto.getPreco() < 0) {
            throw new IllegalArgumentException(
                "O preço deve ser um número válido e não negativo."
            );
        }

        if (produto.getUnidade() == null
                || produto.getUnidade().isBlank()) {
            throw new IllegalArgumentException(
                "A unidade é obrigatória."
            );
        }

        if (produto.getUnidade().length() > 100) {
            throw new IllegalArgumentException(
                "A unidade deve ter no máximo 100 caracteres."
            );
        }
    }
}
