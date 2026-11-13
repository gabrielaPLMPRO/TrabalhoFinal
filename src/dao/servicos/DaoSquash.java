/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.servicos;

import dao.bd.E_BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.bo.bd.BoConexao;

/**
 *
 * @author Daniel
 */
public class DaoSquash {
    // atributos
    private BoConexao conexao;
    
    // construtor
    public DaoSquash(BoConexao conexao) {
        this.conexao = conexao;
    }

    /**
     * Realiza uma consulta para pegar os horários e clientes
     * de uma reserva para um dia especifico e uma quadra especifica
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    public ResultSet obterLista(int dia, String horario, int quadra) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql =  "select c.nome, h.jogo "
                     +" from horariosSquash h inner join clientes c "
                     +" 	on h.codigo_cliente = c.codigo "
                     +" where dia     = ? "
                     +" and   quadra  = ? "
                     +" and   horario = ? "
                     +" group by nome, jogo "
                     +" order by nome, jogo ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, dia);
        ps.setInt(2, quadra);
        ps.setString(3, horario);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }    
    
    public BoConexao getConexao() {
        return conexao;
    }
}
