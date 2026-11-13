/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.cadastros;

import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.controller.I_Controller;
import model.vo.cadastros.VoProduto;
import gui.view.cadastros.GuiProduto;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoProduto;
import javax.swing.JOptionPane;
import utils.Data;

/**
 *
 * @author dlnotari
 */
public class CoProduto implements I_Controller {
    // atributos

    private VoProduto vo;
    private GuiProduto gui;
    private BoProduto bo;

    // construtor
    public CoProduto(GuiProduto gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoProduto();
        this.bo = new BoProduto(vo, conexao);
    }

    /**
     * *
     *
     */
    @Override
    public void salvar() {
        // pega valores
        int codigo = Data.converteStringToInteiro(this.getGui().getjTextField1().getText());
        double preco = (double) this.getGui().getjFormattedTextFieldPreco().getValue();

        // create vo
        this.setVo(new VoProduto(codigo,
                this.getGui().getjTextField2().getText(),
                preco,
                this.getGui().getjComboBox1().getSelectedItem().toString()));

        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Produto", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), "Produto Cadastrado com sucesso",
                    "Cadastrar Produto", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        this.limpar();
    }

    /**
     * *
     *
     */
    @Override
    public void excluir() {
        // pega valores
        int codigo = Data.converteStringToInteiro(this.getGui().getjTextField1().getText());

        // pergunta se quer realmente excluir
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente excluir"
                + " este produto?",
                "Cadastrar Produto", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            this.limpar();
            return;
        }

        // create vo
        this.setVo(new VoProduto(codigo));

        // excluir
        if (this.getBo().excluir()) {
            JOptionPane.showMessageDialog(this.getGui(), "Produto excluído com sucesso",
                    "Cadastrar Produto", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Produto", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        this.limpar();
    }

    /**
     * *
     *
     */
    @Override
    public void obterLista() {
        // obter lista de produtos
        ResultSet rs = this.getBo().obterLista();

        // cria objeto controlador da tela de consulta
        String title = "Consulta lista de produtos";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.Produto);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // cria objeto vo
            this.setVo(new VoProduto(Integer.parseInt(objeto.get(0).toString()),
                    objeto.get(1).toString(),
                    Double.parseDouble(objeto.get(2).toString()),
                    objeto.get(3).toString()));

            // set vo em bo
            this.getBo().setVo(this.getVo());

            // mostrar campos
            this.setCampos();
        }
    }

    /**
     * *
     *
     */
    @Override
    public void consultaCodigo() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField1().getText();
        if (aux.trim().isEmpty()) {
            // codigo não informado
            limpar();
            return;
        }

        // pega código
        int codigo = Integer.parseInt(aux);

        // create vo
        this.setVo(new VoProduto(codigo));

        // validar codigo
        this.getBo().setVo(this.getVo());
        if (!this.getBo().validarCodigo()) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Produto", JOptionPane.ERROR_MESSAGE);
            this.limpar();
            return;
        }

        // pesquisar
        if (this.getBo().consultarCodigo(codigo)) {
            // set vo
            this.setVo(this.getBo().getVo());

            // atribuir
            this.setCampos();
        } else {
            // request focus
            this.getGui().getjTextField2().requestFocus();
        }
    }

    /**
     * *
     *
     */
    @Override
    public void limpar() {
        // pega o próximo código livre
        int codigo = this.getBo().proximoCodigoLivre();
        this.getGui().getjTextField1().setText(String.valueOf(codigo));
        this.getGui().getjTextField2().setText("");
        this.getGui().getjComboBox1().setSelectedIndex(0);
        this.getGui().getjFormattedTextFieldPreco().setText("0");

        // request focus
        this.getGui().getjTextField1().requestFocus();
    }

    /**
     * *
     *
     */
    @Override
    public void setCampos() {
        this.getGui().getjTextField1().setText(String.valueOf(this.getVo().getCodigo()));
        this.getGui().getjTextField2().setText(this.getVo().getDescricao());
        this.getGui().getjComboBox1().setSelectedItem(this.getVo().getUnidade());
        this.getGui().getjFormattedTextFieldPreco().setText(String.valueOf(this.getVo().getPreco()));

        // request focus
        this.getGui().getjTextField2().requestFocus();
    }

    // getters and setters
    public VoProduto getVo() {
        return vo;
    }

    public void setVo(VoProduto vo) {
        this.vo = vo;
    }

    public GuiProduto getGui() {
        return gui;
    }

    public void setGui(GuiProduto gui) {
        this.gui = gui;
    }

    public BoProduto getBo() {
        return bo;
    }

    public void setBo(BoProduto bo) {
        this.bo = bo;
    }
}
