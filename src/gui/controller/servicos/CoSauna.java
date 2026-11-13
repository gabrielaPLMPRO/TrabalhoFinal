/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.E_BD;
import gui.view.serviços.GuiConsumoSauna;
import gui.view.serviços.GuiPagamentoSauna;
import gui.view.serviços.GuiSauna;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.servicos.BoSauna;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class CoSauna {
    // atributos

    private GuiSauna gui;
    private BoSauna bo;
    private ArrayList<String> clientes;
    private double valorSauna;
    private BoConexao conexao;
    private GuiConsumoSauna guiConsumo;
    private GuiPagamentoSauna guiPagamento;
    private String dataDeHoje;

    // construtor
    public CoSauna(GuiSauna guiSauna, BoConexao conexao) {
        this.gui = guiSauna;
        this.bo = new BoSauna(conexao);
        this.clientes = new ArrayList<>(10);
        this.valorSauna = 0;
        this.conexao = conexao;
        this.dataDeHoje = ValidarStrings.getDataHoje();
        this.guiConsumo = new GuiConsumoSauna(new JFrame(), true, this.getConexao());
    }

    /**
     * *
     * mostra os clientes de uma data específica
     */
    public void carregarClientesSauna() {
        try {
            // pega a data
            String data = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBoxData().getSelectedItem());

            // cria a lista de clientes com o seu consumo
            this.setClientes(this.getBo().obterLista(data));

            // mostra a lista de clientes
            this.getGui().getjList2().setListData(this.getClientes().toArray());

            // limpar área de consumo
            this.getGui().getjList1().setListData(new Object[1]);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "Problemas em recuperar os clientes da sauna!\n"
                    + ex.toString(),
                    "Gerenciamento Sauna!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * mostra o que um cliente consumiu na sauna
     */
    public void mostrarConsumoCliente() {
        try {
            // pega indice selecionado
            int posicao = this.getGui().getjList2().getSelectedIndex();

            // busca a lista de consumos
            ArrayList<String> retorno = this.getBo().buscarConsumoCliente(posicao);

            // testa retorno
            if (retorno.isEmpty()) {
                return;
            }

            // mostra o resultado na tela
            this.getGui().getjList1().setListData(retorno.toArray());
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    this.getBo().getErro() + "\n" + ex.toString(),
                    "Gerenciamento Sauna!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * *
     * adiciona um novo cliente a sauna
     */
    public void adicionarCliente() {
        // pega a data selecionada
        String data = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBoxData().getSelectedItem());

        // testar a data selecionada
        if (!this.getDataDeHoje().equals(data)) {
            // data de hoje é diferente da data selecionada
            // não pode inserir cliente em uma data diferente da de hoje
            JOptionPane.showMessageDialog(this.getGui(), "Não é possível cadastrar "
                    + "um cliente para a data selecionada!\nSomente é possível "
                    + "cadastrar clientes para o dia de hoje!\n"
                    + "Selecione a data corrente!",
                    "Cadastrar Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ler o nome do cliente da sauna
        while (true) {
            // controle do laço
            boolean sair = true;

            // pede o nome do cliente
            String nome = JOptionPane.showInputDialog("Informe o nome do cliente da sauna");
            
            // testa se o nome está em branco
            if (nome == null) {
                // sai do laço
                break;
            }

            // verificar se o nome foi informado
            if (!this.getBo().validarNomeCliente(nome)) {
                // mensagem de erro
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Cadastrar Cliente Sauna", JOptionPane.ERROR_MESSAGE);

                // alterar controle do laço
                sair = false;
            }

            // sai do laco
            if (sair) {
                // adicionar nome
                try {
                    this.getBo().adicionarCliente(nome, this.getValorSauna(), data,
                            this.getGuiConsumo().getCo().getBo());
                } catch (SQLException | E_BD | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this.getGui(),
                            this.getBo().getErro() + "\n" + ex.toString(),
                            "Adicionar Cliente Sauna", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // adicionar nome na lista local
                this.getClientes().add(nome);

                // adiciona na tela
                this.getGui().getjList2().setListData(this.getClientes().toArray());

                // encerra laço
                break;
            }
        }
    }

    /**
     * *
     * exclui um cliente da sauna
     */
    public void excluirCliente() {
        // pega a data selecionada
        String data = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBoxData().getSelectedItem());

        // testar a data selecionada
        if (!this.getDataDeHoje().equals(data)) {
            // data de hoje é diferente da data selecionada
            // não pode inserir cliente em uma data diferente da de hoje
            JOptionPane.showMessageDialog(this.getGui(), "Não é possível excluir "
                    + "um cliente para a data selecionada!\nSomente é possível "
                    + "excluir clientes para o dia de hoje!\n"
                    + "Selecione a data corrente!",
                    "Excluir Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // testa se tem um cliente selecionado
        if (this.getGui().getjList2().isSelectionEmpty()) {
            // nenhum cliente selecionado
            JOptionPane.showMessageDialog(this.getGui(),
                    "É necessário selecionar um cliente para excluir!",
                    "Excluir Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pergunta se quer realmente pagar
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente"
                + " excluir este cliente da sauna?",
                "Excluir Cliente Suna", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            return;
        }

        // pegar indice do cliente no vetor
        int posicao = this.getGui().getjList2().getSelectedIndex();

        // testa se a conta já foi encerrada
        if (this.getBo().isContaClienteFinalizada(posicao)) {
            // mensagem de erro
            JOptionPane.showMessageDialog(this.getGui(),
                    "A conta deste cliente já foi encerrada. Portanto, "
                    + "não é possível excluir as informações deste cliente!",
                    "Controle da Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pega o nome no vetor
        String nome = this.getClientes().get(posicao);

        // excluir da tabela
        try {
            // pegar consumos e adicionar no vetor de consumo
            this.getBo().excluirCliente(nome, ValidarStrings.getDataHoje(), posicao);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    this.getBo().getErro() + "\n" + ex.toString(),
                    "Excluir Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // excluir cliente do vetor
        this.getClientes().remove(nome);

        // remove da tela
        this.getGui().getjList2().setListData(this.getClientes().toArray());
        this.getGui().getjList1().setListData(new Object[1]);
    }

    /**
     * *
     * adiciona um novo cliente a sauna
     */
    public void adicionarConsumoCliente() {
        // pega a data selecionada
        String data = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBoxData().getSelectedItem());

        // testar a data selecionada
        if (!this.getDataDeHoje().equals(data)) {
            // data de hoje é diferente da data selecionada
            // não pode inserir cliente em uma data diferente da de hoje
            JOptionPane.showMessageDialog(this.getGui(), "Não é possível cadastrar "
                    + "o consumo de um cliente para a data selecionada!\nSomente é possível "
                    + "cadastrar consumo de um cliente para o dia de hoje!\n"
                    + "Selecione a data corrente!",
                    "Cadastrar Consumo Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // testa se tem um cliente selecionado
        if (this.getGui().getjList2().isSelectionEmpty()) {
            // nenhum cliente selecionado
            JOptionPane.showMessageDialog(this.getGui(),
                    "É necessário selecionar um cliente para incluir um consumo!",
                    "Cadastrar Consumo Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pegar indice do cliente no vetor
        int posicao = this.getGui().getjList2().getSelectedIndex();

        // testa se a conta já foi encerrada
        if (this.getBo().isContaClienteFinalizada(posicao)) {
            // mensagem de erro
            JOptionPane.showMessageDialog(this.getGui(),
                    "A conta deste cliente já foi encerrada. Portanto, "
                    + "não é possível registrar novos consumos!",
                    "Controle da Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pega o nome no vetor
        String nome = this.getClientes().get(posicao);

        // chamar tela
        this.prepararTelaConsumo(nome);
        try {
            // pegar consumos e adicionar no vetor de consumo
            this.getBo().adicionarConsumoCliente(nome, ValidarStrings.getDataHoje(), posicao);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    this.getBo().getErro() + "\n" + ex.toString(),
                    "Adicionar Consumo Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;

        }

        // atualizar tela com os consumos
        this.mostrarConsumoCliente();
    }

    /**
     * *
     * exclui um cliente da sauna
     */
    public void excluirConsumoCliente() {
        // pega a data selecionada
        String data = ValidarStrings.calendarToData((Calendar) this.getGui().getjComboBoxData().getSelectedItem());

        // testar a data selecionada
        if (!this.getDataDeHoje().equals(data)) {
            // data de hoje é diferente da data selecionada
            // não pode inserir cliente em uma data diferente da de hoje
            JOptionPane.showMessageDialog(this.getGui(), "Não é possível excluir "
                    + "o consumo de um cliente para a data selecionada!\nSomente é possível "
                    + "escluir o consumo de clientes para o dia de hoje!\n"
                    + "Selecione a data corrente!",
                    "Cadastrar Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // testa se tem um cliente selecionado
        if (this.getGui().getjList2().isSelectionEmpty()) {
            // nenhum cliente selecionado
            JOptionPane.showMessageDialog(this.getGui(),
                    "É necessário selecionar um cliente para excluir um consumo!",
                    "Cadastrar Consumo Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // testa se tem um cliente selecionado
        if (this.getGui().getjList1().isSelectionEmpty()) {
            // nenhum cliente selecionado
            JOptionPane.showMessageDialog(this.getGui(),
                    "É necessário selecionar um produto para excluir!",
                    "Excluir Consumo Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pergunta se quer realmente pagar
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente"
                + " excluir este consumo do cliente da sauna?",
                "Excluir Cliente Suna", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            return;
        }

        // pegar indice do cliente no vetor
        int posicaoCliente = this.getGui().getjList2().getSelectedIndex();

        // testa se a conta já foi encerrada
        if (this.getBo().isContaClienteFinalizada(posicaoCliente)) {
            // mensagem de erro
            JOptionPane.showMessageDialog(this.getGui(),
                    "A conta deste cliente já foi encerrada. Portanto, "
                    + "não é possível excluir consumos!",
                    "Controle da Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pegar indice do produto no vetor
        int posicaoConsumo = this.getGui().getjList1().getSelectedIndex();

        // testa se quer excluir o valor da sauna
        if (posicaoConsumo == 0) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "Não é possível excluir este consumo!",
                    "Excluir Consumo Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // testa se quer excluir o total
        if (posicaoConsumo == this.getGui().getjList1().getLastVisibleIndex()) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "Não é possível excluir este consumo!",
                    "Excluir Consumo Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pega o nome no vetor
        String nome = this.getClientes().get(posicaoCliente);

        // excluir da tabela
        try {
            // pegar consumos e adicionar no vetor de consumo
            this.getBo().excluirConsumoCliente(nome, ValidarStrings.getDataHoje(),
                    posicaoCliente, posicaoConsumo);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    this.getBo().getErro() + "\n" + ex.toString(),
                    "Excluir Consumo Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // atualizar tela com os consumos
        this.mostrarConsumoCliente();
    }

    /**
     * *
     * gerar pagamento cliente
     */
    public void gerarPagamentoCliente() {
        // testa se tem um cliente selecionado
        if (this.getGui().getjList2().isSelectionEmpty()) {
            // nenhum cliente selecionado
            JOptionPane.showMessageDialog(this.getGui(),
                    "É necessário selecionar um cliente para gerar o pagamento!",
                    "Pagamento Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pegar indice do cliente no vetor
        int posicao = this.getGui().getjList2().getSelectedIndex();

        // testa se a conta já foi encerrada
        if (this.getBo().isContaClienteFinalizada(posicao)) {
            // mensagem de erro
            JOptionPane.showMessageDialog(this.getGui(),
                    "A conta deste cliente já foi encerrada!",
                    "Controle da Sauna", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pega o nome no vetor
        String nome = this.getClientes().get(posicao);

        // total do cliente a ser pago
        double valor;
        try {
            valor = this.getBo().getValorASerPago(nome, ValidarStrings.getDataHoje(), posicao);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    this.getBo().getErro() + "\n" + ex.toString(),
                    "Gerar Pagamento Cliente Sauna", JOptionPane.ERROR_MESSAGE);
            return;

        }

        // preparar tela pagamento
        this.prepararTelaPagamento(nome, valor);
    }

    /**
     * *
     * prepara tela de consumo
     */
    private void prepararTelaConsumo(String nome) {
        // prepara a tela
        this.getGuiConsumo().getCo().configurarTela(nome);

        // mostrar tela
        this.getGuiConsumo().setVisible(true);
    }

    /**
     * *
     * prepara tela de pagamento
     */
    private void prepararTelaPagamento(String nome, double valor) {
        // cria objeto de consumo
        if (this.getGuiPagamento() == null) {
            this.setGuiPagamento(new GuiPagamentoSauna(new JFrame(), true, this.getConexao()));
        }

        // pegar indice do cliente no vetor
        int posicao = this.getGui().getjList2().getSelectedIndex();

        // prepara a tela
        this.getGuiPagamento().getCo().prepararTelaPagamentoSauna(nome, valor, posicao, this.getBo());

        // mostrar tela
        this.getGuiPagamento().setVisible(true);
    }

    /**
     * *
     *
     * @return
     */
    public void limpar() {
        this.getGui().getjList1().removeAll();
        this.getGui().getjList2().removeAll();
    }

    /**
     * pega o valor a ser pago pela sauna
     *
     * @return
     */
    public double getValorSauna() {
        // pega o valor da sauna
        this.valorSauna = this.getGui().getGui().getController().getGuiValores().getCo().getVo().getValorSauna();
        return valorSauna;
    }

    public GuiSauna getGui() {
        return gui;
    }

    public BoSauna getBo() {
        return bo;
    }

    public ArrayList<String> getClientes() {
        return clientes;
    }

    private BoConexao getConexao() {
        return conexao;
    }

    private GuiConsumoSauna getGuiConsumo() {
        return guiConsumo;
    }

    private GuiPagamentoSauna getGuiPagamento() {
        return guiPagamento;
    }

    private void setGuiPagamento(GuiPagamentoSauna guiPagamento) {
        this.guiPagamento = guiPagamento;
    }

    private void setClientes(ArrayList<String> clientes) {
        this.clientes = clientes;
    }

    private String getDataDeHoje() {
        return dataDeHoje;
    }
}
