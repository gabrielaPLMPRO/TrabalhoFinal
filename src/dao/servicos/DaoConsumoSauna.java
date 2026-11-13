/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.servicos;

import dao.I_DAO;
import dao.bd.E_BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.bo.bd.BoConexao;
import model.vo.servicos.VoConsumoSauna;

/**
 *
 * @author dlnotari
 */
public class DaoConsumoSauna implements I_DAO {
    // atributos

    private BoConexao conexao;
    private VoConsumoSauna vo;

    // construtor
    public DaoConsumoSauna(BoConexao conexao) {
        this.conexao = conexao;
        this.vo = new VoConsumoSauna();
    }

    /**
     * codigo_cliente, data, sequencia, codigo_produto, quantidade, valor, pago"
     */
    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        String sql;
        // gravar
        sql = "insert into consumosSauna (codigo_cliente, data, sequencia, "
                + "nome) values (?, ?, ?, ?)";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, this.getVo().getCodigoCliente());
        ps.setInt(2, Integer.parseInt(this.getVo().getData()));
        ps.setInt(3, this.getVo().getSequencia());
        ps.setString(4, this.getVo().getNome());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps);

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
        // consultar números da sequencia da tabela consumosSauna
        String sql = "select codigo_cliente, data, sequencia "
                + " from consumosSauna"
                + " where nome = ?";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setString(1, this.getVo().getNome());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // excluir da tabela de consumosSauna
        String sql1 = "delete from consumosSauna where nome = ?";

        // obtem objeto
        PreparedStatement ps1 = this.getConexao().getBd().getStatement(sql1);

        // atribui valores
        ps1.setString(1, this.getVo().getNome());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps1);
        
        // excluir da tabela de consumos
        while (rs.next()) {
            // excluir
            sql1 = "delete from consumos "
                    + " where codigo_cliente = ?"
                    + " and   data           = ?"
                    + " and   sequencia      = ?";

            // obtem objeto
            ps1 = this.getConexao().getBd().getStatement(sql1);

            // atribui valores
            ps1.setInt(1, rs.getInt("codigo_cliente"));
            ps1.setInt(2, rs.getInt("data"));
            ps1.setInt(3, rs.getInt("sequencia"));

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps1);
        }

        // return 
        return true;
    }

    /***
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    @Override
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select c.codigo_cliente, c.data, c.sequencia, c.codigo_produto,"
                + " c.quantidade, c.valor, cl.nome, p.descricao  "
                + " from consumos c inner join consumosSauna cl on c.codigo_cliente = cl.codigo_cliente "
                + " and c.data = cl.data and c.sequencia = cl.sequencia"
                + " inner join produtos p on c.codigo_produto = p.codigo"
                + " where nome = ? "
                + " and   c.data = ? "
                + " order by c.codigo_cliente, c.data, c.sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setString(1, this.getVo().getNome());
        ps.setInt(2, Integer.parseInt(this.getVo().getData()));

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /***
     * busca a lista de clientes da sauna por data sem consumo
     * @param data
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    public ResultSet obterLista(boolean data) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select distinct cs.nome, cs.codigo_cliente"
                + " from consumosSauna cs"
                + " where cs.data = ? "
                + " order by cs.nome, cs.codigo_cliente";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
         ps.setInt(1, Integer.parseInt(this.getVo().getData()));

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }    
    
    /***
     * busca a lista de consumos de clientes da sauna por data
     * @param data
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    public ResultSet obterListaConsumo(String nome, String data) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select cs.codigo_cliente, cs.sequencia, c.codigo_produto,"
                + " c.quantidade, c.valor, p.descricao, c.pago, p.unidade "
                + " from consumos c inner join consumosSauna cs on c.codigo_cliente = cs.codigo_cliente "
                + " and c.data = cs.data and c.sequencia = cs.sequencia"
                + " inner join produtos p on c.codigo_produto = p.codigo"
                + " where c.data = ? "
                + " and   cs.nome = ?"
                + " order by cs.codigo_cliente, cs.sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
         ps.setInt(1, Integer.parseInt(data));
         ps.setString(2, nome);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }    
    
    
    /***
     * usado para backup
     * @param data
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException 
     */
    public ResultSet obterLista(int tudo) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select *"
                + " from consumosSauna "
                + " order by codigo_cliente, sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }     
    
    /**
     * *
     * método para buscar o total a ser pago
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public double getValorASerPago() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select sum(c.quantidade * c.valor) as total"
                + " from consumos c inner join consumosSauna cl on c.codigo_cliente = cl.codigo_cliente "
                + " and c.data = cl.data and c.sequencia = cl.sequencia"
                + " where nome = ? "
                + " and   c.data = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setString(1, this.getVo().getNome());
        ps.setInt(2, Integer.parseInt(this.getVo().getData()));

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
     * altera o campo pago da tabela consumo para true
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public void alterarPagoConsumosClienteSauna(double diferenca) throws SQLException, E_BD, ClassNotFoundException {
        // recuperar consumos do cliente da tabela temporária
        String sql = "select codigo_cliente, data, sequencia "
                + " from consumosSauna"
                + " where nome = ?"
                + " and   data = ?";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setString(1, this.getVo().getNome());
        ps.setInt(2, Integer.parseInt(this.getVo().getData()));

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // percorre a lista para atualizar valores para pago
        int aux = 0;
        while (rs.next()) {
            // regravar
            String sql1 = "update consumos "
                    + " set pago             = ?"
                    + " where codigo_cliente = ?"
                    + " and   data           = ?"
                    + " and   sequencia      = ?";

            // obtem objeto
            PreparedStatement ps1 = this.getConexao().getBd().getStatement(sql1);

            // atribui valores
            ps1.setBoolean(1, true);
            ps1.setInt(2, rs.getInt("codigo_cliente"));
            ps1.setInt(3, rs.getInt("data"));
            ps1.setInt(4, rs.getInt("sequencia"));

            // salva o código da sequencia para ser usado na atualização do saldo
            if (aux == 0) {
                aux = rs.getInt("sequencia");
            }

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps1);
        }

        // atualiza diferenca
        if (diferenca != 0) {
            // regravar
            String sql2 = "update consumos "
                    + " set valor            = valor + ?"
                    + " where codigo_cliente = ?"
                    + " and   data           = ?"
                    + " and   sequencia      = ?";

            // obtem objeto
            PreparedStatement ps2 = this.getConexao().getBd().getStatement(sql2);

            // atribui valores
            ps2.setDouble(1, diferenca);
            ps2.setInt(2, this.getVo().getCodigoCliente());
            ps2.setInt(3, Integer.parseInt(this.getVo().getData()));
            ps2.setInt(4, aux);

            // regrava no bd
            this.getConexao().getBd().executaSQL(ps2);
        }
    }

    /**
     * *
     * excluir um consumo
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public boolean excluirConsumo() throws SQLException, E_BD, ClassNotFoundException {
         // excluir da tabela de consumosSauna
        String sql1 = "delete from consumosSauna"
                + " where codigo_cliente = ?"
                + " and   data           = ?"
                + " and   sequencia      = ?";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql1);

        // atribui valores
        ps.setInt(1, this.getVo().getCodigoCliente());
        ps.setInt(2, Integer.parseInt(this.getVo().getData()));
        ps.setInt(3, this.getVo().getSequencia());

        // excluir no bd
        this.getConexao().getBd().executaSQL(ps);

        // excluir da tabela de consumos
        String sql = "delete from consumos "
                + " where codigo_cliente = ?"
                + " and   data           = ?"
                + " and   sequencia      = ?";

        // obtem objeto
         ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, this.getVo().getCodigoCliente());
        ps.setInt(2, Integer.parseInt(this.getVo().getData()));
        ps.setInt(3, this.getVo().getSequencia());

        // exclui no bd
        this.getConexao().getBd().executaSQL(ps);

        // return 
        return true;
    }

    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        throw new E_BD("Método existeCodigo não implementado na classe DaoConsumoSauna");
    }

    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        throw new E_BD("Método consultarCodigo não implementado na classe DaoConsumoSauna");
    }

    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        throw new E_BD("Método proximoCodigoLivre não implementado na classe DaoConsumoSauna");
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public VoConsumoSauna getVo() {
        return vo;
    }

    public void setVo(VoConsumoSauna vo) {
        this.vo = vo;
    }

    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
