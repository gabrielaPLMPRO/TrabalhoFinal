/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.cadastros;

import gui.controller.I_Controller;
import gui.view.cadastros.GuiValores;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoValores;
import model.vo.cadastros.VoValores;
import utils.Data;


/**
 *
 * @author dlnotari
 */
public class CoValores implements I_Controller{
    // atributos
    private VoValores vo;
    private GuiValores gui;
    private BoValores bo;
    
    // construtor
    public CoValores(GuiValores gui,  BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoValores();
        this.bo = new BoValores(vo, conexao);
    }    
    
    @Override
    public void salvar() {
        // pegar valores 
        double valorSauna = (double) this.getGui().getjFormattedTextFieldValorSauna().getValue();
        double valorSquash1 = (double) this.getGui().getjFormattedTextFieldValorSquash1().getValue();
        double valorSquash2 = (double) this.getGui().getjFormattedTextFieldValorSquash2().getValue();
        double valorSquash3 = (double) this.getGui().getjFormattedTextFieldValorSquash3().getValue();
        double valorAulaSquash = (double) this.getGui().getjFormattedTextFieldValorAula().getValue();
        
        // pega vencimentos
        int vencimento1 = Data.converteStringToInteiro(this.getGui().getjTextFieldVenc1().getText());
        int vencimento2 = Data.converteStringToInteiro(this.getGui().getjTextFieldVenc2().getText());
        int vencimento3 = Data.converteStringToInteiro(this.getGui().getjTextFieldVenc3().getText());
        
        // create vo
        this.setVo(new VoValores(1, valorSauna, valorAulaSquash, valorSquash1,
                valorSquash2, valorSquash3, vencimento1, vencimento2, vencimento3));
        
        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Valores", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), "Valores cadastrados com sucesso",
                    "Cadastrar Valores", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        this.limpar();        
    }

    @Override
    public void excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void obterLista() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void limpar() {
        this.getGui().getjTextFieldVenc1().setText("");        
        this.getGui().getjTextFieldVenc2().setText("");
        this.getGui().getjTextFieldVenc3().setText("");        
        this.getGui().getjFormattedTextFieldValorAula().setText("0");
        this.getGui().getjFormattedTextFieldValorSauna().setText("0");        
        this.getGui().getjFormattedTextFieldValorSquash1().setText("0");
        this.getGui().getjFormattedTextFieldValorSquash2().setText("0");     
        this.getGui().getjFormattedTextFieldValorSquash3().setText("0");     
        
        // request focus
        this.getGui().getjFormattedTextFieldValorSauna().requestFocus(); 
    }

    @Override
    public void setCampos() {
        // teste se há valores
        if (this.getVo().getValorSauna() == 0) {
            this.limpar();
            return;
        }
        
        // atribui campos se não for zero
        this.getGui().getjFormattedTextFieldValorSauna().setText(String.valueOf(this.getVo().getValorSauna()));
        this.getGui().getjTextFieldVenc1().setText(String.valueOf(this.getVo().getVencimento1()));        
        this.getGui().getjFormattedTextFieldValorSquash1().setText(String.valueOf(this.getVo().getValorSquashMensal1()));
        this.getGui().getjTextFieldVenc2().setText(String.valueOf(this.getVo().getVencimento2()));        
        this.getGui().getjFormattedTextFieldValorSquash2().setText(String.valueOf(this.getVo().getValorSquashMensal2()));
        this.getGui().getjTextFieldVenc3().setText(String.valueOf(this.getVo().getVencimento3()));        
        this.getGui().getjFormattedTextFieldValorSquash3().setText(String.valueOf(this.getVo().getValorSquashMensal3()));
        this.getGui().getjFormattedTextFieldValorAula().setText(String.valueOf(this.getVo().getValorAulaSquash()));  
        
        // request focus
        this.getGui().getjTextFieldVenc1().requestFocus(); 
    }

    @Override
    public void consultaCodigo() {
        // create vo
        this.setVo(new VoValores());

        // pesquisar
        this.getBo().setVo(this.getVo());
        if (this.getBo().consultarCodigo(1)) {
            this.setVo(this.getBo().getVo());
        }
        
        // atribuir
        this.setCampos();
    }

    public VoValores getVo() {
        return vo;
    }

    public void setVo(VoValores vo) {
        this.vo = vo;
    }

    public GuiValores getGui() {
        return gui;
    }

    public BoValores getBo() {
        return bo;
    }
}
