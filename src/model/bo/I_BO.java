/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 *
 * @author dlnotari
 */
public interface I_BO extends Serializable{
    public boolean salvar();
    public boolean excluir();
    public boolean consultarCodigo(int codigo);
    public ResultSet obterLista();
    public boolean validar();
    public String getErro();   
    public int proximoCodigoLivre();
}
