/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vo.cadastros;

import model.vo.I_VO;

public class VoCliente implements I_VO {
    // atributos
    private int codigo;
    private String cpf;
    private String nome;
    private String dataAniversario;
    private String telefone;
    
    // construtor
    public VoCliente(int codigo, String cpf, String nome, String dataAniversario, String telefone) {
        this.codigo = codigo;
        this.cpf = cpf;
        this.nome = nome;
        this.dataAniversario = dataAniversario;
        this.telefone = telefone;
    }

    // construtor    
    public VoCliente(int codigo) {
        this.codigo = codigo;
        this.cpf = "";
        this.nome = "";
        this.dataAniversario = "";
        this.telefone = "";
    }

    // construtor    
    public VoCliente(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.cpf = "";
        this.dataAniversario = "";
        this.telefone = "";
    }

    // construtor    
    public VoCliente(String nome) {
        this.codigo = 0;
        this.nome = nome;
        this.cpf = "";
        this.dataAniversario = "";
        this.telefone = "";
    }
    
    // construtor    
    public VoCliente() {
        this.codigo = 0;
        this.cpf = "";
        this.nome = "";
        this.dataAniversario = "";
        this.telefone = "";
    }
    
    // getters and setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataAniversario() {
        return dataAniversario;
    }

    public void setDataAniversario(String dataAniversario) {
        this.dataAniversario = dataAniversario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "VoCliente{" + "codigo=" + codigo + ", cpf=" + cpf + ", nome=" + nome + ", dataAniversario=" + dataAniversario + ", telefone=" + telefone + '}';
    }
    
}
