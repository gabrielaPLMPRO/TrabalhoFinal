/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.servicos;

import dao.bd.E_BD;
import dao.servicos.DaoHorariosSquash;
import gui.controller.servicos.CoHorarioSquash;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoCliente;
import model.vo.cadastros.VoCliente;
import model.vo.servicos.VoHorarioSquash;

/**
 *
 * @author Daniel
 */
public class BoHorarioSquash implements model.bo.I_BO {
    // atributos

    private VoHorarioSquash vo;
    private BoConexao conexao;
    private String erro;
    private DaoHorariosSquash dao;
    private CoHorarioSquash co;

    // construtor
    public BoHorarioSquash(VoHorarioSquash vo, BoConexao conexao, CoHorarioSquash co) {
        this.vo = vo;
        this.conexao = conexao;
        this.erro = "";
        this.dao = new DaoHorariosSquash(vo, conexao);
        this.co = co;
    }

    /**
     * *
     * obter lista de clientes
     */
    public ArrayList<VoCliente> obterListaClientes() {
        // cria objeto
        BoCliente boCliente = new BoCliente(null, this.getConexao());

        // obter lista de clientes
        ResultSet rs = boCliente.obterLista();

        // declara lista
        ArrayList<VoCliente> clientes = null;
        
        try {
            // cria objeto vazio da lista de clientes
            clientes = new ArrayList<>();

            // copia para a lista de clientes
            while (rs.next()) {
                // cria vo
                VoCliente voAux = new VoCliente(rs.getInt("codigo"), rs.getString("nome"));

                // adiciona na coleção        
                clientes.add(voAux);
            }
        } catch (SQLException ex) {
            this.setErro("Não foi possível obter a lista de clientes!" + ex.toString());
            return null;
        }

        // return
        return clientes;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean salvar() {
        // valida os campos necessários
        if (!this.validar()) {
            return false;
        }
        
        try {
            // set vo
            this.getDao().setVo(this.getVo());

            // inicialmente vai excluir todas as informações de uma reserva, se 
            // existir
            if (this.getDao().existeCodigo()) {

                // pede se quer limpar os campos
                int resp = JOptionPane.showConfirmDialog(co.getGui(),
                        "Já existe uma reserva para este horário! "
                        + "Desejar substituir a reserva existente?",
                        "Cadastrar Horários Quadra de Squash", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.NO_OPTION) {
                    this.setErro("Inclusão cancelada pelo usuário!");
                    return false;
                }

                // exclui a reserva toda
                if (!this.getDao().excluir()) {
                    this.setErro("Houve um erro ao excluir as informações "
                            + "da reserva de squash no banco de dados");
                    return false;
                }
            }

            // percorre a lista e faz um salvamento para cada cliente
            for (VoCliente voAux : this.getVo().getClientes()) {
                // seta o código do cliente
                this.getVo().setCodigo_cliente(voAux.getCodigo());

                // set vo
                this.getDao().setVo(this.getVo());

                // salva cada um dos clientes da reserva
                if (!this.getDao().salvar(true)) {
                    this.setErro("Houve um erro ao salvar as informações da reserva de um cliente no banco de dados");
                    return false;
                }
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao salvar as informações da reserva de um cliente "
                    + "no banco de dados\n" + ex.toString());
            return false;
        }

        // return
        return true;
        
    }

    /**
     *
     * @return
     */
    @Override
    public boolean excluir() {
        try {
            // set vo
            this.getDao().setVo(this.getVo());

            // excluir
            if (!this.getDao().excluir()) {
                this.setErro("Houve um erro ao excluir uma reserva de um cliente do banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao excluir uma reserva de um cliente do banco de dados\n" + ex.getMessage());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     * *
     *
     * @param codigo
     * @return
     */
    @Override
    public boolean consultarCodigo(int codigo) {
        // consultar
        try {
            this.getDao().setVo(this.getVo());
            if (!this.getDao().consultarCodigo()) {
                this.setErro("Erro ao consultar uma reserva de um cliente do banco de dados!");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar a reserva de um cliente do banco de dados\n" + ex.getMessage());
            return false;
        }

        // setar vo
        this.setVo(this.getDao().getVo());

        // retorno
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public ResultSet obterLista() {
        // consultar
        ResultSet resultado;
        try {
            this.getDao().setVo(this.getVo());
            resultado = this.getDao().obterLista();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }

    /**
     * usado para backup
     *
     * @return
     */
    public ResultSet obterLista(boolean tudo) {
        // consultar
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista(tudo);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }

    /**
     * obtem uma lista de reserva para um cliente apenas, dados utilizados na
     * tela de consulta da ficha do cliente
     *
     * @return
     */
    public ArrayList<VoHorarioSquash> obterListaReservaCliente() {
        // cria objeto de retorno
        ArrayList<VoHorarioSquash> lista = new ArrayList<>();
        
        try {
            // set vo no dao
            this.getDao().setVo(this.getVo());

            // obtem lista
            ResultSet resultado = this.getDao().obterListaReservaCliente();

            // percorre o resultado
            while (resultado.next()) {
                // cria vo auxiliar
                VoHorarioSquash voAux = new VoHorarioSquash(resultado.getInt(1),
                        resultado.getInt(3), resultado.getString(2),
                        resultado.getBoolean(4));

                // adiciona resultado
                lista.add(voAux);
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return lista;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean validar() {
        // se for aula, somente um cliente pode fazer a reserva
        if (!this.getVo().isJogo()) {
            if (this.getVo().getClientes().size() > 1) {
                this.setErro("A reserva de uma quadra para aula deve ser feita"
                        + " para somente um cliente!");
                return false;
            }
        }

        // return
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public int proximoCodigoLivre() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public String getErro() {
        return this.erro;
    }

    // getters and setters
    public VoHorarioSquash getVo() {
        return vo;
    }
    
    public BoConexao getConexao() {
        return conexao;
    }
    
    public DaoHorariosSquash getDao() {
        return dao;
    }
    
    public void setVo(VoHorarioSquash vo) {
        this.vo = vo;
    }
    
    public void setErro(String erro) {
        this.erro = erro;
    }
}
