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
public class VoValores implements I_VO{
    private int    codigo;
    private double valorSauna;
    private double valorAulaSquash;
    private double valorSquashMensal1;
    private double valorSquashMensal2;
    private double valorSquashMensal3;
    private int    vencimento1;
    private int    vencimento2;
    private int    vencimento3;

    public VoValores(int codigo, double valorSauna, double valorAulaSquash,
            double valorSquashMensal1, double valorSquashMensal2, 
            double valorSquashMensal3, int vencimento1, int vencimento2, int vencimento3) {
        this.codigo = codigo;
        this.valorSauna = valorSauna;
        this.valorAulaSquash = valorAulaSquash;
        this.valorSquashMensal1 = valorSquashMensal1;
        this.valorSquashMensal2 = valorSquashMensal2;
        this.valorSquashMensal3 = valorSquashMensal3;
        this.vencimento1 = vencimento1;
        this.vencimento2 = vencimento2;
        this.vencimento3 = vencimento3;
    }

    public VoValores() {
        this.codigo = 1;
        this.valorSauna = 0;
        this.valorAulaSquash = 0;
        this.valorSquashMensal1 = 0;
        this.valorSquashMensal2 = 0;
        this.valorSquashMensal3 = 0;
        this.vencimento1 = 0;
        this.vencimento2 = 0;
        this.vencimento3 = 0;
    }

    public double getValorSauna() {
        return valorSauna;
    }

    public void setValorSauna(double valorSauna) {
        this.valorSauna = valorSauna;
    }

    public double getValorSquashMensal1() {
        return valorSquashMensal1;
    }

    public void setValorSquashMensal1(double valorSquashMensal1) {
        this.valorSquashMensal1 = valorSquashMensal1;
    }

    public double getValorSquashMensal2() {
        return valorSquashMensal2;
    }

    public void setValorSquashMensal2(double valorSquashMensal2) {
        this.valorSquashMensal2 = valorSquashMensal2;
    }

    public double getValorSquashMensal3() {
        return valorSquashMensal3;
    }

    public void setValorSquashMensal3(double valorSquashMensal3) {
        this.valorSquashMensal3 = valorSquashMensal3;
    }

    public int getVencimento1() {
        return vencimento1;
    }

    public void setVencimento1(int vencimento1) {
        this.vencimento1 = vencimento1;
    }

    public int getVencimento2() {
        return vencimento2;
    }

    public void setVencimento2(int vencimento2) {
        this.vencimento2 = vencimento2;
    }

    public int getVencimento3() {
        return vencimento3;
    }

    public void setVencimento3(int vencimento3) {
        this.vencimento3 = vencimento3;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }   

    public double getValorAulaSquash() {
        return valorAulaSquash;
    }

    public void setValorAulaSquash(double valorAulaSquash) {
        this.valorAulaSquash = valorAulaSquash;
    }

    public String toString() {
        return "VoValores{" + "codigo=" + codigo + ", valorSauna=" + valorSauna + ", valorAulaSquash=" + valorAulaSquash + ", valorSquashMensal1=" + valorSquashMensal1 + ", valorSquashMensal2=" + valorSquashMensal2 + ", valorSquashMensal3=" + valorSquashMensal3 + ", vencimento1=" + vencimento1 + ", vencimento2=" + vencimento2 + ", vencimento3=" + vencimento3 + '}';
    }
}
