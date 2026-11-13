/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vo.cadastros;

import model.vo.I_VO;

/**
 *
 * @author dlnotari
 */
public class VoProduto implements I_VO{
    // constantes
    public static final int SaldoPagamento = 0;
    public static final int CodigoSauna = 1;
    public static final int CodigoMensalidade = -1;
    public static final int MultaMensalidade1 = -2;
    public static final int MultaMensalidade2 = -3;
    
    // atributos
    private int codigo;
    private String descricao;
    private double preco;
    private String unidade;

    // construtor
    public VoProduto(int codigo, String descricao, double preco, String unidade) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
        this.unidade = unidade;
    }

    // construtor
    public VoProduto() {
        this.codigo = 0;
        this.descricao = "";
        this.preco = 0;
        this.unidade = "";
    }

    // construtor
    public VoProduto(int codigo, double preco) {
        this.codigo = codigo;
        this.preco = preco;
        this.descricao = "";
        this.unidade = "";        
    }

    // construtor
    public VoProduto(int codigo) {
        this.codigo = codigo;
        this.descricao = "";
        this.preco = 0;
        this.unidade = "";
    }

    // getters and setters
    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public String getUnidade() {
        return unidade;
    }

    @Override
    public String toString() {
        return "VoProduto{" + "codigo=" + codigo + ", descricao=" + descricao
                + ", preco=" + preco + ", unidade=" + unidade + '}';
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
