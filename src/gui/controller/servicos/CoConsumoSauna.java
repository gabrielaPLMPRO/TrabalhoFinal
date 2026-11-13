/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.controller.I_Controller;
import gui.view.serviços.GuiConsumoSauna;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.bo.cadastros.BoCliente;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoConsumo;
import model.bo.cadastros.BoProduto;
import model.vo.cadastros.VoCliente;
import model.vo.cadastros.VoConsumo;
import model.vo.servicos.VoConsumoSauna;
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author dlnotari
 */
public class CoConsumoSauna implements I_Controller {
    // atributos

    private VoConsumo vo;
    private GuiConsumoSauna gui;
    private BoConsumo bo;
    private BoCliente boCliente;
    private BoProduto boProduto;

    // construtor
    public CoConsumoSauna(GuiConsumoSauna gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoConsumo();
        this.bo = new BoConsumo(vo, conexao);
        this.boCliente = new BoCliente(new VoCliente(), conexao);
        this.boProduto = new BoProduto(new VoConsumo(), conexao);
    }

    /**
     * consulta o produto a partir do código informado
     */
    public void consultarProduto() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField2().getText();
        if (aux.trim().isEmpty()) {
            // codigo não informado
            // não pode usar controle de foco aqui porque entra em conflito
            // com o resultado do botão pesquisar produto
            //this.getGui().getjTextField2().requestFocus();
            return;
        }

        // pega código
        int codigo = Data.converteStringToInteiro(this.getGui().getjTextField2().getText());

        // pesquisar
        this.getBoProduto().setErro("");
        if (this.getBoProduto().consultarCodigo(codigo)) {
            // seta código produto
            this.getVo().setCodigo(codigo);

            // seta preço produto
            this.getVo().setPreco(this.getBoProduto().getVo().getPreco());

            // set campos
            this.getGui().getjTextField2().setText(String.valueOf(codigo));
            this.getGui().getjLabel8().setText(this.getBoProduto().getVo().getDescricao());
            this.getGui().getjFormattedTextFieldValor().setText(String.valueOf(this.getBoProduto().getVo().getPreco()));

            // focus
            this.getGui().getjTextField3().requestFocus();
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBoProduto().getErro(),
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            // focus
            this.getGui().getjTextField2().setText("");
            this.getGui().getjTextField2().requestFocus();
        }
    }

    /**
     * pesquisa produto com a Jtable
     */
    public void pesquisarProduto() {
        // obter lista de produtos
        ResultSet rs = this.getBoProduto().obterLista();

        // cria objeto controlador da tela de consulta
        String title = "Consulta lista de produtos para consumo do cliente";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.ConsumoSauna);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // pega valores
            int codigo = Integer.parseInt(objeto.get(0).toString());
            double valor = Double.parseDouble(objeto.get(2).toString());

            // seta vo
            this.getVo().setCodigo(codigo);
            this.getVo().setPreco(valor);

            // set campos
            this.getGui().getjTextField2().setText(String.valueOf(codigo));
            this.getGui().getjLabel8().setText(objeto.get(1).toString());
            this.getGui().getjFormattedTextFieldValor().setText(String.valueOf(valor));

            // focus
            this.getGui().getjTextField3().requestFocus();
        }
    }

    /**
     * calcularTotal
     */
    public void calcularTotal() {
        // pega a quantidade e o preço
        int quantidade = Data.converteStringToInteiro(this.getGui().getjTextField3().getText());
        double preco = (double) this.getGui().getjFormattedTextFieldValor().getValue();

        // testa valores 
        if ((preco <= 0) || (quantidade <= 0)) {
            // quantidade não informado
            return;
        }

        // calcula total
        double total = preco * quantidade;

        // seta o total
        this.getGui().getjFormattedTextFieldTotal().setText(String.valueOf(total));
    }

    /**
     * *
     * pega a data de hoje
     */
    public void setarData() {
        this.getGui().getjFormattedTextFieldData().setText(ValidarStrings.getDataHoje());
    }

    /**
     *
     */
    @Override
    public void limpar() {
        // limpa os campos
        this.getGui().getjTextField1().setText("");
        this.getGui().getjTextField2().setText("");
        this.getGui().getjTextField3().setText("1");
        this.getGui().getjFormattedTextFieldValor().setText("0");
        this.getGui().getjFormattedTextFieldData().setText(ValidarStrings.getDataHoje());
        this.getGui().getjFormattedTextFieldTotal().setText("0");
        this.getGui().getjLabel7().setText("");
        this.getGui().getjLabel8().setText("");
        this.getGui().getjCheckBox1().setSelected(false);

        // foco
        this.getGui().getjTextField2().requestFocus();
    }

    /**
     *
     */
    @Override
    public void setCampos() {
        // sets básicos
        this.getGui().getjTextField1().setText(String.valueOf(this.getVo().getCodigoCliente()));
        this.getGui().getjTextField2().setText(String.valueOf(this.getVo().getCodigo()));
        this.getGui().getjTextField3().setText(String.valueOf(this.getVo().getQuantidade()));
        this.getGui().getjFormattedTextFieldValor().setText(String.valueOf(this.getVo().getPreco()));
        this.getGui().getjFormattedTextFieldData().setText(String.valueOf(this.getVo().getData()));
        this.getGui().getjCheckBox1().setSelected(this.getVo().isPago());

        // mostrar total
        this.getGui().getjFormattedTextFieldTotal().setText("0");
        this.calcularTotal();

        // focus
        this.getGui().getjTextField3().requestFocus();
    }

    /**
     * prepara tela para consumo a partir da sauna
     */
    public void configurarTela(String nome) {
        // limpar campos
        this.limpar();

        // desabilitar campos e botões
        this.getGui().getjTextField1().setEnabled(false);
        this.getGui().getjButton1().setEnabled(false);
        this.getGui().getjButton7().setEnabled(false);
        this.getGui().getjButton4().setEnabled(false);
        this.getGui().getjButton5().setEnabled(false);
        this.getGui().getjCheckBox1().setEnabled(false);

        // set valores iniciais
        this.getGui().getjTextField1().setText("");
        this.getGui().getjLabel7().setText(nome);

        // focus
        this.getGui().getjTextField2().requestFocus();
    }

    /**
     * *
     * salva o consumo da sauna
     */
    @Override
    public void salvar() {
        // pega valores
        String codProduto = this.getGui().getjTextField2().getText();
        String qtde = this.getGui().getjTextField3().getText();
        String prc = this.getGui().getjFormattedTextFieldValor().getText();

        // valida campos digitados
        if (!this.getBo().validar("Cliente", codProduto, qtde, prc)) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Consumo Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // converte valores
        int codigoCliente = 0;
        int codigoProduto = Integer.parseInt(codProduto);
        int quantidade = Integer.parseInt(qtde);
        double preco = (double) this.getGui().getjFormattedTextFieldValor().getValue();

        // create vo
        String data = Data.retiraMascaraData(this.getGui().getjFormattedTextFieldData().getText());
        this.setVo(new VoConsumo(codigoCliente, data));

        // gerar sequencia
        this.getBo().setVo(this.getVo());
        int sequencia = this.getBo().proximoCodigoLivre();

        // demais sets
        this.getVo().setSequencia(sequencia);
        this.getVo().setCodigo(codigoProduto);
        this.getVo().setQuantidade(quantidade);
        this.getVo().setPreco(preco);
        this.getVo().setPago(false);

        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Consumo da sauna", JOptionPane.ERROR_MESSAGE);
        } else {
            // salvar consumo na tabela de consumo da sauna
            String nome = this.getGui().getjLabel7().getText();
            VoConsumoSauna temp = new VoConsumoSauna(this.getVo(), nome);
            if (!this.getBo().salvarConsumoSauna(temp)) {
                // houve erro
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Cadastrar Consumo da sauna", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.getGui(), "Consumo Sauna Cadastrado com sucesso",
                        "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
                nome = this.getGui().getjLabel7().getText();
                this.limpar();
                this.getGui().getjLabel7().setText(nome);
                // focus
                this.getGui().getjTextField2().requestFocus();
            }
        }
    }

    /**
     *
     */
    @Override
    public void consultaCodigo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * getters and setters
     */
    public VoConsumo getVo() {
        return vo;
    }

    public GuiConsumoSauna getGui() {
        return gui;
    }

    public BoConsumo getBo() {
        return bo;
    }

    public void setVo(VoConsumo vo) {
        this.vo = vo;
    }

    public BoCliente getBoCliente() {
        return boCliente;
    }

    public BoProduto getBoProduto() {
        return boProduto;
    }

    @Override
    public void excluir() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void obterLista() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
