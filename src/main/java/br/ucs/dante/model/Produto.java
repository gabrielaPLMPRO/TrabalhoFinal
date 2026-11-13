
package br.ucs.dante.model;

public class Produto {

    private Integer codigo;
    private String descricao;
    private Double preco;
    private String unidade;

    public Produto(
            Integer codigo,
            String descricao,
            Double preco,
            String unidade) {

        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
        this.unidade = unidade;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
