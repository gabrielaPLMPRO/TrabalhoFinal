/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.bd;

import dao.bd.DaoEsquema;
import dao.bd.E_BD;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author daniel
 */
public class BoEsquemaBD {
    // atributos

    private DaoEsquema dao;
    private BoConexao conexao;

    // construtor
    public BoEsquemaBD() {
        this.conexao = new BoConexao();
    }

    // construtor
    public BoEsquemaBD(BoConexao conexao) {
        this.conexao = conexao;
    }

    // getter and setter
    private BoConexao getConexao() {
        return conexao;
    }

    private DaoEsquema getDao() {
        return dao;
    }

    /**
     * conectar
     */
    public void conectar() {
        try {
            // conectar
            this.getConexao().conectar();
            System.out.println("conectou");
        } catch (E_BD | ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao conectar no banco de dados!"
                    + ex.toString(),
                    "Manipular tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * desconectar do banco de dados
     */
    public void desconectar() {
        try {
            // desconectar
            this.getConexao().desconectar();
            System.out.println("desconectou");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao desconectar no banco de dados!"
                    + ex.toString(),
                    "Manipular tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * cria tabelas no banco de dados
     *
     * @return
     */
    public boolean criarTabelas() {
        try {
            // criar objeto dao
            this.dao = new DaoEsquema(this.getConexao().getConexao());
            
            // criar tabelas
            this.getDao().criarTabelas();

            // retorna
            return true;
        } catch (E_BD | ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao criar as tabelas no banco de dados!"
                    + ex.toString(),
                    "Criação de Tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
        // retorna
        return false;
    }

    /**
     * cria valores default no banco de dados
     *
     * @return
     */
    public boolean criarValoresDefault() {
        try {
            // criar objeto dao
            this.dao = new DaoEsquema(this.getConexao().getConexao());            
            
            // criar inserts
            this.getDao().inserirValoresDefault();

            // retorna
            return true;
        } catch (E_BD | ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao criar as tabelas no banco de dados!"
                    + ex.toString(),
                    "Criação de Tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
        // retorna
        return false;
    }

    /**
     * excluir tabelas banco de dados
     *
     * @return
     */
    public boolean excluirTabelas() {
        try {
            // criar objeto dao
            this.dao = new DaoEsquema(this.getConexao().getConexao());            
            
            // criar tabelas
            this.getDao().excluirTabelas();

            // retorna
            return true;
        } catch (E_BD | ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao excluir as tabelas no banco de dados!"
                    + ex.toString(),
                    "Exclusão de Tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
        // retorna
        return false;
    }

    public static void main(String args[]) {
        BoEsquemaBD esquema = new BoEsquemaBD();
        esquema.conectar();
        esquema.excluirTabelas();
        esquema.criarTabelas();
        esquema.criarValoresDefault();
        esquema.desconectar();
    }
}
