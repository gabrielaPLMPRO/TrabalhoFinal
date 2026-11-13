
package br.ucs.dante.model;

import java.time.LocalDate;

public class Consumo {

    private Integer codigoCliente;
    private LocalDate data;
    private Integer sequencia;
    private Integer codigoProduto;
    private Integer quantidade;
    private Double valor;
    private Boolean pago;

    public Consumo(
            Integer codigoCliente,
            LocalDate data,
            Integer sequencia,
            Integer codigoProduto,
            Integer quantidade,
            Double valor,
            Boolean pago) {

        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.pago = pago;
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

    public Integer getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Integer codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }
}
