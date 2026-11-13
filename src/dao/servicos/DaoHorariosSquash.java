/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.servicos;

import dao.bd.E_BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.bo.bd.BoConexao;
import model.vo.cadastros.VoCliente;
import model.vo.servicos.VoHorarioSquash;

/**
 *
 * @author Daniel
 */
public class DaoHorariosSquash implements dao.I_DAO {
    // atributos

    private VoHorarioSquash vo;
    private BoConexao conexao;

    // construtor
    public DaoHorariosSquash(VoHorarioSquash vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
    }

    /**
     * *
     *
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean salvar(boolean gerarCodigo) throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql = "insert into horariosSquash (dia, horario, quadra, "
                + " codigo_cliente, jogo)"
                + " values (?, ?, ?, ?, ?)";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getDia());
        ps.setString(2, vo.getHorario());
        ps.setInt(3, vo.getQuadra());
        ps.setInt(4, vo.getCodigo_cliente());
        ps.setBoolean(5, vo.isJogo());

        // regrava no bd
        this.getConexao().getBd().executaSQL(ps);

        // return, tudo certo ao salvar
        return true;
    }

    /**
     * exclui todos os clientes de uma reserva
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean excluir() throws SQLException, E_BD, ClassNotFoundException {
        // testa se existe
        String sql = "delete from horariosSquash"
                + " where dia            = ? "
                + " and   horario        = ? "
                + " and   quadra         = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getDia());
        ps.setString(2, vo.getHorario());
        ps.setInt(3, vo.getQuadra());

        // exclui no bd
        this.getConexao().getBd().executaSQL(ps);

        // return, tudo certo ao excluir
        return true;
    }

    /**
     * *
     *
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public ResultSet obterLista() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select h.dia, h.horario, h.quadra, c.nome, h.codigo_cliente, h.jogo"
                + " from horariosSquash h inner join clientes c on h.codigo_cliente = c.codigo ";

        // verifica se o dia foi informado
        if (this.getVo().getDia() != 0) {
            // adiciona clausula where
            sql += " where dia = ? ";
        }

        // adiciona o agrupamento
        sql += " order by h.dia, h.horario, h.quadra, c.nome";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // verifica se o dia foi informado
        if (this.getVo().getDia() != 0) {
            // atribui valores
            ps.setInt(1, vo.getDia());
        }
        
        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }

    /**
     * usado para backup
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet obterLista(boolean tudo) throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * "
                + " from horariosSquash "
                + " order by dia, horario, quadra";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return, não existe o código
        return rs;
    }
    
    /**
     * *
     *
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet obterListaReserva() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select h.dia, h.horario, h.quadra, c.nome, h.codigo_cliente, h.jogo"
                + " from horariosSquash h inner join clientes c on c.codigo_cliente = h.codigo "
                + " where dia            = ? "
                + " and   horario        = ? "
                + " and   quadra         = ? "
                + " order by h.dia, h.horario, h.quadra, c.nome";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // atribui valores
        ps.setInt(1, vo.getDia());
        ps.setString(2, vo.getHorario());
        ps.setInt(3, vo.getQuadra());

        // return, não existe o código
        return rs;
    }
    
    /**
     * obtem uma lista de reserva para um cliente apenas,
     * dados utilizados na tela de consulta da ficha do cliente
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet obterListaReservaCliente() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select h.dia, h.horario, h.quadra, h.jogo"
                + " from horariosSquash h"
                + " where codigo_cliente = ? "
                + " order by h.dia, h.horario, h.quadra";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);
        
        // atribui valores        
        ps.setInt(1, vo.getCodigo_cliente());        

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // atribui valores
        ps.setInt(1, vo.getCodigo_cliente());

        // return, não existe o código
        return rs;
    }

    /**
     * verfica se existe uma reserva, independente do número de clientes
     *
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean existeCodigo() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select * from horariosSquash "
                + " where dia            = ? "
                + " and   horario        = ? "
                + " and   quadra         = ? ";

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getDia());
        ps.setString(2, vo.getHorario());
        ps.setInt(3, vo.getQuadra());

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
     * *
     *
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean consultarCodigo() throws SQLException, E_BD, ClassNotFoundException {
        String sql = "select h.dia, h.horario, h.quadra, h.codigo_cliente, c.nome, h.jogo"
                + " from horariosSquash h inner join clientes c"
                + " on h.codigo_cliente = c.codigo "
                + " where dia            = ? "
                + " and   horario        = ? "
                + " and   quadra         = ? "
                + " order by h.dia, h.horario, h.quadra, c.nome" ;

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // atribui valores
        ps.setInt(1, vo.getDia());
        ps.setString(2, vo.getHorario());
        ps.setInt(3, vo.getQuadra());

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);
        
        // create vo
        this.getVo().setClientes(new ArrayList<VoCliente>());

        // testa resultado
        while (rs.next()) {
            // set jogo
            this.getVo().setJogo(rs.getBoolean("jogo"));
            
            // cria o objeto de cliente
            VoCliente voAux = new VoCliente(rs.getInt("codigo_cliente"), 
                    rs.getString("nome"));
            
            // adiciona o cliente
            this.getVo().getClientes().add(voAux);
        } 

        // return, 
        return true;
    }

    /**
     * *
     *
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public int proximoCodigoLivre() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * *
     *
     * @return @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    @Override
    public boolean existeChaveEstrangeira() throws SQLException, E_BD, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VoHorarioSquash getVo() {
        return vo;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public void setVo(VoHorarioSquash vo) {
        this.vo = vo;
    }
}
