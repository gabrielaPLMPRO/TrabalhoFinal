/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.view.serviços.GuiFichaCliente;
import java.awt.Color;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.JTableHeader;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoCliente;
import model.bo.cadastros.BoConsumo;
import model.bo.cadastros.BoPagamento;
import model.bo.servicos.BoHorarioSquash;
import model.vo.cadastros.VoCliente;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoPagamento;
import model.vo.servicos.VoHorarioSquash;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class CoFichaCliente {
    // atributos

    private GuiFichaCliente gui;
    private BoCliente boCliente;
    private BoConsumo boConsumo;
    private BoPagamento boPagamento;
    private BoHorarioSquash boHorarioSquash;
    private VoCliente vo;

    // construtor
    public CoFichaCliente(GuiFichaCliente gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoCliente();
        this.boCliente = new BoCliente(this.vo, conexao);
        this.boConsumo = new BoConsumo(new VoConsumo(), conexao);
        this.boPagamento = new BoPagamento(new VoPagamento(), conexao);
        this.boHorarioSquash = new BoHorarioSquash(new VoHorarioSquash(), conexao, null);
    }

    /**
     * consulta o cliente a partir do código informado
     */
    public void consultarCliente() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField1().getText();
        if (aux.isEmpty()) {
            // codigo não informado
            this.getGui().getjTextField1().requestFocus();
            return;
        }

        // pega código
        int codigo = Integer.parseInt(aux);

        // pesquisar
        this.getBoCliente().setErro("");
        if (this.getBoCliente().consultarCodigo(codigo)) {
            // testa se o nome foi informado
            if (!this.getBoCliente().getVo().getNome().isEmpty()) {
                // seta código cliente
                this.getVo().setCodigo(codigo);

                // seta nome do cliente
                this.getGui().getjLabel7().setText(this.getBoCliente().getVo().getNome());

                // mostra os dados da ficha do cliente
                this.pesquisarDadosCliente();
            } else {
                JOptionPane.showMessageDialog(this.getGui(), "Cliente não cadastrado!",
                        "Ficha do Cliente", JOptionPane.ERROR_MESSAGE);
                this.getGui().getjTextField1().setText("");
                this.getGui().getjLabel7().setText("");
                this.getGui().getjTextField1().requestFocus();
            }
        } else {
            if (!this.getBoCliente().getErro().isEmpty()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBoCliente().getErro(),
                        "Ficha do Cliente", JOptionPane.ERROR_MESSAGE);
                this.getGui().getjTextField1().setText("");
                this.getGui().getjLabel7().setText("");
                this.getGui().getjTextField1().requestFocus();
            }
        }
    }

    /**
     * pesquisa cliente com a Jtable
     */
    public void pesquisarCliente() {
        // obter lista de clientes
        ResultSet rs = this.getBoCliente().obterLista();

        // cria objeto controlador da tela de consulta
        String title = "Consulta lista de clientes para consumo de produtos";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.Consumo);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // seta código do cliente
            this.getVo().setCodigo(Integer.parseInt(objeto.get(0).toString()));
            this.getGui().getjTextField1().setText(objeto.get(0).toString());

            // seta descrição produto
            this.getGui().getjLabel7().setText(objeto.get(2).toString());

            // mostra os dados da ficha do cliente
            this.pesquisarDadosCliente();
        }
    }

    /**
     * *
     * pesquisa os valores necessários e mostra eles na tela
     */
    private void pesquisarDadosCliente() {
        // pega código do cliente
        String codCliente = this.getGui().getjTextField1().getText();

        // testa o código do cliente
        if (codCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this.getGui(), "Informe o cliente a ser consultado!",
                    "Ficha do Cliente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // converte valores
        int codigoCliente = Integer.parseInt(codCliente);

        // limpar área de texto
        this.getGui().getjTextArea1().setText("");

        // pesquisa as reservas de horários do cliente
        if (!this.pesquisarReservasSquash(codigoCliente)) {
            return;
        }

        // pesquisa o total devido
        if (!this.pesquisarTotalDevidoCliente(codigoCliente)) {
            return;
        }

        // pesquisa os valores de consumo
        this.pesquisarConsumoCliente(codigoCliente);
    }

    /**
     * monta a lista de consumos de um cliente
     */
    private boolean pesquisarConsumoCliente(int codigoCliente) {
        // set vo
        this.getBoConsumo().setVo(new VoConsumo(codigoCliente));
        this.getBoConsumo().setErro("");

        // obter lista de consumo do cliente        
        ResultSet rs = this.getBoConsumo().obterLista(false, true);

        // testa se houve erro
        if (!this.getBoConsumo().getErro().isEmpty()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBoConsumo().getErro()
                    + "\nAo pesquisar a lista de consumos do cliente!",
                    "Consultar Ficha do Cliente", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // testa se está vazio o resultado
        if (rs == null) {
            JOptionPane.showMessageDialog(this.getGui(), "Não existem informações "
                    + "a serem mostradas para esta data!",
                    "Ficha do Cliente!", JOptionPane.ERROR_MESSAGE);
            return false;            
        }
        
        // cria objeto controlador da tela de consulta
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, "", DaoTabelas.Consumo);

        // constroi a tabela de consulta de consumos
        JTable tabela = controllerConsulta.montarTelaConsultar();

        // seta preenchimento
        tabela.setFillsViewportHeight(true);

        // set font
        tabela.setFont(new java.awt.Font("Arial", 1, 18));
        tabela.setRowHeight(28);
        tabela.setBorder(new BevelBorder(1, Color.black, Color.black));
        tabela.setShowGrid(true);

        // monta cabecalho da tabela
        JTableHeader header = tabela.getTableHeader();
        header.setForeground(Color.black);
        header.setFont(new java.awt.Font("Arial", 1, 18));
        header.setBorder(new BevelBorder(1, Color.black, Color.black));

        // adiciona ao container
        Rectangle aux = this.getGui().getjScrollPane2().getBounds();
        this.getGui().getContentPane().remove(this.getGui().getjScrollPane2());
        this.getGui().setjScrollPane2(new JScrollPane(tabela));
        this.getGui().getjScrollPane2().setBounds(aux);
        this.getGui().getContentPane().add(this.getGui().getjScrollPane2());

        // set seleção
        tabela.setEnabled(false);

        // return
        return true;
    }

    /**
     * *
     * calcular total devido pelo cliente
     */
    private boolean pesquisarTotalDevidoCliente(int codigoCliente) {
        // set vo
        this.getBoPagamento().setVo(new VoPagamento(codigoCliente));
        this.getBoPagamento().setErro("");

        // buscar o total
        double total = this.getBoPagamento().totalPagar();

        // testa se houve erro
        if (!this.getBoPagamento().getErro().isEmpty()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBoPagamento().getErro()
                    + "\nAo pesquisar o total a ser pago pelo cliente1",
                    "Consultar Ficha do Cliente", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // montar texto
        String texto = "O total de consumo devido até o momento é de R$ " 
                + ValidarStrings.formatarDouble(total);

        // mostra o total a pagar
        this.getGui().getjTextArea1().append(texto);

        // return
        return true;
    }

    /**
     * *
     * pesquisa as reservas de squash do cliente
     */
    private boolean pesquisarReservasSquash(int codigoCliente) {
        // set vo
        this.getBoHorarioSquash().setVo(new VoHorarioSquash(codigoCliente));
        this.getBoHorarioSquash().setErro("");

        // obtem a lista de horarios
        ArrayList<VoHorarioSquash> lista = this.getBoHorarioSquash().obterListaReservaCliente();

        // testa se houve erro
        if (!this.getBoHorarioSquash().getErro().isEmpty()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBoHorarioSquash().getErro()
                    + "\nAo pesquisar as reservas de squash do cliente!",
                    "Consultar Ficha do Cliente", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // monta o texto
        StringBuilder texto = new StringBuilder();

        // testa se há reservas
        if (lista.isEmpty()) {
            texto.append("Este cliente não possui reservas para quadras de Squash!\n");
        } else {
            // varre a lista
            for (VoHorarioSquash voAux : lista) {
                texto.append(voAux.listaReservas()).append("\n");
            }
        }

        // mostrar na tela
        this.getGui().getjTextArea1().append(texto.toString());

        // return
        return true;
    }

    /**
     * *
     * limpar os campos da tela
     */
    public void limpar() {
        this.getGui().getjTextField1().setText("");
        this.getGui().getjLabel7().setText("");
        this.getGui().getjScrollPane2().removeAll();
        this.getGui().getjTextArea1().setText("");
    }

    /**
     * *
     * getters and setters
     *
     * @return
     */
    public GuiFichaCliente getGui() {
        return gui;
    }

    public BoCliente getBoCliente() {
        return boCliente;
    }

    public VoCliente getVo() {
        return vo;
    }

    public BoConsumo getBoConsumo() {
        return boConsumo;
    }

    public BoPagamento getBoPagamento() {
        return boPagamento;
    }

    public BoHorarioSquash getBoHorarioSquash() {
        return boHorarioSquash;
    }
}
