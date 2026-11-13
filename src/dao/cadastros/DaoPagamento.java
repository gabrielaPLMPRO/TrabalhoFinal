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
import model.bo.cadastros.BoConsumo;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoPagamento;

/**
 *
 * @author dlnotari
 */
public class DaoPagamento implements I_DAO {
    // atributos

    private BoConexao conexao;
    private VoPagamento vo;

    // construtor
    public DaoPagamento(BoConexao conexao, VoPagamento vo) {
        this.conexao = conexao;
        this.vo = vo;
    }

    /**
     * codigo_cliente, data, sequencia, valor, pagto_total somente salva, não
     * regrava
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        // gravar
        String sql = "insert into pagamentos (codigo_cliente, data, sequencia, "
                + "valor, pagto_total)"
                + " values (?, ?, ?, ?, ?)";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // testa se é para gerar o código
        if (gerarCodigo) {
            vo.setSequencia(this.proximoCodigoLivre());
        }

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setInt(2, vo.getData());
        ps.setInt(3, vo.getSequencia());
        ps.setDouble(4, vo.getValor());
        ps.setBoolean(5, vo.isPagarTudo());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps);

        // return, tudo certo ao salvar
        return true;
    }

    /**
     * *
     * não será permitido excluir um pagamento feito
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean excluir() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select c.codigo_cliente, cl.nome, c.data, "
                + " c.valor, c.pagto_total, c.sequencia "
                + "from pagamentos c inner join clientes cl on c.codigo_cliente = cl.codigo "
                + "where codigo_cliente = ? "
                + "and   data           = ? "
                + "order by data, sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setInt(2, vo.getData());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * usado para backup
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet obterLista(boolean tudo) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * "
                + "from pagamentos "
                + "order by codigo_cliente, sequencia";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * método não será implementado porque não há atualização
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * método não será implementado porque não há consulta simples
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select max(sequencia) from pagamentos "
                + "where codigo_cliente = ? "
                + "and   data           = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setInt(2, vo.getData());

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
     * calcula o total a ser pago por um cliente
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public double totalPagar() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select sum(valor * quantidade) from consumos "
                + "where codigo_cliente = ? "
                + "and   pago           = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getCodigoCliente());
        ps.setBoolean(2, false);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // testa resultado
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
    public boolean alterarPagoConsumosCliente(double desconto) throws SQLException, E_BD, ClassNotFoundException {
        // grava desconto
        if (desconto > 0) {
            // gera um registro com o desconto
            VoConsumo voAux = new VoConsumo(this.getVo().getCodigoCliente(),
                    String.valueOf(this.getVo().getData()),
                    0, 1, true, 0, "desconto",
                    desconto * -1, "un");

            // gera sequencia
            BoConsumo boConsumo = new BoConsumo(voAux, this.getConexao());
            int sequencia = boConsumo.proximoCodigoLivre();
            voAux.setSequencia(sequencia);

            // grava consumo
            this.criarConsumoCliente(voAux);
        }

        // regravar consumos para pagos de um cliente
        String sql = "update consumos "
                + "set pago             = ?  "
                + "where codigo_cliente = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setBoolean(1, true);
        ps.setInt(2, vo.getCodigoCliente());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps);

        // return, não existe o código
        return true;
    }

    /**
     * criar novo consumo quando há difeneça de pagamento
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public boolean criarConsumoCliente(VoConsumo aux) throws SQLException, E_BD, ClassNotFoundException {
        // gravar
        String sql = "insert into consumos (codigo_cliente, data, sequencia, "
                + "codigo_produto, quantidade, valor, pago)"
                + " values (?, ?, ?, ?, ?, ?, ?)";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, aux.getCodigoCliente());
        ps.setInt(2, Integer.parseInt(aux.getData()));
        ps.setInt(3, aux.getSequencia());
        ps.setInt(4, aux.getCodigo());
        ps.setInt(5, aux.getQuantidade());
        ps.setDouble(6, aux.getPreco());
        ps.setBoolean(7, aux.isPago());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps);

        // return 
        return true;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public VoPagamento getVo() {
        return vo;
    }

    public void setVo(VoPagamento vo) {
        this.vo = vo;
    }

    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
