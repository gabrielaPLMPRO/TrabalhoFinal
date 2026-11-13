/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.cadastros;

import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.controller.I_Controller;
import gui.view.cadastros.GuiConsumo;
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
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author dlnotari
 */
public class CoConsumo implements I_Controller {
    // atributos

    private VoConsumo vo;
    private GuiConsumo gui;
    private BoConsumo bo;
    private BoCliente boCliente;
    private BoProduto boProduto;

    // construtor
    public CoConsumo(GuiConsumo gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoConsumo();
        this.bo = new BoConsumo(vo, conexao);
        this.boCliente = new BoCliente(new VoCliente(), conexao);
        this.boProduto = new BoProduto(new VoConsumo(), conexao);
    }

    /**
     * consulta o cliente a partir do código informado
     */
    public void consultarCliente() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField1().getText();
        if (aux.trim().isEmpty()) {
            // codigo não informado
            limpar();
            return;
        }

        // pega código
        int codigo = Data.converteStringToInteiro(this.getGui().getjTextField1().getText());

        // pesquisar
        this.getBoCliente().setErro("");
        if (this.getBoCliente().consultarCodigo(codigo)) {
            // seta código cliente
            this.getVo().setCodigoCliente(codigo);

            // limpar
            this.limpar(codigo, this.getBoCliente().getVo().getNome());
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBoCliente().getErro(),
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            this.limpar();
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
                true, rs, title, DaoTabelas.Cliente);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // seta código do cliente
            int codigo = Integer.parseInt(objeto.get(0).toString());
            this.getVo().setCodigoCliente(codigo);

            // limpar
            this.limpar(codigo, objeto.get(2).toString());
        }
    }

    /**
     * consulta o produto a partir do código informado
     */
    public void consultarProduto() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField2().getText();
        if (aux.trim().isEmpty()) {
            // codigo não informado            
            // não posso colocar chamada de focus aqui porque senão 
            // causa uma chamada recursiva se o método limpar 
            // for pressionado quando o foco estiver no campo produto
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

            // limpar
            this.limpar(codigo, this.getBoProduto().getVo().getDescricao(), this.getBoProduto().getVo().getPreco());
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBoProduto().getErro(),
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            // limpar
            this.limpar(0, "", 0);
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
                true, rs, title, DaoTabelas.Produto);

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
            
            // limpar
            this.limpar(codigo, objeto.get(1).toString(), valor);            
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
    public void salvar() {
        // pega valores
        String codCliente = this.getGui().getjTextField1().getText();
        String codProduto = this.getGui().getjTextField2().getText();
        String qtde = this.getGui().getjTextField3().getText();
        String prc = this.getGui().getjFormattedTextFieldValor().getText();

        // valida campos digitados
        if (!this.getBo().validar(codCliente, codProduto, qtde, prc)) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // converte valores
        int codigoCliente = Integer.parseInt(codCliente);
        int codigoProduto = Integer.parseInt(codProduto);
        int quantidade = Integer.parseInt(qtde);
        double preco = (double) this.getGui().getjFormattedTextFieldValor().getValue();

        // create vo
        String data = Data.retiraMascaraData(this.getGui().getjFormattedTextFieldData().getText());

        // gerar sequencia
        this.setVo(new VoConsumo(codigoCliente, data));
        this.getBo().setVo(this.getVo());
        int sequencia = this.getBo().proximoCodigoLivre();

        // demais sets
        boolean pago = this.getGui().getjCheckBox1().isSelected();
        this.getVo().setSequencia(sequencia);
        this.getVo().setCodigo(codigoProduto);
        this.getVo().setQuantidade(quantidade);
        this.getVo().setPreco(preco);
        this.getVo().setPago(pago);

        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            this.limpar();
        } else {
            JOptionPane.showMessageDialog(this.getGui(), "Consumo Cadastrado com sucesso",
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            this.limpar();
            this.limpar(this.getVo().getCodigoCliente(), this.getGui().getjLabel7().getText());
        }
    }

    /**
     *
     */
    @Override
    public void excluir() {
        // pergunta se quer realmente excluir
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente excluir"
                + " este consumo do cliente?",
                "Cadastrar Consumo", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            this.limpar();
            this.limpar(this.getVo().getCodigoCliente(), this.getGui().getjLabel7().getText());
            return;
        }

        // excluir
        this.getBo().setVo(this.getVo());
        if (this.getBo().excluir()) {
            JOptionPane.showMessageDialog(this.getGui(), "Consumo excluído com sucesso",
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        String nome = this.getGui().getjLabel7().getText();
        this.limpar();
        this.limpar(this.getVo().getCodigoCliente(), nome);
    }

    /**
     *
     */
    @Override
    public void obterLista() {
        // pega valores
        String codCliente = this.getGui().getjTextField1().getText();

        // testar se foi digitado um código
        if (codCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this.getGui(), "Informe o cliente a ser consultado!",
                    "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
            this.limpar();
            this.getGui().getjTextField1().requestFocus();
            return;
        }

        // converte valores
        int codigoCliente = Integer.parseInt(codCliente);

        // create vo
        this.setVo(new VoConsumo(codigoCliente));

        // obter lista de clientes
        this.getBo().setVo(this.getVo());
        ResultSet rs = this.getBo().obterLista();

        // cria objeto controlador da tela de consulta
        String title = "Consulta Consumo de clientes";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.Consumo);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // pega valores
            codigoCliente = Integer.parseInt(objeto.get(0).toString());
            String nome = objeto.get(1).toString();
            String data = objeto.get(2).toString();
            int codigoProduto = Integer.parseInt(objeto.get(3).toString());
            String descricao = objeto.get(4).toString();
            int quantidade = Integer.parseInt(objeto.get(5).toString());
            double valor = Double.parseDouble(objeto.get(6).toString());
            boolean pago = Boolean.parseBoolean(objeto.get(7).toString());
            int sequencia = Integer.parseInt(objeto.get(8).toString());

            // faz uma nova consulta para pegar corretamente o valor do pago
            this.setVo(new VoConsumo(codigoCliente, data, sequencia));
            this.getBo().setVo(vo);
            if (this.getBo().consultarCodigo(0)) {
                this.setVo(this.getBo().getVo());
            } else {
                // cria objeto vo
                this.setVo(new VoConsumo(codigoCliente, data, sequencia, quantidade,
                        pago, codigoProduto, valor));

                // set vo em bo
                this.getBo().setVo(this.getVo());
            }

            // mostrar campos
            this.setCampos(nome, descricao);
        }
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
        this.getGui().getjTextField1().requestFocus();
    }

    /**
     * seta o código cliente e seu nome
     */
    public void limpar(int codigoCliente, String nome) {
        // set campos
        this.getGui().getjTextField1().setText(String.valueOf(codigoCliente));
        this.getGui().getjLabel7().setText(nome);

        // focus
        this.getGui().getjTextField2().requestFocus();
    }

    /**
     * seta o codigo do produto, descricao e valor
     */
    public void limpar(int codigoProduto, String descricao, double valor) {
        // set campos
        this.getGui().getjTextField2().setText(String.valueOf(codigoProduto));
        this.getGui().getjLabel8().setText(descricao);
        this.getGui().getjFormattedTextFieldValor().setText(String.valueOf(valor));

        // focus
        if (codigoProduto == 0) {
            this.getGui().getjTextField2().requestFocus();
        } else {
            this.getGui().getjTextField3().requestFocus();
        }
    }

    /**
     * *
     *
     */
    private void setCampos(String nome, String descricao) {
        this.setCampos();
        this.getGui().getjLabel7().setText(nome);
        this.getGui().getjLabel8().setText(descricao);
        this.getGui().getjTextField3().requestFocus();
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

    public GuiConsumo getGui() {
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
}
