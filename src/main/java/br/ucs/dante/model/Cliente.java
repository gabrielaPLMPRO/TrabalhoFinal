
package br.ucs.dante.model;

import java.time.LocalDate;

public class Cliente {

    private Integer codigo;
    private String nome;
    private String cpf;
    private LocalDate dataAniversario;
    private String telefone;

    public Cliente(
            Integer codigo,
            String nome,
            String cpf,
            LocalDate dataAniversario,
            String telefone) {

        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
        this.dataAniversario = dataAniversario;
        this.telefone = telefone;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataAniversario() {
        return dataAniversario;
    }

    public void setDataAniversario(LocalDate dataAniversario) {
        this.dataAniversario = dataAniversario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
