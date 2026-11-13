/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.principal;

import gui.view.GuiPrincipal;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.bd.BoBackupTabelas;

/**
 *
 * @author dlnotari
 */
public class CoBackupTabelas {
    // atributos

    private GuiPrincipal gui;
    private BoBackupTabelas bo;

    // construtor
    public CoBackupTabelas(GuiPrincipal gui, BoConexao conexao) {
        this.gui = gui;
        this.bo = new BoBackupTabelas(conexao);
    }

    /**
     * salvar tabelas
     */
    public void salvarTabelas() {
        // variavel local
        String texto = "";
        try {
            // salva tabela produtos
            if (!this.getBo().salvarTabelaProdutos()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto = "Tabela produtos exportada com sucesso!\n";

            // salva tabela clientes            
            if (!this.getBo().salvarTabelaClientes()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela clientes exportada com sucesso!\n";

            // salva tabela valores            
            if (!this.getBo().salvarTabelaValores()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela valores exportada com sucesso!\n";

            // salva tabela consumos
            if (!this.getBo().salvarTabelaConsumos()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela consumos exportada com sucesso!\n";            

            // salva tabela consumos
            if (!this.getBo().salvarTabelaConsumosSauna()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela consumosSauna exportada com sucesso!\n";

            // salva tabela pagamentos
            if (!this.getBo().salvarTabelaPagamentos()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela pagamentos exportada com sucesso!\n";

            // salva tabela horariosSquash
            if (!this.getBo().salvarTabelaHorariosSquash()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela horariossquash exportada com sucesso!\n";            

            // salva tabela mensalidade
            if (!this.getBo().salvarTabelaMensalidade()) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            }
            else texto += "Tabela Mensalidade exportada com sucesso!\n";               
            
            // mensagem
            JOptionPane.showMessageDialog(this.getGui(), texto,
                    "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro() + "\n" + ex.toString(),
                    "Backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
    }

    // getters and setters
    public BoBackupTabelas getBo() {
        return bo;
    }

    public GuiPrincipal getGui() {
        return gui;
    }
}
