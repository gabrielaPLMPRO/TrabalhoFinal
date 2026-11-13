/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.principal;

import gui.controller.cadastros.CoProduto;
import dao.bd.E_BD;
import gui.view.cadastros.GuiCliente;
import gui.view.cadastros.GuiConsumo;
import gui.view.GuiPrincipal;
import gui.view.cadastros.GuiPagamento;
import gui.view.cadastros.GuiProduto;
import gui.view.cadastros.GuiValores;
import gui.view.serviços.GuiSauna;
import gui.view.serviços.GuiHorariosSquash;
import gui.view.serviços.GuiAbertura;
import gui.view.serviços.GuiFichaCliente;
import gui.view.serviços.GuiFluxoCaixa;
import gui.view.serviços.GuiGerarMensalidades;
import gui.view.serviços.GuiResumoMensal;
import gui.view.serviços.GuiSquash;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import model.bo.bd.BoConexao;
import model.bo.bd.BoEsquemaBD;

/**
 *
 * @author dlnotari
 */
public class CoPrincipal {
    // atributos interfaces
    private GuiResumoMensal guiResumoMensal;
    private GuiProduto guiProduto;
    private GuiCliente guiCliente;
    private GuiPrincipal guiPrincipal;
    private GuiValores guiValores;
    private GuiConsumo guiConsumo;
    private GuiPagamento guiPagamento;
    private GuiSauna guiSauna;
    private GuiAbertura guiAbertura;
    private GuiHorariosSquash guiHorariosSquash;
    private GuiSquash guiSquash;
    private GuiFichaCliente guiFichaCliente;
    private GuiFluxoCaixa guiFluxoCaixa;
    private GuiGerarMensalidades guiGerarMensalidades;
    // conexão com o Banco de Dados
    private BoConexao conexao;

    // construtor
    public CoPrincipal(GuiPrincipal guiPrincipal) throws E_BD, ClassNotFoundException, SQLException {
        this.guiPrincipal = guiPrincipal;
        this.conexao = new BoConexao();
    }

    /***
     * configurações iniciais da aplicação
     */
    public void configuracaoInicial() {
        this.conectar();
        this.guiProduto = new GuiProduto(guiPrincipal, true, conexao);
        this.guiCliente = new GuiCliente(guiPrincipal, true, conexao);
        this.guiValores = new GuiValores(guiPrincipal, true, conexao);
        this.guiConsumo = new GuiConsumo(guiPrincipal, true, conexao);
        this.guiPagamento = new GuiPagamento(guiPrincipal, true, conexao);
        this.guiSauna = new GuiSauna(guiPrincipal, conexao);
        this.guiHorariosSquash = new GuiHorariosSquash(guiPrincipal, true, conexao);
        this.guiAbertura = new GuiAbertura();
        this.guiSquash = new GuiSquash(guiPrincipal, conexao);
        this.guiFichaCliente = new GuiFichaCliente(guiPrincipal, true, conexao);
        this.guiFluxoCaixa = new GuiFluxoCaixa(guiPrincipal, true, conexao);
        this.guiGerarMensalidades = new GuiGerarMensalidades(guiPrincipal, true, conexao);
        this.guiResumoMensal = new GuiResumoMensal(guiPrincipal, true, conexao);
        this.montarTabs();
    }

    /***
     * montar tabs da tela principal
     */
    private void montarTabs() {
        // create tab step 0
        this.showImage();
        this.getGuiPrincipal().getjTabbedPane1().addTab("Dante Squash & Sauna", null, this.getGuiAbertura(),
                "Tela de Abertura!");
        this.getGuiPrincipal().getjTabbedPane1().setMnemonicAt(0, KeyEvent.VK_0);
        this.getGuiPrincipal().getjTabbedPane1().setFont(new java.awt.Font("Arial", 1, 18));

        // create tab step 1
        this.getGuiPrincipal().getjTabbedPane1().addTab("1. Sauna", null, this.getGuiSauna(),
                "Controle de uso e consumo da Sauna!");
        this.getGuiPrincipal().getjTabbedPane1().setMnemonicAt(1, KeyEvent.VK_1);

        // create tab step 1
        this.getGuiPrincipal().getjTabbedPane1().addTab("2. Squash", null, this.getGuiSquash(),
                "Controle de uso e consumo do Squash!");
        this.getGuiPrincipal().getjTabbedPane1().setMnemonicAt(2, KeyEvent.VK_2);

        //The following line enables to use scrolling tabs.
        this.getGuiPrincipal().getjTabbedPane1().setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        // carrega os clientes de hoje da sauna
        this.getGuiSauna().getCo().carregarClientesSauna();
        
        // esconder menu banco de dados
        this.getGuiPrincipal().getjMenu5().setVisible(false);
        
        // carregar grade do squash
        this.getGuiSquash().getCo().configuracaoInicial();
    }

    /**
     * chama tela de produto
     */
    public void mostrarTelaProduto() {
        this.getGuiProduto().getCo().limpar();
        this.getGuiProduto().setVisible(true);
    }

    /**
     * chama tela de cliente
     */
    public void mostrarTelaCliente() {
        this.getGuiCliente().getCo().limpar();
        this.getGuiCliente().setVisible(true);
    }

    /**
     * chama tela de valores
     */
    public void mostrarTelaValores() {
        this.getGuiValores().getCo().limpar();
        this.getGuiValores().setVisible(true);
    }

    /**
     * chama tela de consumo
     */
    public void mostrarTelaConsumo() {
        this.getGuiConsumo().getCo().limpar();
        this.getGuiConsumo().setVisible(true);
    }

    /**
     * chama tela de pagamento
     */
    public void mostrarTelaPagamento() {
        this.getGuiPagamento().getCo().limpar();
        this.getGuiPagamento().setVisible(true);
    }

    /**
     * chama tela de horarios do squash
     */
    public void mostrarTelaHorariosSquash() {
        this.getGuiHorariosSquash().getCo().limpar();
        this.getGuiHorariosSquash().setVisible(true);
    }

    /**
     * chama tela de ficha do cliente
     */
    public void mostrarTelaFichaCliente() {
        this.getGuiFichaCliente().getCo().limpar();
        this.getGuiFichaCliente().setVisible(true);
    }

    /**
     * chama tela de horarios do squash
     */
    public void mostrarTelaFluxoCaixa() {
        this.getGuiFluxoCaixa().getCo().limpar();
        this.getGuiFluxoCaixa().setVisible(true);
    }

    /**
     * chama tela de horarios do squash
     */
    public void mostrarTelaGerarMensalidades() {
        this.getGuiGerarMensalidades().getCo().limpar();
        this.getGuiGerarMensalidades().setVisible(true);
    }
    
    /**
     * faz backup das tabelas
     */
    public void backupTabelas(){
        // cria objeto
        CoBackupTabelas backup = new CoBackupTabelas(this.getGuiPrincipal(), this.getConexao());
        
        // gerar backup
        backup.salvarTabelas();
    }
    
    /**
     * mostra o resumo mensal
     */
    public void mostrarResumoMensal(){
        this.getGuiResumoMensal().getCo().limpar();
        this.getGuiResumoMensal().setVisible(true);        
    }

    public GuiResumoMensal getGuiResumoMensal() {
        return guiResumoMensal;
    }
    

    /***
     * show workflow image
     * @return
     */
    private void showImage() {
        // define image path
        String path = "/gui/logo.jpg";

        // create icon object
        ImageIcon icon = new ImageIcon(getClass().getResource(path));

        // set image to label
        this.getGuiAbertura().getjButton1().setIcon(icon);
    }

    /**
     * *
     * conectar
     */
    private void conectar() {
        this.conexao = new BoConexao();
        try {
            this.conexao.conectar();
        } catch (E_BD | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CoProduto.class.getName()).log(Level.SEVERE, null, ex);
            // houve erro
            JOptionPane.showMessageDialog(this.getGuiPrincipal(),
                    "Não foi possível estabelecer uma conexão com o banco de dados!"
                    + "\nO sistema será encerrado",
                    "Conexão com o Banco de Dados", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * desconectar
     */
    public void desconectar() {
        try {
            // encerrar conexão
            this.conexao.desconectar();
            JOptionPane.showConfirmDialog(this.getGuiPrincipal(),
                    "Conexão com o banco de dados foi encerrada com sucesso",
                    "Conexão com o SGBD", JOptionPane.DEFAULT_OPTION);
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(this.getGuiPrincipal(),
                    "Conexão com o banco de dados não foi encerrada com sucesso",
                    "Conexão com o SGBD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * *
     * criar banco de dados
     *
     * @return
     */
    public void criarBancoDados() {
        BoEsquemaBD esquema = new BoEsquemaBD(this.getConexao());
        
        // cria tabela
        if (esquema.criarTabelas()) {
            JOptionPane.showConfirmDialog(this.getGuiPrincipal(),
                    "Banco de dados criado com sucesso",
                    "Conexão com o SGBD", JOptionPane.DEFAULT_OPTION);
        }
        
        // insere valores default
        if (esquema.criarValoresDefault()) {
            JOptionPane.showConfirmDialog(this.getGuiPrincipal(),
                    "Banco de dados criado com sucesso",
                    "Conexão com o SGBD", JOptionPane.DEFAULT_OPTION);
        }
        
    }

    /**
     * *
     * excluir banco de dados
     *
     * @return
     */
    public void excluirBancoDados() {
        BoEsquemaBD esquema = new BoEsquemaBD(this.getConexao());
        if (esquema.excluirTabelas()) {
            JOptionPane.showConfirmDialog(this.getGuiPrincipal(),
                    "Banco de dados excluído com sucesso",
                    "Conexão com o SGBD", JOptionPane.DEFAULT_OPTION);
        }
    }

    public GuiProduto getGuiProduto() {
        return guiProduto;
    }

    public GuiCliente getGuiCliente() {
        return guiCliente;
    }

    public GuiPrincipal getGuiPrincipal() {
        return guiPrincipal;
    }

    public GuiValores getGuiValores() {
        return guiValores;
    }

    public GuiConsumo getGuiConsumo() {
        return guiConsumo;
    }

    public GuiPagamento getGuiPagamento() {
        return guiPagamento;
    }

    public GuiSauna getGuiSauna() {
        return guiSauna;
    }

    public GuiAbertura getGuiAbertura() {
        return guiAbertura;
    }

    public GuiHorariosSquash getGuiHorariosSquash() {
        return guiHorariosSquash;
    }

    public GuiSquash getGuiSquash() {
        return guiSquash;
    }

    public GuiFichaCliente getGuiFichaCliente() {
        return guiFichaCliente;
    }

    public GuiFluxoCaixa getGuiFluxoCaixa() {
        return guiFluxoCaixa;
    }

    public GuiGerarMensalidades getGuiGerarMensalidades() {
        return guiGerarMensalidades;
    }

    private BoConexao getConexao() {
        return conexao;
    }
    
    
}
