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
import model.vo.cadastros.VoCliente;

/**
 *
 * @author dlnotari
 */
public class DaoCliente implements I_DAO {
    // atributos
    private VoCliente vo;
    private BoConexao conexao;

    // construtor
    public DaoCliente(VoCliente vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
    }

    /**
     * 
     */       
    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException  {
        // testa se existe
        String sql;
        if (this.existeCodigo()) {
            // regravar
            sql = "update clientes "
                    + "set cpf             = ?, "
                    + "    nome            = ?, "
                    + "    dataAniversario = ?, "
                    + "    telefone        = ? "
                    + "where codigo        = ?";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // atribui valores
            ps.setInt(5, vo.getCodigo());
            ps.setString(1, vo.getCpf());
            ps.setString(2, vo.getNome());
            ps.setString(3, vo.getDataAniversario());
            ps.setString(4, vo.getTelefone());            

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        } else {
            // gravar
            sql = "insert into clientes (codigo, cpf, nome, dataAniversario, telefone )"
                    + " values (?, ?, ?, ?, ?)";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // testa se é para gerar o código
            if (gerarCodigo){
                vo.setCodigo(this.proximoCodigoLivre());
            }
            
            // atribui valores
            ps.setInt(1, vo.getCodigo());
            ps.setString(2, vo.getCpf());
            ps.setString(3, vo.getNome());
            ps.setString(4, vo.getDataAniversario());
            ps.setString(5, vo.getTelefone());

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        }

        // return, tudo certo ao salvar
        return true;
    }

    /**
     * 
     */       
    @Override
    public boolean excluir() throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql;
        if (this.existeCodigo()) {
            // regravar
            sql = "delete from clientes where codigo = ?";

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
     */       
    @Override
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException  {
        // consultar o código
        String sql = "select codigo, cpf, nome, dataAniversario, telefone"
                + " from clientes where codigo > 0 order by nome";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // return, não existe o código
        return rs;
    }

    /**
     * usado para backup
     */       
    public ResultSet obterLista(boolean tudo) throws SQLException, E_BD, ClassNotFoundException  {
        // consultar o código
        String sql = "select codigo, cpf, nome, dataAniversario, telefone"
                + " from clientes order by codigo";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // return, não existe o código
        return rs;
    }
    
    /**
     * 
     */       
    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from clientes where codigo = " + this.getVo().getCodigo();

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            this.vo = new VoCliente(rs.getInt("codigo"), rs.getString("cpf"),
                    rs.getString("nome"), rs.getString("dataAniversario"),
                    rs.getString("telefone"));
            return true;
        } 

        // return, não existe o código
        return false;
    }
    
    /**
     * 
     */       
    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from clientes where codigo = " + this.getVo().getCodigo();
        
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
     * verifica se um cliente é usado como chave estrangeira nas tabelas
     * consumos, consumosSauna e pagamentos
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código na tabela consumos
        String sql = "select * from consumos where codigo_cliente = " + this.getVo().getCodigo();
        
        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            return true;
        }
        
        // consultar o código na tabela consumosSauna
        sql = "select * from consumosSauna where codigo_cliente = " + this.getVo().getCodigo();
        
        // executar sql
        rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            return true;
        }
        
        // consultar o código na tabela pagamentos
        sql = "select * from pagamentos where codigo_cliente = " + this.getVo().getCodigo();
        
        // executar sql
        rs = this.getConexao().getBd().consulta(sql);

        // testa resultado
        while (rs.next()) {
            return true;
        }

        // return, não existe o código
        return false;
    }    

    /**
     * 
     */       
    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select max(codigo) from clientes";

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
     * getters and setters
     */       
    public VoCliente getVo() {
        return vo;
    }

    public void setVo(VoCliente vo) {
        this.vo = vo;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public void setConexao(BoConexao conexao) {
        this.conexao = conexao;
    }
}
