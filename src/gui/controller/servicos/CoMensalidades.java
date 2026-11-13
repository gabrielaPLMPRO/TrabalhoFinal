/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import gui.view.serviços.GuiGerarMensalidades;
import java.util.Calendar;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.servicos.BoMensalidade;
import model.vo.cadastros.VoValores;
import model.vo.servicos.VoHorarioSquash;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class CoMensalidades {
    // atributos

    private GuiGerarMensalidades gui;
    private BoMensalidade bo;

    // construtor
    public CoMensalidades(GuiGerarMensalidades gui, BoConexao conexao) {
        this.gui = gui;
        this.bo = new BoMensalidade(new VoHorarioSquash(), conexao);
    }

    /**
     * *
     * controla a geração de mensalidades e/ou multas
     */
    public void gerar() {
        // validar se foi feita uma seleção
        if ((!this.getGui().getjCheckBox1().isSelected()) && ((!this.getGui().getjCheckBox2().isSelected()))) {
            // nenhuma seleção foi feita
            JOptionPane.showMessageDialog(this.getGui(),
                    "Você deve selecionar uma das opções de geração de mensalidades e/ou multas!",
                    "Gerenciamento Mensalidades e/ou Multas!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // fluxo de controle 
        try {
            // pega a data
            String dataHoje = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBox1().getSelectedItem());

            // pegar valores da data
            int dia = ValidarStrings.getDiaData(dataHoje);
            int mes = ValidarStrings.getMesData(dataHoje);
            int ano = ValidarStrings.getAnoData(dataHoje);

            // pegar os valores e datas padrão das reservas de squash
            VoValores voValores = this.getGui().getGui().getController().getGuiValores().getCo().getVo();
            
            // cria variavel texto
            this.getBo().setTextoJanela(new StringBuilder());            

            // gerar mensalidades
            if (this.getGui().getjCheckBox1().isSelected()) {
                // gerar mensalidades
                if (this.getBo().gerarMensalidades(mes, ano, dataHoje, voValores)) {
                    // atualizar tela
                    this.getGui().getjTextArea1().setText(this.getBo().getTextoJanela().toString());
                    
                    // mensagem 
                    JOptionPane.showMessageDialog(this.getGui(),
                            "Mensalidades geradas com sucesso!",
                            "Gerenciamento Mensalidades e/ou Multas!", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                            "Gerenciamento Mensalidades e/ou Multas!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // gerar multas
            if (this.getGui().getjCheckBox2().isSelected()) {
                // gerar multas
                if (this.getBo().gerarMultas(dia, mes, ano, dataHoje, voValores)) {
                    // atualizar tela
                    this.getGui().getjTextArea1().setText(this.getBo().getTextoJanela().toString());
                    
                    // mensagem 
                    JOptionPane.showMessageDialog(this.getGui(),
                            "Multas geradas com sucesso!",
                            "Gerenciamento Mensalidades e/ou Multas!", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                            "Gerenciamento Mensalidades e/ou Multas!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "Problemas na geração das mensalidades e/ou multas para as "
                    + "reservas das quadras de squash!\n"
                    + ex.toString(),
                    "Gerenciamento Mensalidades e/ou Multas!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * *
     * limpa os campos
     */
    public void limpar() {
        this.getGui().getjCheckBox1().setSelected(false);
        this.getGui().getjCheckBox2().setSelected(false);
        this.getGui().getjTextArea1().setText("");
    }

    public GuiGerarMensalidades getGui() {
        return gui;
    }

    public BoMensalidade getBo() {
        return bo;
    }
}
