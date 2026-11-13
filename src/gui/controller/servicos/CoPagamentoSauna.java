/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.E_BD;
import gui.controller.I_Controller;
import gui.view.serviços.GuiPagamentoSauna;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoPagamento;
import model.bo.servicos.BoSauna;
import model.vo.cadastros.VoPagamento;
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author dlnotari
 */
public class CoPagamentoSauna implements I_Controller {
    // atributos

    private VoPagamento vo;
    private GuiPagamentoSauna gui;
    private BoPagamento bo;
    private BoSauna boSauna;
    private int posicao;

    // construtor
    public CoPagamentoSauna(GuiPagamentoSauna gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoPagamento();
        this.bo = new BoPagamento(vo, conexao);
    }

    /**
     * calcula o valor a ser pago com desconto
     */
    public void calcularTotal() {
        // pega valores
        double valorConsumo = (double) this.getGui().getjFormattedTextFieldValorConsumo().getValue();
        double desconto = (double) this.getGui().getjFormattedTextFieldValorDesconto().getValue();

        // testa se já foi selecionado um cliente
        if (valorConsumo <= 0) {
            return;
        }

        // gera o valor a ser pago        
        double valorPago = valorConsumo - desconto;

        // devolve o valor para a tela
        this.getGui().getjFormattedTextFieldTotalPagar().setText(String.valueOf(valorPago));
    }

    /**
     *
     * @return
     */
    @Override
    public void excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * pesquisa consumo com a Jtable
     */
    @Override
    public void obterLista() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public void limpar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * prepara tela para consumo a partir da sauna
     */
    public void prepararTelaPagamentoSauna(String nome, double valor, int posicao, BoSauna boSauna) {
        // set valores iniciais
        this.getGui().getjLabel7().setText(nome);
        this.getGui().getjFormattedTextFieldValorConsumo().setText(String.valueOf(valor));
        this.getGui().getjFormattedTextFieldTotalPagar().setText(String.valueOf(valor));
        this.getGui().getjFormattedTextFieldValorDesconto().setText("0");
        this.getGui().getjFormattedTextFieldData().setText(ValidarStrings.getDataHoje());        
        this.setPosicao(posicao);
        this.setBoSauna(boSauna);
    }

    /**
     * *
     * controla pagamento da sauna
     */
    @Override
    public void salvar() {
        // pergunta se quer realmente pagar
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente efetuar"
                + " o pagamento deste cliente?\n Um pagamento feito não poderá ser excluíd8o!",
                "Cadastrar Pagamento da sauna", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            return;
        }

        // valida campos digitados
        if (this.getGui().getjFormattedTextFieldTotalPagar().getText().isEmpty()) {
            JOptionPane.showMessageDialog(this.getGui(), "Valor a ser pago deve ser informado!",
                    "Cadastrar Pagamento Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // pega o desconto
            double desconto = (double) this.getGui().getjFormattedTextFieldValorDesconto().getValue();

            // pega e converte valores
            String nome = this.getGui().getjLabel7().getText();
            String data = Data.retiraMascaraData(this.getGui().getjFormattedTextFieldData().getText());
            double valorConsumo = (double) this.getGui().getjFormattedTextFieldValorConsumo().getValue();
            double valorPago = (double) this.getGui().getjFormattedTextFieldTotalPagar().getValue();

            // verificar se há diferença entre o valor pago e o valor gasto
            if (valorPago != (valorConsumo - desconto)) {
                // pergunta se quer realmente pagar
                resp = JOptionPane.showConfirmDialog(this.getGui(),
                        "Existe diferença entre o valor gasto ( " + valorConsumo
                        + " ) e o valor que está sendo pago ( " + valorPago + " )."
                        + "Confirma o pagamento deste valor?",
                        "Cadastrar Pagamento da sauna", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            // create vo
            this.setVo(new VoPagamento(Integer.parseInt(data), valorPago, nome));

            // gerar sequencia
            this.getBo().setVo(this.getVo());
            this.getVo().setSequencia(this.getBo().proximoCodigoLivre());

            // salvar
            this.getBo().setVo(this.getVo());
            if (!this.getBo().salvarConsumoSauna()) {
                // houve erro
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Cadastrar Pagamento da Sauna", JOptionPane.ERROR_MESSAGE);
            } else {
                // pagar itens consumidos como pagos
                if (!this.getBoSauna().gerarPagamentoCliente(nome, ValidarStrings.getDataHoje(),
                        this.getPosicao(), valorConsumo, valorPago)) {
                    // erro
                    JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                            "Cadastrar Pagamento Cliente Sauna", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // mensagem
                JOptionPane.showMessageDialog(this.getGui(),
                        "Pagamento realizado com sucesso!",
                        "Cadastrar Pagamento Cliente Sauna", JOptionPane.ERROR_MESSAGE);

                // fecha janela
                this.getGui().setVisible(false);
            }

        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    this.getBo().getErro() + "\n" + ex.toString(),
                    "Cadastrar Pagamento Cliente Sauna", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public void setCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public void consultaCodigo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * getters and setters
     */
    public VoPagamento getVo() {
        return vo;
    }

    public void setVo(VoPagamento vo) {
        this.vo = vo;
    }

    public GuiPagamentoSauna getGui() {
        return gui;
    }

    public BoPagamento getBo() {
        return bo;
    }

    private BoSauna getBoSauna() {
        return boSauna;
    }

    private void setBoSauna(BoSauna boSauna) {
        this.boSauna = boSauna;
    }

    private int getPosicao() {
        return posicao;
    }

    private void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
