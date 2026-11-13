/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.DaoTabelas;
import dao.bd.E_BD;
import gui.controller.principal.CoConsulta;
import gui.view.serviços.GuiFluxoCaixa;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.JTableHeader;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoConsumo;
import model.bo.servicos.BoFluxoCaixa;
import model.vo.cadastros.VoConsumo;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class CoFluxoCaixa {
    // atributos
    private BoFluxoCaixa bo;
    private BoConsumo boConsumo;
    private GuiFluxoCaixa gui;

    // construtor
    public CoFluxoCaixa(GuiFluxoCaixa gui, BoConexao conexao) {
        this.gui = gui;
        this.boConsumo = new BoConsumo(new VoConsumo(), conexao);
        this.bo = new BoFluxoCaixa(conexao);
    }

    /**
     * *
     * limpar os campos da tela
     */
    public void limpar() {
        this.getGui().getjTextArea1().setText("");
        this.getGui().getjTable1().removeAll();
    }

    /**
     * *
     * pesquisa os valores necessários e mostra eles na tela
     */
    public void pesquisarDados() {
        try {
            // pega código do cliente
            String data = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBox1().getSelectedItem());

            // testa o código do cliente
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this.getGui(), "Informe a data a ser consultada!",
                        "Fluxo de Caixa.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // cria objeto temporário
            StringBuilder texto = new StringBuilder();

            // calcular total da sauna
            texto.append("O total pago da sauna foi R$ ");
            texto.append(ValidarStrings.formatarDouble(this.getBo().calcularTotalPagoSauna(data)));
            texto.append("\n");

            // calcular total da sauna
            texto.append("O total não pago da sauna foi R$ ");
            texto.append(ValidarStrings.formatarDouble(this.getBo().calcularTotalNaoPagoSauna(data)));
            texto.append("\n");

            // calcular total squash
            texto.append("O total pago do squash foi R$ ");
            texto.append(ValidarStrings.formatarDouble(this.getBo().calcularTotalPagoSquash(data)));
            texto.append("\n");

            // calcular total squash
            texto.append("O total não pago do squash foi R$ ");
            texto.append(ValidarStrings.formatarDouble(this.getBo().calcularTotalNaoPagoSquash(data)));
            texto.append("\n");


            // calcular total consumo
            texto.append("O total pago de consumo de produtos foi R$ ");
            texto.append(ValidarStrings.formatarDouble(this.getBo().calcularTotalPagoConsumo(data)));
            texto.append("\n");

            // calcular total consumo
            texto.append("O total não pago de consumo de produtos foi R$ ");
            texto.append(ValidarStrings.formatarDouble(this.getBo().calcularTotalNaoPagoConsumo(data)));
            texto.append("\n");

            // adiciona ao container
            Rectangle aux = this.getGui().getjScrollPane1().getBounds();
            this.getGui().getContentPane().remove(this.getGui().getjScrollPane1());
            this.getGui().getjTextArea1().setText(texto.toString());
            this.getGui().setjScrollPane1(new JScrollPane(this.getGui().getjTextArea1()));
            this.getGui().getjScrollPane1().setBounds(aux);
            this.getGui().getContentPane().add(this.getGui().getjScrollPane1());

            // pesquisa os valores de consumo
            this.pesquisarConsumoCliente(data);
        } catch (HeadlessException | SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "Problemas na exibição dos valores consumidos de produtos,"
                    + "sauna e squash!\n"
                    + ex.toString(),
                    "Fluxo de Caixa!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * monta a lista de consumos de um cliente
     */
    private boolean pesquisarConsumoCliente(String data) {
        // set vo
        this.getBoConsumo().setVo(new VoConsumo(data));
        this.getBoConsumo().setErro("");

        // obter lista de consumo do cliente        
        ResultSet rs = this.getBoConsumo().obterLista(true, true);

        // testa se houve erro
        if (!this.getBoConsumo().getErro().isEmpty()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBoConsumo().getErro()
                    + "\nAo pesquisar a lista de consumos!",
                    "Fluxo de Caixa.", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // testa se está vazio o resultado
        if (rs == null) {
            JOptionPane.showMessageDialog(this.getGui(), "Não existem informações "
                    + "de consumo a serem mostradas para esta data!",
                    "Fluxo de Caixa!", JOptionPane.ERROR_MESSAGE);
            return true;
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

    // getters and setters  
    public GuiFluxoCaixa getGui() {
        return gui;
    }

    public BoConsumo getBoConsumo() {
        return boConsumo;
    }

    private BoFluxoCaixa getBo() {
        return bo;
    }
}
