
package br.ucs.dante.model;

import java.time.LocalDate;

public class Pagamento {

    private Integer codigoCliente;
    private LocalDate data;
    private Integer sequencia;
    private Double valor;
    private Boolean pagamentoTotal;

    public Pagamento(
            Integer codigoCliente,
            LocalDate data,
            Integer sequencia,
            Double valor,
            Boolean pagamentoTotal) {

        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.valor = valor;
        this.pagamentoTotal = pagamentoTotal;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getPagamentoTotal() {
        return pagamentoTotal;
    }

    public void setPagamentoTotal(Boolean pagamentoTotal) {
        this.pagamentoTotal = pagamentoTotal;
    }
}
