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
import model.vo.cadastros.VoConsumo;

/**
 *
 * @author dlnotari
 */
public class DaoConsumo implements I_DAO {
    // atributos

    private BoConexao conexao;
    private VoConsumo vo;

    // construtor
    public DaoConsumo(BoConexao conexao, VoConsumo vo) {
        this.conexao = conexao;
        this.vo = vo;
    }

    /**
     * codigo_cliente, data, sequencia, codigo_produto, quantidade, valor, pago"
     */
    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql;
        if (this.existeCodigo()) {
            // regravar
            sql = "update consumos "
                    + "set codigo_produto = ?, "
                    + "    quantidade     = ?, "
                    + "    valor          = ?, "
                    + "    pago           = ?  "
                    + "where codigo_cliente = ? "
                    + "and   data           = ? "
                    + "and   sequencia      = ?";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // atribui valores
            ps.setInt(1, vo.getCodigo());
            ps.setInt(2, vo.getQuantidade());
            ps.setDouble(3, vo.getPreco());
            ps.setBoolean(4, vo.isPago());
            ps.setInt(5, vo.getCodigoCliente());
            ps.setInt(6, Integer.parseInt(vo.getData()));
            ps.setInt(7, vo.getSequencia());

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps);
        } else {
            // gravar
            sql = "insert into consumos (codigo_cliente, data, sequencia, "
                    + "codigo_produto, quantidade, valor, pago)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // testa se é para gerar o código
            if (gerarCodigo){
                vo.setSequencia(this.proximoCodigoLivre());
            }            
            
            // atribui valores
            ps.setInt(1, vo.getCodigoCliente());
            ps.setInt(2, Integer.parseInt(vo.getData()));
            ps.setInt(3, vo.getSequencia());
            ps.setInt(4, vo.getCodigo());
            ps.setInt(5, vo.getQuantidade());
            ps.setDouble(6, vo.getPreco());
            ps.setBoolean(7, vo.isPago());

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
            sql = "delete from consumos where codigo_cliente = ? "
                    + "and   data           = ? "
                    + "and   sequencia      = ?";

            // obtem objeto
            PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

            // atribui valores
            ps.setInt(1, vo.getCodigoCliente());
            ps.setInt(2, Integer.parseInt(vo.getData()));
            ps.setInt(3, vo.getSequencia());

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
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select c.codigo_cliente, cl.nome,c.data, c.codigo_produto,"
                + " p.descricao, c.quantidade, c.valor, c.pago, c.sequencia "
                + "from consumos c inner join clientes cl on c.codigo_cliente = cl.codigo "
                + "inner join produtos p on c.codigo_produto = p.codigo "
                + "where codigo_cliente = ? "
                + "order by codigo_cliente, data, sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * usado para backup
     */
    public ResultSet obterLista(boolean tudo) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * "
                + "from consumos "
                + "order by codigo_cliente, data, sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }
    
    /**
     * obter lista para a tela de pagamento, tela de ficha do cliente e
     * tela de fluxo de caixa
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet obterLista(boolean data, boolean descendente) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select c.data, p.descricao, c.quantidade, c.valor, c.pago "
                + "from consumos c inner join produtos p on c.codigo_produto = p.codigo ";
        
        // verificar se o teste é por data ou por cliente
        if (!data){
            sql += "where codigo_cliente = ? ";
        }                
        else{
            sql += "where data = ? ";
        }   
        
        // acrescenta ordenação
        sql += "order by data";
        
        // em ordem descrescente
        if (descendente) {
            sql += " desc";                    
        }

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        if (!data){        
            ps.setInt(1, vo.getCodigoCliente());
        }
        else {        
            ps.setInt(1, Integer.parseInt(vo.getData()));
        }

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * 
     */
    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from consumos "
                + "where codigo_cliente = ? "
                + "and   data           = ? "
                + "and   sequencia      = ?";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setInt(2, Integer.parseInt(vo.getData()));
        ps.setInt(3, vo.getSequencia());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

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
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from consumos "
                + "where codigo_cliente = ? "
                + "and   data           = ? "
                + "and   sequencia      = ?";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setInt(2, Integer.parseInt(vo.getData()));
        ps.setInt(3, vo.getSequencia());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // testa resultado
        while (rs.next()) {
            this.getVo().setCodigo(rs.getInt("codigo_produto"));
            this.getVo().setQuantidade(rs.getInt("quantidade"));
            this.getVo().setPreco(rs.getDouble("valor"));
            this.getVo().setPago(rs.getBoolean("pago"));
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
        String sql = "select max(sequencia) from consumos "
                + "where codigo_cliente = ? "
                + "and   data           = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setInt(2, Integer.parseInt(vo.getData()));

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // testa resultado
        while (rs.next()) {
            // pega o conteúdo do código e incrementa                
            int codigo = rs.getInt(1) + 1;
            return codigo;
        }

        // return, não existe o código
        return 1;
    }

     /**
     * getters and setters
     */
   public BoConexao getConexao() {
        return conexao;
    }

    public VoConsumo getVo() {
        return vo;
    }

    public void setVo(VoConsumo vo) {
        this.vo = vo;
    }

    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
