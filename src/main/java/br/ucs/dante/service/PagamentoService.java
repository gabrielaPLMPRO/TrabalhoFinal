
package br.ucs.dante.service;

import br.ucs.dante.model.Pagamento;

public class PagamentoService {

    public void validar(Pagamento pagamento) {

        if (pagamento == null) {
            throw new IllegalArgumentException(
                "Pagamento não informado."
            );
        }

        if (pagamento.getCodigoCliente() == null
                || pagamento.getCodigoCliente() <= 0) {
            throw new IllegalArgumentException(
                "Informe um cliente válido."
            );
        }

        if (pagamento.getData() == null) {
            throw new IllegalArgumentException(
                "A data do pagamento é obrigatória."
            );
        }

        if (pagamento.getSequencia() != null
                && pagamento.getSequencia() <= 0) {
            throw new IllegalArgumentException(
                "A sequência deve ser positiva."
            );
        }

        if (pagamento.getValor() == null
                || !Double.isFinite(pagamento.getValor())
                || pagamento.getValor() < 0) {
            throw new IllegalArgumentException(
                "O valor deve ser válido e não negativo."
            );
        }

        if (pagamento.getPagamentoTotal() == null) {
            throw new IllegalArgumentException(
                "Informe se o pagamento é total."
            );
        }
    }
}
