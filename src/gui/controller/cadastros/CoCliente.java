/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.cadastros;

import comp.JCalendar;
import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.controller.I_Controller;
import model.vo.cadastros.VoCliente;
import gui.view.cadastros.GuiCliente;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.bo.cadastros.BoCliente;
import model.bo.bd.BoConexao;
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author dlnotari
 */
public class CoCliente implements I_Controller {
    // atributos

    private VoCliente vo;
    private GuiCliente gui;
    private BoCliente bo;

    // construtor
    public CoCliente(GuiCliente gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoCliente();
        this.bo = new BoCliente(vo, conexao);
    }

    /**
     * 
     */
    @Override
    public void salvar() {
        // pega valores
        int codigo = Data.converteStringToInteiro(this.getGui().getjTextField1().getText());

        // create vo
        this.setVo(new VoCliente(codigo,
                this.getGui().getjTextField3().getText(),
                this.getGui().getjTextField2().getText(),
                Data.retiraMascaraData(this.getGui().getjFormattedTextFieldData().getText()),
                Data.retiraMascaraTelefone(this.getGui().getjFormattedTextFieldTelefone().getText())));

        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Cliente", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), "Cliente Cadastrado com sucesso",
                    "Cadastrar Cliente", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        this.limpar();
    }

    /**
     * 
     */
    @Override
    public void excluir() {
        // pega valores
        int codigo = Data.converteStringToInteiro(this.getGui().getjTextField1().getText());

        // pergunta se quer realmente excluir
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente excluir"
                + " este cliente?",
                "Cadastrar Cliente", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            this.limpar();
            return;
        }

        // create vo
        this.setVo(new VoCliente(codigo));

        // excluir
        if (this.getBo().excluir()) {
            JOptionPane.showMessageDialog(this.getGui(), "Cliente excluído com sucesso",
                    "Cadastrar Cliente", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Cliente", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        this.limpar();
    }

    /**
     * 
     */
    @Override
    public void obterLista() {
        // obter lista de clientes
        ResultSet rs = this.getBo().obterLista();

        // cria objeto controlador da tela de consulta
        String title = "Consulta lista de clientes";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.Cliente);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // cria objeto vo
            this.setVo(new VoCliente(Integer.parseInt(objeto.get(0).toString()),
                    objeto.get(1).toString(),
                    objeto.get(2).toString(),
                    objeto.get(3).toString(),
                    objeto.get(4).toString()));

            // set vo em bo
            this.getBo().setVo(this.getVo());

            // mostrar campos
            this.setCampos();
        }
    }

    /**
     * 
     */
    @Override
    public void consultaCodigo() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField1().getText();
        if (aux.trim().isEmpty()) {
            // codigo não informado
            this.limpar();
            return;
        }

        // pega código
        int codigo = Integer.parseInt(aux);

        // create vo
        this.setVo(new VoCliente(codigo));

        // validar codigo
        this.getBo().setVo(this.getVo());
        if (!this.getBo().validarCodigo()) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Cliente", JOptionPane.ERROR_MESSAGE);
            this.limpar();
            return;
        }

        // pesquisar
        if (this.getBo().consultarCodigo(codigo)) {
            this.setVo(this.getBo().getVo());
            this.setCampos();
        } else {
            // request focus
            this.getGui().getjTextField2().requestFocus();
        }
    }

    /**
     * 
     */
    @Override
    public void limpar() {
        // pega o próximo código livre
        int codigo = this.getBo().proximoCodigoLivre();
        this.getGui().getjTextField1().setText(String.valueOf(codigo));
        this.getGui().getjTextField2().setText("");
        this.getGui().getjTextField3().setText("");
        this.getGui().getjFormattedTextFieldData().setText("");
        this.getGui().getjFormattedTextFieldTelefone().setText("");

        // request focus
        this.getGui().getjTextField1().requestFocus();
    }

    /**
     * 
     */
    @Override
    public void setCampos() {
        // converte data
        Date date = ValidarStrings.converteDataBDParaDataComboBox(this.getVo().getDataAniversario());
        comp.JCalendar aux = new JCalendar();
        aux.getEditor().setItem(date);

        // set campos
        this.getGui().getjTextField1().setText(String.valueOf(this.getVo().getCodigo()));
        this.getGui().getjTextField2().setText(this.getVo().getNome());
        this.getGui().getjTextField3().setText(this.getVo().getCpf());
        this.getGui().getjFormattedTextFieldData().setText(this.getVo().getDataAniversario());
        this.getGui().getjFormattedTextFieldTelefone().setText(this.getVo().getTelefone());

        // request focus
        this.getGui().getjTextField2().requestFocus();
    }

    /**
     * getters and setters
     */
    public VoCliente getVo() {
        return vo;
    }

    public void setVo(VoCliente vo) {
        this.vo = vo;
    }

    public GuiCliente getGui() {
        return gui;
    }

    public void setGui(GuiCliente gui) {
        this.gui = gui;
    }

    public BoCliente getBo() {
        return bo;
    }

    public void setBo(BoCliente bo) {
        this.bo = bo;
    }
}
