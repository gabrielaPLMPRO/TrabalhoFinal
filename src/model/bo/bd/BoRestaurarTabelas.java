/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.bd;

import dao.bd.DaoRestaurarTabelas;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class BoRestaurarTabelas {
    // atributos
    private BoConexao conexao;
    private DaoRestaurarTabelas dao;
    private BoEsquemaBD bo;

    // construtor
    public BoRestaurarTabelas() {
        this.conexao = new BoConexao();
        this.bo = new BoEsquemaBD(conexao);
        this.dao = new DaoRestaurarTabelas(conexao);
    }

    // controle de execução
    public void controle() {
        try {
            // conectar
            this.getConexao().conectar();
            System.out.println("conectou");            

            // excluir tabelas
            this.getBo().excluirTabelas();

            // criar tabelas
            this.getBo().criarTabelas();
            
            // criar valores default
            this.getBo().criarValoresDefault();

            // criar arquivo
            File file = new File("produtos.txt");

            // variavel mensagem
            String msg;

            // recuperar tabela produtos
            this.getDao().recuperarTabelaProdutos(file);
            msg = "Tabela produtos recuperada com sucesso!\n";

            // recuperar tabela clientes
            file = new File("clientes.txt");
            this.getDao().recuperarTabelaClientes(file);
            msg += "Tabela clientes recuperada com sucesso!\n";

            // recuperar tabela valores
            file = new File("valores.txt");
            this.getDao().recuperarTabelaValores(file);
            msg += "Tabela valores recuperada com sucesso!\n";

            // recuperar tabela consumos
            file = new File("consumos.txt");
            this.getDao().recuperarTabelaConsumos(file);
            msg += "Tabela consumos recuperada com sucesso!\n";

            // recuperar tabela consumosSauna
            file = new File("consumossauna.txt");
            this.getDao().recuperarTabelaConsumosSauna(file);
            msg += "Tabela consumosSauna recuperada com sucesso!\n";

            // recuperar tabela pagamentos
            file = new File("pagamentos.txt");
            this.getDao().recuperarTabelaPagamentos(file);
            msg += "Tabela pagamentos recuperada com sucesso!\n";

            // recuperar tabela horariosSquash
            file = new File("horariossquash.txt");
            this.getDao().recuperarTabelaHorariosSquash(file);
            msg += "Tabela horariosSquash recuperada com sucesso!\n";

            // recuperar tabela horariosSquash
            file = new File("mensalidade.txt");
            this.getDao().recuperarTabelaMensalidade(file);
            msg += "Tabela mensalidade recuperada com sucesso!\n";            
            
            // mensagem
            JOptionPane.showMessageDialog(null, msg,
                    "Restaurar backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
            
            // desconectar
            this.getConexao().desconectar();
            System.out.println("desconectou");            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao restaurar o banco de dados!\n" + ex.toString(),
                    "Restaurar backup do Banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
    }

    // getters and setters
    private DaoRestaurarTabelas getDao() {
        return dao;
    }

    private BoEsquemaBD getBo() {
        return bo;
    }

    private BoConexao getConexao() {
        return conexao;
    }
    
    public static void main(String args[]){
        new BoRestaurarTabelas().controle();
    }
}
