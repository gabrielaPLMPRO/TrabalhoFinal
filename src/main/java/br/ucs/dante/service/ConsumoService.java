
package br.ucs.dante.service;

import br.ucs.dante.model.Consumo;

public class ConsumoService {

    public void validar(Consumo consumo) {
        if (consumo == null) {
            throw new IllegalArgumentException(
                "Consumo não informado."
            );
        }

        if (consumo.getCodigoCliente() == null
                || consumo.getCodigoCliente() <= 0) {
            throw new IllegalArgumentException(
                "Informe um cliente válido."
            );
        }

        if (consumo.getData() == null) {
            throw new IllegalArgumentException(
                "A data do consumo é obrigatória."
            );
        }

        if (consumo.getSequencia() != null
                && consumo.getSequencia() <= 0) {
            throw new IllegalArgumentException(
                "A sequência deve ser positiva."
            );
        }

        if (consumo.getCodigoProduto() == null
                || consumo.getCodigoProduto() <= 0) {
            throw new IllegalArgumentException(
                "Informe um produto válido."
            );
        }

        if (consumo.getQuantidade() == null
                || consumo.getQuantidade() <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }

        if (consumo.getValor() == null
                || !Double.isFinite(consumo.getValor())
                || consumo.getValor() < 0) {
            throw new IllegalArgumentException(
                "O valor deve ser válido e não negativo."
            );
        }

        if (consumo.getPago() == null) {
            throw new IllegalArgumentException(
                "Informe a situação do pagamento."
            );
        }
    }
}
