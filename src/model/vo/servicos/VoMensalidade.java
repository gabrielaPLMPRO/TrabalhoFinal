/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vo.servicos;

import model.vo.I_VO;

/**
 *
 * @author Daniel
 */
public class VoMensalidade implements I_VO{
    private int codigo_cliente;
    private int dia;
    private int quadra;
    private String horario;
    private int mes;
    private int ano;

    public VoMensalidade() {
        this.codigo_cliente = 0;
        this.dia = 0;
        this.quadra = 0;
        this.horario = "";
        this.mes = 0;
        this.ano = 0;
    }

    public VoMensalidade(int codigo_cliente, int dia, int quadra, String horario, int mes, int ano) {
        this.codigo_cliente = codigo_cliente;
        this.dia = dia;
        this.quadra = quadra;
        this.horario = horario;
        this.mes = mes;
        this.ano = ano;
    }

    public int getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(int codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getQuadra() {
        return quadra;
    }

    public void setQuadra(int quadra) {
        this.quadra = quadra;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
    
}
