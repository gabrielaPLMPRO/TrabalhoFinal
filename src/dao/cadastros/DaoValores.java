/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.cadastros;

import dao.bd.E_BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.bo.bd.BoConexao;
import model.vo.cadastros.VoValores;

/**
 *
 * @author dlnotari
 */
public class DaoValores implements dao.I_DAO{
    // atritutos
    private VoValores vo;
    private BoConexao conexao;
    
    // construtor
    public DaoValores(VoValores vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
    }
    

    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql;
        if (this.existeCodigo()) {
            // regravar
            sql = "update valores "
                    + "set valorSauna         = ?, "
                    + "    vencimento1        = ?, "
                    + "    valorSquashMensal1 = ?, "
                    + "    vencimento2        = ?, "
                    + "    valorSquashMensal2 = ?, "
                    + "    vencimento3        = ?, "
                    + "    valorSquashMensal3 = ?, "
                    + "    valorAulaSquash    = ? "
                    + "where codigo           = ?";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql.trim());

            // atribui valores
            ps.setInt(9, vo.getCodigo());
            ps.setDouble(1, vo.getValorSauna());
            ps.setInt(2, vo.getVencimento1());
            ps.setDouble(3, vo.getValorSquashMensal1());
            ps.setInt(4, vo.getVencimento2());
            ps.setDouble(5, vo.getValorSquashMensal2());
            ps.setInt(6, vo.getVencimento3());
            ps.setDouble(7, vo.getValorSquashMensal3());
            ps.setDouble(8, vo.getValorAulaSquash());
            
            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        } else {
            // gravar
            sql = "insert into valores (codigo, valorSauna, vencimento1, valorSquashMensal1,"
                    + "vencimento2, valorSquashMensal2, vencimento3, valorSquashMensal3, "
                    + "valorAulaSquash ) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // atribui valores
            ps.setInt(1, vo.getCodigo());
            ps.setDouble(2, vo.getValorSauna());
            ps.setInt(3, vo.getVencimento1());
            ps.setDouble(4, vo.getValorSquashMensal1());
            ps.setInt(5, vo.getVencimento2());
            ps.setDouble(6, vo.getValorSquashMensal2());
            ps.setInt(7, vo.getVencimento3());
            ps.setDouble(8, vo.getValorSquashMensal3());
            ps.setDouble(9, vo.getValorAulaSquash());
            
            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        }

        // return, tudo certo ao salvar
        return true;
    }

    @Override
    public boolean excluir() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from valores";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // return, não existe o código
        return rs;
    }

    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from valores where codigo = " + this.getVo().getCodigo();
        
        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            return true;
        }

        // return, não existe o código
        return false;
    }

    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from valores where codigo = " + this.getVo().getCodigo();

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            this.vo = new VoValores(rs.getInt("codigo"), rs.getDouble("valorSauna"),
                    rs.getDouble("valorAulaSquash"),rs.getDouble("valorSquashMensal1"),
                    rs.getDouble("valorSquashMensal2"), rs.getDouble("valorSquashMensal3"),
                    rs.getInt("vencimento1"), rs.getInt("vencimento2"),
                    rs.getInt("vencimento3"));
            return true;
        }

        // return, não existe o código
        return false;
    }

    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VoValores getVo() {
        return vo;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public void setVo(VoValores vo) {
        this.vo = vo;
    }

    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
