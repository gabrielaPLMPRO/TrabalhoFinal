/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.servicos;

import dao.bd.E_BD;
import dao.servicos.DaoSquash;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.bo.bd.BoConexao;

/**
 *
 * @author Daniel
 */
public class BoSquash {
    // atributos
    private DaoSquash dao;

    // construtor
    public BoSquash(BoConexao boConexao) {
        this.dao = new DaoSquash(boConexao);
    }

    /**
     * recupera os dados de reserva de uma quadra de squash
     * @param dia
     * @param quadra
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    public ResultSet obterLista(int dia, String horario, int quadra) throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().obterLista(dia, horario, quadra);
    }    
    
    /**
     * getters and setters
     * @return 
     */
    public DaoSquash getDao() {
        return dao;
    }
}
