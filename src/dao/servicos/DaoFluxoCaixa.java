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
import model.vo.cadastros.VoProduto;

/**
 *
 * @author dlnotari
 */
public class DaoFluxoCaixa {
    // atributos
    private BoConexao conexao;    
    
    // construtor
    public DaoFluxoCaixa(BoConexao conexao) {
        this.conexao = conexao;
    }
    

    /**
     * método para buscar o total a ser pago ou não da sauna
     * @return valor pago ou não pago
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public double getValorSauna(String data, boolean pago) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select sum(c.quantidade * c.valor) as total"
                + " from consumos c inner join consumosSauna cl on c.codigo_cliente = cl.codigo_cliente "
                + " and c.data = cl.data and c.sequencia = cl.sequencia"
                + " where c.data = ? "
                + " and   c.pago = ? "
                + " and   c.codigo_produto = ?";                
        
        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, Integer.parseInt(data));
        ps.setBoolean(2, pago);
        ps.setInt(3, VoProduto.CodigoSauna);        

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // pega resultado
        double total = 0;
        while (rs.next()) {
            total = rs.getDouble(1);
        }

        // return, não existe o código
        return total;
    }    

    /**
     * método para buscar o total a ser pago ou não do squash
     * @return valor pago ou não pago
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public double getValorSquash(String data, boolean pago) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select sum(c.quantidade * c.valor) as total"
                + " from consumos c "
                + " where c.data = ? "
                + " and   c.pago = ? "
                + " and   c.codigo_produto in (?, ?, ?)";
        
        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, Integer.parseInt(data));
        ps.setBoolean(2, pago);
        ps.setInt(3, VoProduto.CodigoMensalidade);
        ps.setInt(4, VoProduto.MultaMensalidade1);
        ps.setInt(5, VoProduto.MultaMensalidade2);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // pega resultado
        double total = 0;
        while (rs.next()) {
            total = rs.getDouble(1);
        }

        // return, não existe o código
        return total;
    }    
    
    /**
     * método para buscar o total a ser pago ou não do consumo de produtos
     * @return valor pago ou não pago
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public double getValorConsumo(String data, boolean pago) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select sum(c.quantidade * c.valor) as total"
                + " from consumos c "
                + " where c.data = ? "
                + " and   c.pago = ? "
                + " and   c.codigo_produto > 1";
        
        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, Integer.parseInt(data));
        ps.setBoolean(2, pago);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // pega resultado
        double total = 0;
        while (rs.next()) {
            total = rs.getDouble(1);
        }

        // return, não existe o código
        return total;
    }    
    
    // getters and setters
    public BoConexao getConexao() {
        return conexao;
    }
    
}
