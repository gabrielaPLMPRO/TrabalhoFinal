/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.E_BD;
import gui.view.serviços.GuiResumoMensal;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.bo.bd.BoConexao;
import model.bo.servicos.BoFluxoCaixa;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class CoResumoMensal {
    // atributos

    private BoFluxoCaixa bo;
    private GuiResumoMensal gui;

    // construtor
    public CoResumoMensal(GuiResumoMensal gui, BoConexao conexao) {
        this.gui = gui;
        this.bo = new BoFluxoCaixa(conexao);
    }

    /**
     * *
     * limpar os campos da tela
     */
    public void limpar() {
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
                        "Fluxo de Caixa: Resumo Mensal.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // separa mes e ano
            int mes = ValidarStrings.getMesData(data);
            int ano = ValidarStrings.getAnoData(data);

            // cria uma matriz para armazenar as informações
            // posição [][0] - total vendido da sauna
            // posição [][1] - total vendido do squash
            // posição [][2] - total vendido de produtos
            // posição [][3] - total vendido de todos acima
            // posição [][4] - total não pago da sauna
            // posição [][5] - total não pago do squash
            // posição [][6] - total não pago de produtos
            // posição [][7] - total não pago dos produtos acima
            // posição [][8] - total a receber
            // posição [31][ ] - contém o total de cada coluna
            double valores[][] = new double[32][9];

            // percorre todos os dias do mes
            for (int i = 1; i < 31; i++) {
                // montar data
                data = ValidarStrings.montarDataString(i, mes, ano);
                
                // pegar valor
                double totalNaoPagoSauna    = this.getBo().calcularTotalNaoPagoSauna(data);
                double totalNaoPagoSquash   = this.getBo().calcularTotalNaoPagoSquash(data);
                double totalNaoPagoProdutos = this.getBo().calcularTotalNaoPagoConsumo(data);
                double totalPagoSauna       = this.getBo().calcularTotalPagoSauna(data);
                double totalPagoSquash      = this.getBo().calcularTotalPagoSquash(data);
                double totalPagoProdutos    = this.getBo().calcularTotalPagoConsumo(data);

                // calcular total vendido da sauna
                valores[i - 1][0] = totalNaoPagoSauna + totalPagoSauna;

                // calcular total vendido no squash
                valores[i - 1][1] = totalNaoPagoSquash + totalPagoSquash;

                // calcular total vendido no consumo
                valores[i - 1][2] = totalNaoPagoProdutos + totalPagoProdutos;

                // total vendido por dia
                valores[i - 1][3] = totalNaoPagoSauna + totalPagoSauna +
                                    totalNaoPagoSquash + totalPagoSquash +
                                    totalNaoPagoProdutos + totalPagoProdutos;

                // calcular total pago na sauna
                valores[i - 1][4] = totalPagoSauna;

                // calcular total pago no squash
                valores[i - 1][5] = totalPagoSquash;

                // calcular total não pago no consumo
                valores[i - 1][6] = totalPagoProdutos;

                // total vendido por dia
                valores[i - 1][7] = totalPagoSauna + totalPagoSquash + totalPagoProdutos;
                
                // total a receber do dia
                valores[i - 1][8] = valores[i - 1][3] - valores[i - 1][7];

                // soma o total de cada coluna
                valores[31][0] += valores[i - 1][0];
                valores[31][1] += valores[i - 1][1];
                valores[31][2] += valores[i - 1][2];
                valores[31][3] += valores[i - 1][3];
                valores[31][4] += valores[i - 1][4];
                valores[31][5] += valores[i - 1][5];
                valores[31][6] += valores[i - 1][6];
                valores[31][7] += valores[i - 1][7];
                valores[31][8] += valores[i - 1][8];
            }

            // montar tabela
            this.criarJTable(valores);
        } catch (HeadlessException | SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "Problemas na exibição dos valores consumidos de produtos,"
                    + "sauna e squash!\n"
                    + ex.toString(),
                    "Fluxo de Caixa: Resumo Mensal!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * monta a lista de consumos de um cliente
     */
    private boolean criarJTable(double valores[][]) {
        // criar cabeçalho
        Object[] vetorColunas = new Object[]{"Dia/Consumo",
            "Sauna Vdo", "Squash Vdo", "Produtos Vdo", "Total Vdo",
            "Sauna Pg ", "Squash Pg ", "Produtos Pg ", "Total Pg", "À Receber"};

        // monta cabecalho
        DefaultTableModel dataModel = new DefaultTableModel(vetorColunas, 0);

        // cria Vetor linhas
        Object[] vetorLinhas = new Object[32];

        // carrega linhas
        for (int i = 1; i < 32; i++) {
            // atribui o dia do mês
            vetorLinhas[0] = String.valueOf(i);

            // atribui total não pago na sauna
            vetorLinhas[1] = ValidarStrings.formatarDouble(valores[i - 1][0]);

            // atribui total não pago no squash
            vetorLinhas[2] = ValidarStrings.formatarDouble(valores[i - 1][1]);

            // atribui total pago no consumo
            vetorLinhas[3] = ValidarStrings.formatarDouble(valores[i - 1][2]);

            // atribui vendido por dia
            vetorLinhas[4] = ValidarStrings.formatarDouble(valores[i - 1][3]);

            // atribui total pago na sauna
            vetorLinhas[5] = ValidarStrings.formatarDouble(valores[i - 1][4]);

            // atribui total pago no squash
            vetorLinhas[6] = ValidarStrings.formatarDouble(valores[i - 1][5]);

            // atribui total não pago no consumo
            vetorLinhas[7] = ValidarStrings.formatarDouble(valores[i - 1][6]);

            // atribui vendido por dia
            vetorLinhas[8] = ValidarStrings.formatarDouble(valores[i - 1][7]);
            
            // atribui total a receber do dia
            vetorLinhas[9] = ValidarStrings.formatarDouble(valores[i - 1][8]);            

            // adiciona linha ao data model
            dataModel.addRow(vetorLinhas);
        }

        // carrega total de cada coluna
        vetorLinhas[0] = "Total";
        vetorLinhas[1] = ValidarStrings.formatarDouble(valores[31][0]);
        vetorLinhas[2] = ValidarStrings.formatarDouble(valores[31][1]);
        vetorLinhas[3] = ValidarStrings.formatarDouble(valores[31][2]);
        vetorLinhas[4] = ValidarStrings.formatarDouble(valores[31][3]);
        vetorLinhas[5] = ValidarStrings.formatarDouble(valores[31][4]);
        vetorLinhas[6] = ValidarStrings.formatarDouble(valores[31][5]);
        vetorLinhas[7] = ValidarStrings.formatarDouble(valores[31][6]);
        vetorLinhas[8] = ValidarStrings.formatarDouble(valores[31][7]);
        vetorLinhas[9] = ValidarStrings.formatarDouble(valores[31][8]);
        dataModel.addRow(vetorLinhas);

        // constroi a tabela de consulta de consumos
        JTable tabela = new JTable(dataModel);

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

    public BoFluxoCaixa getBo() {
        return bo;
    }

    public void setBo(BoFluxoCaixa bo) {
        this.bo = bo;
    }

    public GuiResumoMensal getGui() {
        return gui;
    }

    public void setGui(GuiResumoMensal gui) {
        this.gui = gui;
    }
}
