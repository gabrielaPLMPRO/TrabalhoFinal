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
public class VoPagamento implements I_VO {
    // atributos
    private int codigoCliente;
    private int data;
    private int sequencia;
    private double valor;
    private boolean pagarTudo;
    private String nome;

    // construtor
    public VoPagamento(int codigoCliente, int data, double valor) {
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = 0;
        this.valor = valor;
        this.pagarTudo = false;
        this.nome = "";
    }

    // construtor usado para a ficha do cliente
    public VoPagamento(int codigoCliente) {
        this.codigoCliente = codigoCliente;
        this.data = 0;
        this.valor = 0;
        this.sequencia = 0;
        this.pagarTudo = false;        
        this.nome = "";         
    }    
    
    // construtor
    public VoPagamento(int codigoCliente, int data, int sequencia, double valor) {
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.valor = valor;
        this.pagarTudo = false;
    }    
    
    // construtor    
    public VoPagamento() {
        this.codigoCliente = 0;
        this.data = 0;
        this.valor = 0;
        this.sequencia = 0;
        this.pagarTudo = false;        
        this.nome = "";        
    }

    // construtor usado para o pagamento da sauna
    public VoPagamento(int data, double valor, String nome) {
        this.data = data;
        this.valor = valor;
        this.nome = nome;
        this.codigoCliente = 0;
        this.sequencia = 0;
        this.pagarTudo = true;          
    }    

    // construtor para restaurar backup
    public VoPagamento(int codigoCliente, int data, int sequencia, double valor, boolean pagarTudo) {
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.valor = valor;
        this.pagarTudo = pagarTudo;
    }
    
    // getters and setters
    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    @Override
    public String toString() {
        return "VoPagamento{" + "codigoCliente=" + codigoCliente + ", data=" + data + ", sequencia=" + sequencia + ", valor=" + valor + ", pagarTudo=" + pagarTudo + '}';
    }

    public boolean isPagarTudo() {
        return pagarTudo;
    }

    public void setPagarTudo(boolean pagarTudo) {
        this.pagarTudo = pagarTudo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
