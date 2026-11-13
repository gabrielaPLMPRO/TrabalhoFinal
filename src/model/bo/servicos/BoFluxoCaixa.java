/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.servicos;

import dao.bd.E_BD;
import dao.servicos.DaoFluxoCaixa;
import java.sql.SQLException;
import model.bo.bd.BoConexao;

/**
 *
 * @author dlnotari
 */
public class BoFluxoCaixa {    
    // atributos
    private DaoFluxoCaixa dao;

    // construtor
    public BoFluxoCaixa(BoConexao conexao) {
        this.dao = new DaoFluxoCaixa(conexao);
    }
    
    /**
     * calcula o total pago na sauna por data
     * @param data
     * @return
     */
    public double calcularTotalPagoSauna(String data) throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().getValorSauna(data, true);
    }

    /**
     * calcula o total não pago na sauna por data
     * @param data
     * @return
     */
    public double calcularTotalNaoPagoSauna(String data) throws SQLException, E_BD, ClassNotFoundException{
        return this.getDao().getValorSauna(data, false);
    }    
    
    /**
     * calcula o total pago no squash por data
     * @param data
     * @return
     */
    public double calcularTotalPagoSquash(String data) throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().getValorSquash(data, true);
    } 
    
    /**
     * calcula o total não pago no squash por data
     * @param data
     * @return
     */
    public double calcularTotalNaoPagoSquash(String data) throws SQLException, E_BD, ClassNotFoundException{
        return this.getDao().getValorSquash(data, false);
    }     

    /**
     * calcula o total pago do consumo de produtos por data
     * @param data
     * @return
     */
    public double calcularTotalPagoConsumo(String data)throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().getValorConsumo(data, true);
    }    
    
    /**
     * calcula o total não pago do consumo de produtos por data
     * @param data
     * @return
     */
    public double calcularTotalNaoPagoConsumo(String data)throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().getValorConsumo(data, false);
    }       
    
    // getters and setters
    private DaoFluxoCaixa getDao() {
        return dao;
    }
}
