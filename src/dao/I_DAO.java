/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dlnotari
 */
public interface I_DAO {
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException;
    public boolean excluir() throws SQLException, E_BD, ClassNotFoundException;
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException;
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException;    
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException;    
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException;
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException;
}
