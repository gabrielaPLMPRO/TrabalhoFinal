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
import model.vo.servicos.VoHorarioSquash;
import model.vo.servicos.VoMensalidade;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class DaoMensalidade implements I_DAO{
    // atributos

    private VoHorarioSquash vo;
    private BoConexao conexao;
    private VoMensalidade voMensalidade;

    // construtor
    public DaoMensalidade(VoHorarioSquash vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
        this.voMensalidade = new VoMensalidade();
    }

    /**
     * *
     * obtem o número total de clientes para uma reserva (dia, horario e quadra)
     *
     * @return
     */
    public ResultSet obterListaReservasComNumeroClientes() throws SQLException, E_BD, ClassNotFoundException {
        // busca todas as reservas com o número de clientes
        String sql = "select dia, horario, quadra, count(codigo_cliente), jogo"
                + " from horariossquash"
                + " group by dia, horario, quadra, jogo"
                + " order by dia, horario, quadra  ";
        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * *
     * obtem o código dos clientes de uma reserva
     *
     * @return
     */
    public ResultSet obterClientesReserva() throws SQLException, E_BD, ClassNotFoundException {
        // busca os clientes de cada reserva de acordo com a resposta acima
        String sql = "select codigo_cliente "
                + " from horariossquash "
                + " where dia = ? "
                + " and   horario = ? "
                + " and   quadra = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);


        // atribui valores
        ps.setInt(1, vo.getDia());
        ps.setString(2, vo.getHorario());
        ps.setInt(3, vo.getQuadra());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * *
     * verifica se há mensalidade de um cliente foi gerada
     */
    public boolean verificarMensalidadeGerada() throws SQLException, E_BD, ClassNotFoundException {
        // busca os clientes de cada reserva de acordo com a resposta acima
        String sql = "select *"
                + " from mensalidades "
                + " where codigo_cliente = ?"
                + " and   dia            = ?"
                + " and   horario        = ?"
                + " and   quadra         = ?"
                + " and   mes            = ?"
                + " and   ano            = ?";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, voMensalidade.getCodigo_cliente());
        ps.setInt(2, voMensalidade.getDia());
        ps.setString(3, voMensalidade.getHorario());
        ps.setInt(4, voMensalidade.getQuadra());
        ps.setInt(5, voMensalidade.getMes());
        ps.setInt(6,voMensalidade.getAno());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // verfica se existe
        while (rs.next()) {
            if ((voMensalidade.getCodigo_cliente() == rs.getInt("codigo_cliente"))
            &&  (voMensalidade.getDia() == rs.getInt("dia") )
            &&  (voMensalidade.getHorario().equals(rs.getString("horario"))
            &&  (voMensalidade.getQuadra() == rs.getInt("quadra"))
            &&  (voMensalidade.getMes() == rs.getInt("mes"))
            &&  (voMensalidade.getAno() == rs.getInt("ano"))))
            return true;
        }

        // return
        return false;
    }

    // verificar se há mensalidade de um mês e ano foi paga
    public boolean verificarMensalidadePaga(int codigoCliente, int mes, int ano,
            int codigoProdutoMensalidade) throws SQLException, E_BD, ClassNotFoundException {
        // busca os clientes de cada reserva de acordo com a resposta acima
        String sql = "select data"
                + " from consumos "
                + " where codigo_cliente = ?"
                + " and   codigo_produto = ?"
                + " and   pago = ?"
                + " order by data desc";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, codigoCliente);
        ps.setInt(2, codigoProdutoMensalidade);
        ps.setBoolean(3, true);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // verfica se existe
        while (rs.next()) {
            // pega valores
            String data = rs.getString("data");

            // pega o mes
            int mesPgto = ValidarStrings.getMesData(data);

            // pega o ano
            int anoPgto = ValidarStrings.getAnoData(data);

            // compara mes e ano
            if ((mes == mesPgto) && (ano == anoPgto)) {
                return true;
            }
        }

        // return
        return false;
    }

    // verificar se as mensalidades de um mês já foram geradas
    public boolean verificarMensalidadadesMultasGeradas(int mes, int ano,
            int codigoProdutoMensalidade) throws SQLException, E_BD, ClassNotFoundException {
        // busca os clientes de cada reserva de acordo com a resposta acima
        String sql = "select data"
                + " from consumos "
                + " where codigo_produto = ?"
                + " order by data desc";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, codigoProdutoMensalidade);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // verfica se existe
        while (rs.next()) {
            // pega valores
            String data = rs.getString("data");

            // testa se a data volta com 7 digitos
            if (data.length() == 7) {
                data += "0" + data;
            }

            // pega o mes
            int mesPgto = ValidarStrings.getMesData(data);

            // pega o ano
            int anoPgto = ValidarStrings.getAnoData(data);

            // compara mes e ano
            if ((mes == mesPgto) && (ano == anoPgto)) {
                return true;
            }
        }

        // return
        return false;
    }

    // verificar se a multa 1 de um mês já foi gerada
    public boolean verificarMultasGeradas(String data, int codigoMulta) {
        // return
        return true;
    }

    public VoHorarioSquash getVo() {
        return vo;
    }

    public void setVo(VoHorarioSquash vo) {
        this.vo = vo;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql = "insert into mensalidades (dia, horario, quadra, "
                + " codigo_cliente, mes, ano)"
                + " values (?, ?, ?, ?, ?, ?)";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, voMensalidade.getDia());
        ps.setString(2, voMensalidade.getHorario());
        ps.setInt(3, voMensalidade.getQuadra());
        ps.setInt(4, voMensalidade.getCodigo_cliente());
        ps.setInt(5, voMensalidade.getMes());
        ps.setInt(6, voMensalidade.getAno());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps);

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
        String sql = "select * from mensalidades";

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(sql);

        // return, não existe o código
        return rs;
    }

    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VoMensalidade getVoMensalidade() {
        return voMensalidade;
    }

    public void setVoMensalidade(VoMensalidade voMensalidade) {
        this.voMensalidade = voMensalidade;
    }
}
