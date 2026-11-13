/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.vo.servicos;

import model.vo.cadastros.VoCliente;
import java.util.ArrayList;
import model.vo.I_VO;

/**
 *
 * @author dlnotari
 */
public class VoHorarioSquash implements I_VO {
    // atributos

    private ArrayList<VoCliente> clientes;
    private int codigo_cliente;
    private int dia;
    private int quadra;
    private String horario;
    private boolean jogo;

    // construtor
    public VoHorarioSquash() {
        this.codigo_cliente = 0;
        this.dia = 0;
        this.quadra = 0;
        this.horario = "";
        this.jogo = true;
        this.clientes = new ArrayList<>();
    }

    // construtor
    public VoHorarioSquash(int dia, int quadra, String horario, boolean jogo) {
        this.codigo_cliente = 0;
        this.dia = dia;
        this.quadra = quadra;
        this.horario = horario;
        this.jogo = jogo;
        this.clientes = new ArrayList<>();
    }

    // construtor
    public VoHorarioSquash(int dia, int quadra, String horario) {
        this.dia = dia;
        this.quadra = quadra;
        this.horario = horario;
        this.codigo_cliente = 0;
        this.clientes = new ArrayList<>();
        this.jogo = true;
    }

    // construtor utilizado para a tela de ficha do cliente
    public VoHorarioSquash(int codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
        this.dia = 0;
        this.quadra = 0;
        this.horario = "";
        this.jogo = true;
        this.clientes = new ArrayList<>();
    }
    
    // construtor para restaurar backup
    public VoHorarioSquash(int codigo_cliente, int dia, int quadra, String horario, boolean jogo) {
        this.codigo_cliente = codigo_cliente;
        this.dia = dia;
        this.quadra = quadra;
        this.horario = horario;
        this.jogo = jogo;
        this.clientes = new ArrayList<>();
    }
    
    // getters and setters
    public ArrayList<VoCliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<VoCliente> clientes) {
        this.clientes = clientes;
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

    public boolean isJogo() {
        return jogo;
    }

    public void setJogo(boolean jogo) {
        this.jogo = jogo;
    }

    public int getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(int codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    @Override
    public String toString() {
        return "VoHorarioSquash{" + "clientes=" + clientes + ", codigo_cliente=" + codigo_cliente + ", dia=" + dia + ", quadra=" + quadra + ", horario=" + horario + ", jogo=" + jogo + '}';
    }

    // monta string para a tela de ficha do cliente
    public String listaReservas() {
        // variável local
        StringBuilder builder = new StringBuilder();

        // adiciona se é aula ou jogo
        if (this.isJogo()) {
            builder.append("Reserva de jogo de Squash, ");
        } else {
            builder.append("Aula de Squash, ");
        }        
        
        // adiciona o dia
        builder.append(this.diaDaSemana(this.getDia()));

        // adiciona o horário
        builder.append(", as ").append(this.getHorario());

        // adiciona a quadra
        builder.append(" horas na quadra ").append(quadra).append(".");

        // retorna
        return builder.toString();
    }

    // devolve o dia da semana
    private String diaDaSemana(int dia) {
        switch (dia) {
            case 1:
                return "segunda-feira";
            case 2:
                return "terca-feira";
            case 3:
                return "quarta-feira";
            case 4:
                return "quinta-feira";
            case 5:
                return "sexta-feira";
            case 6:
                return "sabado";
        }
        return "domingo";
    }
}
