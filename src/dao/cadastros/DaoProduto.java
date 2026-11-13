/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.cadastros;

import dao.I_DAO;
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
public class DaoProduto implements I_DAO {
    // atributos
    private VoProduto vo;
    private BoConexao conexao;

    // construtor
    public DaoProduto(VoProduto vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
    }

    /***
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql;
        if (this.existeCodigo()) {
            // regravar
            sql = "update produtos "
                    + "set descricao = ?, "
                    + "    preco     = ?, "
                    + "    unidade   = ? "
                    + "where codigo  = ?";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // atribui valores
            ps.setInt(4, vo.getCodigo());
            ps.setString(1, vo.getDescricao());
            ps.setDouble(2, vo.getPreco());
            ps.setString(3, vo.getUnidade());

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        } else {
            // gravar
            sql = "insert into produtos (codigo, descricao, preco, unidade)"
                    + " values (?, ?, ?, ?)";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // testa se é para gerar o código
            if (gerarCodigo){
                vo.setCodigo(this.proximoCodigoLivre());
            }

            // atribui valores            
            ps.setInt(1, vo.getCodigo());
            ps.setString(2, vo.getDescricao());
            ps.setDouble(3, vo.getPreco());
            ps.setString(4, vo.getUnidade());

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        }

        // return, tudo certo ao salvar
        return true;
    }

    /**
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public boolean excluir() throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql;
        if (this.existeCodigo()) {
            // regravar
            sql = "delete from produtos where codigo = ?";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // atribui valores
            ps.setInt(1, vo.getCodigo());

            // exclui no bd
            this.getConexao().getBd().executaSQL(ps);
        }

        // return, tudo certo ao excluir
        return true;
    }

    /**
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from produtos where codigo > 1 order by descricao";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // return, não existe o código
        return rs;
    }
    
    /**
     * usado para fazer backup
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    public ResultSet obterLista(boolean tudo) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from produtos order by codigo";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // return, não existe o código
        return rs;
    }    

    /***
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from produtos where codigo = " + this.getVo().getCodigo();

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            this.vo = new VoProduto(rs.getInt("codigo"), rs.getString("descricao"),
                    rs.getDouble("preco"), rs.getString("unidade"));
            return true;
        }

        // return, não existe o código
        return false;
    }    
    
    /***
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from produtos where codigo = " + this.getVo().getCodigo();
        
        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            return true;
        }

        // return, não existe o código
        return false;
    }
    
    /**
     * verifica se existe chave estrangeira na tabela consumos 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código na tabela consumos
        String sql = "select * from consumos where codigo_produto = " + this.getVo().getCodigo();
        
        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            return true;
        }

        // return, não existe o código
        return false;
    }    

    /***
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select max(codigo) from produtos";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
                int codigo = rs.getInt(1) + 1;
                return codigo;
         }        
        
        // return, não existe o código
        return 1;
    }
    
    /**
     * GETTERS & SETTERS
     */
    public VoProduto getVo() {
        return vo;
    }

    public void setVo(VoProduto vo) {
        this.vo = vo;
    }

    public BoConexao getConexao() {
        return conexao;
    }
}
