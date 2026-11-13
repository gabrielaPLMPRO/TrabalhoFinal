/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.cadastros;

import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.controller.I_Controller;
import gui.view.cadastros.GuiPagamento;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.bo.cadastros.BoCliente;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoConsumo;
import model.bo.cadastros.BoPagamento;
import model.vo.cadastros.VoCliente;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoPagamento;
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author dlnotari
 */
public class CoPagamento implements I_Controller {
    // atributos
    private VoPagamento vo;
    private GuiPagamento gui;
    private BoConsumo boConsumo;
    private BoCliente boCliente;
    private BoPagamento bo;

    // construtor
    public CoPagamento(GuiPagamento gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoPagamento();
        this.bo = new BoPagamento(vo, conexao);
        this.boCliente = new BoCliente(new VoCliente(), conexao);
        this.boConsumo = new BoConsumo(new VoConsumo(), conexao);
    }

    /**
     * consulta o cliente a partir do código informado
     */
    public void consultarCliente() {
        // testa se o código foi informado
        String aux = this.getGui().getjTextField1().getText();
        if (aux.trim().isEmpty()) {
            // codigo não informado
            this.limpar();
            return;
        }

        // pega código
        int codigo = Integer.parseInt(aux);

        // pesquisar
        if (this.getBoCliente().consultarCodigo(codigo)) {
            // testa se o nome foi informado
            if (!this.getBoCliente().getVo().getNome().isEmpty()) {
                // seta código cliente
                this.getVo().setCodigoCliente(codigo);

                // seta nome do cliente
                this.getGui().getjLabel7().setText(this.getBoCliente().getVo().getNome());

                // calcular o total a ser pago
                this.calcularTotalCliente();

                // request focus
                this.getGui().getjFormattedTextFieldValorDesconto().requestFocus();
            } else {
                JOptionPane.showMessageDialog(this.getGui(), "Cliente não cadastrado!",
                        "Cadastrar Pagamento", JOptionPane.ERROR_MESSAGE);
                this.limpar();
            }
        } else {
            if (!this.getBoCliente().getErro().isEmpty()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBoCliente().getErro(),
                        "Cadastrar Consumo", JOptionPane.ERROR_MESSAGE);
                this.limpar();
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
        String title = "Consulta lista de clientes para pagamento";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.Cliente);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // seta código do cliente
            this.getVo().setCodigoCliente(Integer.parseInt(objeto.get(0).toString()));
            this.getGui().getjTextField1().setText(objeto.get(0).toString());

            // seta descrição produto
            this.getGui().getjLabel7().setText(objeto.get(2).toString());

            // calcula total
            this.calcularTotalCliente();

            // request focus
            this.getGui().getjFormattedTextFieldValorDesconto().requestFocus();
        }
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
    public void salvar() {
        // pergunta se quer realmente pagar
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente efetuar"
                + " o pagamento deste cliente?\n Um pagamento feito não poderá ser excluído!",
                "Cadastrar Consumo", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            return;
        }

        // pega valores
        int codigoCliente = Integer.parseInt(this.getGui().getjTextField1().getText());
        int data = Integer.parseInt(Data.retiraMascaraData(this.getGui().getjFormattedTextFieldData().getText()));
        double valorConsumo = (double) this.getGui().getjFormattedTextFieldValorConsumo().getValue();
        double desconto = (double) this.getGui().getjFormattedTextFieldValorDesconto().getValue();
        double valorPago = (double) this.getGui().getjFormattedTextFieldTotalPagar().getValue();

        // create vo
        this.setVo(new VoPagamento(codigoCliente, data, valorPago));

        // valida campos digitados
        this.getBo().setVo(this.getVo());
        if (!this.getBo().validar()) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Pagamento", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // verifica desconto
        if ((desconto != 0) && (valorPago == (valorConsumo - desconto))) {
            // pergunta se quer realmente pagar
            resp = JOptionPane.showConfirmDialog(this.getGui(),
                    "Confirma o valor a ser pago com desconto?",
                    "Cadastrar Pagamento", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.NO_OPTION) {
                return;
            }
        } else {
            // verifica se é pagamento parcial
            if (valorPago != valorConsumo) {
                // pergunta se quer realmente pagar
                resp = JOptionPane.showConfirmDialog(this.getGui(),
                        "Existe diferença entre o valor gasto ( R$" + valorConsumo
                        + " ) e o valor que está sendo pago ( R$" + valorPago + " )."
                        + "\nConfirma o pagamento parcial (Um novo registro será "
                        + "lançado no consumo com o saldo do cliente R$ "
                        + (valorConsumo - valorPago) + ")?",
                        "Cadastrar Pagamento", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.NO_OPTION) {
                    return;
                }
            }
        }

        // gerar sequencia
        this.getBo().setVo(this.getVo());
        this.getBo().setDesconto(desconto);
        this.getBo().setValorPago(valorPago);
        this.getVo().setSequencia(this.getBo().proximoCodigoLivre());

        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Pagamento", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), "Pagamento cadastrado com sucesso",
                    "Cadastrar Pagamento", JOptionPane.ERROR_MESSAGE);
            this.limpar();
        }
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
        // pega valores
        String codCliente = this.getGui().getjTextField1().getText();

        if (codCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this.getGui(), "Informe o cliente a ser consultado!",
                    "Pesquisar Consumo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // obter lista de clientes
        this.getBoConsumo().setVo(new VoConsumo(Integer.parseInt(codCliente)));
        ResultSet rs = this.getBoConsumo().obterLista(false, false);

        // cria objeto controlador da tela de consulta
        String title = "Consulta Consumo de clientes";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.Consumo);

        // inibe selecao da tabela
        controllerConsulta.setSelecaoTabela(false);

        // mostrar consulta
        controllerConsulta.consultar();
        
        // foco
        this.getGui().getjTextField1().requestFocus();
    }

    /**
     * calcula o total a ser pago
     */
    private void calcularTotalCliente() {
        // seta os erros para vazio
        this.getBo().setErro("");

        // busca o total a ser pago
        double total = this.getBo().totalPagar();

        // verifica se a erros
        if (!this.getBo().getErro().isEmpty()) {
            //há erros
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Calcular total de consumo de um cliente", JOptionPane.ERROR_MESSAGE);
        } else {
            this.getGui().getjFormattedTextFieldValorConsumo().setText(String.valueOf(total));
        }
    }

    /**
     *
     * @return
     */
    @Override
    public void limpar() {
        this.getGui().getjTextField1().setText("");
        this.getGui().getjFormattedTextFieldValorConsumo().setText("0");
        this.getGui().getjFormattedTextFieldValorDesconto().setText("0");
        this.getGui().getjFormattedTextFieldTotalPagar().setText("0");
        this.getGui().getjFormattedTextFieldData().setText(ValidarStrings.getDataHoje());
        this.getGui().getjLabel7().setText("");
        this.getGui().getjTextField1().requestFocus();
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

    public GuiPagamento getGui() {
        return gui;
    }

    public BoConsumo getBoConsumo() {
        return boConsumo;
    }

    public BoCliente getBoCliente() {
        return boCliente;
    }

    public BoPagamento getBo() {
        return bo;
    }
}
