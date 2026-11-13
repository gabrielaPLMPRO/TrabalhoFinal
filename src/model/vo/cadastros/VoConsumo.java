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
public class VoConsumo extends VoProduto implements I_VO{
    // atributos
    private int codigoCliente;
    private String data;
    private int sequencia;
    private int quantidade;
    private boolean pago;
    
    // construtor
    public VoConsumo(int codigoCliente, String data, int sequencia, int quantidade,
            boolean pago, int codigo, double preco) {
        super(codigo, preco);
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.quantidade = quantidade;
        this.pago = pago;
    }

    /***
     * construtor usado para incluir o produto sauna
     * @param codigoCliente
     * @param data
     * @param codigo
     * @param preco 
     */
    public VoConsumo(int codigoCliente, String data, double preco) {
        super(0, "sauna", preco, "un" );
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = 0;
        this.quantidade = 1;
        this.pago = false;
    }
    
    // construtor
    public VoConsumo() {
        super();
        this.codigoCliente = 0;
        this.data = "";
        this.sequencia = 0;
        this.quantidade = 0;
        this.pago = false;        
    }

    // construtor
    public VoConsumo(String data) {
        super();
        this.codigoCliente = 0;
        this.data = data;
        this.sequencia = 0;
        this.quantidade = 0;
        this.pago = false;        
    }    
    
    // construtor
    public VoConsumo(int codigoCliente, String data) {
        super();
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = 0;
        this.quantidade = 0;
        this.pago = false;        
    }

    // construtor
    public VoConsumo(int codigoCliente, String data, int sequencia) {
        super();
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.quantidade = 0;
        this.pago = false;        
    }    
    // construtor
    public VoConsumo(int codigoCliente) {
        super();
        this.codigoCliente = codigoCliente;
        this.data = "";
        this.sequencia = 0;
        this.quantidade = 0;
        this.pago = false;        
    }    
    
    // construtor
    public VoConsumo(int codigoCliente, String data, int sequencia, int quantidade,
            boolean pago, int codigo, String descricao, double preco, String unidade) {
        super(codigo, descricao, preco, unidade);
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.quantidade = quantidade;
        this.pago = pago;
    }

    // construtor para gerar mensalidade do squash
    public VoConsumo(int codigoCliente, String data, int codigo, double preco) {
        super(codigo, preco);
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.quantidade = 1;
        this.pago = false;
        this.setUnidade("un");
    }
    
    // construtor para restaurar backup
    public VoConsumo(int codigoCliente, String data, int sequencia, int codigo_produto,
            int quantidade, double valor, boolean pago) {
        super(codigo_produto, valor);
        this.codigoCliente = codigoCliente;
        this.data = data;
        this.sequencia = sequencia;
        this.quantidade = quantidade;
        this.pago = pago;
    }
    

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    @Override
    public String toString() {
        return super.toString()+"VoItem{" + "codigoCliente=" + codigoCliente + ", data=" + data +
                ", sequencia=" + sequencia + ", quantidade=" + quantidade + ", pago=" +
                pago + '}';
    }    
}
