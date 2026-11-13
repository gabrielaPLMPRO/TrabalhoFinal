/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.bd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dlnotari
 */
public class DaoBackupTabelas {

    public DaoBackupTabelas() {
    }
    
    /**
     * "CREATE TABLE produtos"
                + "("
                + "  codigo int NOT NULL,"
                + "  descricao character varying(100) NOT NULL,"
                + "  preco float NOT NULL,"
                + "  unidade character varying(100) NOT NULL,"
                + "  CONSTRAINT pk_produto PRIMARY KEY (codigo)"
                + ")";
     * @param rs
     * @param file 
     */
    public void salvarTabelaProdutos(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona código
            aux.append(rs.getString("codigo"));
            aux.append(";");
            
            // adiciona descricao
            aux.append(rs.getString("descricao"));
            aux.append(";");

            // adiciona preco
            aux.append(rs.getString("preco"));
            aux.append(";");
            
            // adiciona unidade
            aux.append(rs.getString("unidade"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }
    
    /**
     * CREATE TABLE clientes("
            +"codigo int NOT NULL,"
            +"cpf character varying(11),"
            +"nome character varying(50) NOT NULL,"
            +"dataAniversario character varying(8),"
            +"telefone character varying(15),"
            +"CONSTRAINT pk_cliente PRIMARY KEY (codigo)"
            +")
     * @param rs
     * @param file 
     */
    public void salvarTabelaClientes(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona código
            aux.append(rs.getString("codigo"));
            aux.append(";");
            
            // adiciona cpf
            aux.append(rs.getString("cpf"));
            aux.append(";");

            // adiciona dataAniversario
            aux.append(rs.getString("dataAniversario"));
            aux.append(";");

            // adiciona telefone
            aux.append(rs.getString("telefone"));
            aux.append(";");

            // adiciona nome
            aux.append(rs.getString("nome"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }  
    
    /**
     * "CREATE TABLE valores("
            +"codigo int NOT NULL,"
            +"valorSauna float NOT NULL,"
            +"valorAulaSquash float NOT NULL,"
            +"vencimento1 int NOT NULL,"    
            +"valorSquashMensal1 float NOT NULL,"
            +"vencimento2 int,"
            +"valorSquashMensal2 float,"
            +"vencimento3 int,"                
            +"valorSquashMensal3 float,"
            +"CONSTRAINT pk_valores PRIMARY KEY (codigo)"
            +")"
     * @param rs
     * @param file 
     */
    public void salvarTabelaValores(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona código
            aux.append(rs.getString("codigo"));
            aux.append(";");
            
            // adiciona valorSauna
            aux.append(rs.getString("valorSauna"));
            aux.append(";");

            // adiciona valorAulaSquash
            aux.append(rs.getString("valorAulaSquash"));
            aux.append(";");
            
            // adiciona valorSquashMensal1
            aux.append(rs.getString("valorSquashMensal1"));
            aux.append(";");
            
            // adiciona valorSquashMensal2
            aux.append(rs.getString("valorSquashMensal2"));
            aux.append(";");
            
            // adiciona valorSquashMensal3
            aux.append(rs.getString("valorSquashMensal3"));
            aux.append(";");
            
            // adiciona vencimento1
            aux.append(rs.getString("vencimento1"));
            aux.append(";");

            // adiciona vencimento2
            aux.append(rs.getString("vencimento2"));
            aux.append(";");

            // adiciona vencimento3
            aux.append(rs.getString("vencimento3"));
            aux.append(";");

            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }    
    
    /**
     * cREATE TABLE consumos("
            +"codigo_cliente int NOT NULL,"
            +"data  int NOT NULL,"
            +"sequencia int NOT NULL,"
            +"codigo_produto int NOT NULL,"    
            +"quantidade int NOT NULL,"
            +"valor float NOT NULL,"
            +"pago boolean NOT NULL,"
     * @param rs
     * @param file 
     */
    public void salvarTabelaConsumos(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona codigo_cliente
            aux.append(rs.getString("codigo_cliente"));
            aux.append(";");
            
            // adiciona data
            aux.append(rs.getString("data"));
            aux.append(";");

            // adiciona sequencia
            aux.append(rs.getString("sequencia"));
            aux.append(";");
            
            // adiciona codigo_produto
            aux.append(rs.getString("codigo_produto"));
            aux.append(";");

            // adiciona quantidade
            aux.append(rs.getString("quantidade"));
            aux.append(";");
            
            // adiciona valor
            aux.append(rs.getString("valor"));
            aux.append(";");

            // adiciona pago
            aux.append(rs.getString("pago"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }    

    /**
     * CREATE TABLE consumosSauna("
            +"codigo_cliente int NOT NULL,"
            +"data  int NOT NULL,"
            +"sequencia int NOT NULL,"
            +"nome  varchar(100) NOT NULL,"
     * @param rs
     * @param file 
     */
    public void salvarTabelaConsumosSauna(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona codigo_cliente
            aux.append(rs.getString("codigo_cliente"));
            aux.append(";");
            
            // adiciona data
            aux.append(rs.getString("data"));
            aux.append(";");

            // adiciona sequencia
            aux.append(rs.getString("sequencia"));
            aux.append(";");
            
            // adiciona nome
            aux.append(rs.getString("nome"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }    
    
    /**
     * CREATE TABLE pagamentos ("
            +"codigo_cliente int NOT NULL,"
            +"data  int NOT NULL,"
            +"sequencia int NOT NULL,"
            +"valor float NOT NULL,"
            +"pagto_total boolean NOT NULL,"
     * @param rs
     * @param file 
     */
    public void salvarTabelaPagamentos(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona codigo_cliente
            aux.append(rs.getString("codigo_cliente"));
            aux.append(";");
            
            // adiciona data
            aux.append(rs.getString("data"));
            aux.append(";");

            // adiciona sequencia
            aux.append(rs.getString("sequencia"));
            aux.append(";");
            
            // adiciona valor
            aux.append(rs.getString("valor"));
            aux.append(";");

            // adiciona pagto_total
            aux.append(rs.getString("pagto_total"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }    

    /**
     * CREATE TABLE horariosSquash("
            +"dia            int        NOT NULL,"
            +"horario        varchar(5) NOT NULL,"
            +"quadra         int        NOT NULL,"
            +"codigo_cliente int        NOT NULL,"
            +"jogo           boolean    NOT NULL
     * @param rs
     * @param file 
     */
    public void salvarTabelaHorariosSquash(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona codigo_cliente
            aux.append(rs.getString("codigo_cliente"));
            aux.append(";");

            // adiciona dia
            aux.append(rs.getString("dia"));
            aux.append(";");

            // adiciona quadra
            aux.append(rs.getString("quadra"));
            aux.append(";");

            // adiciona horario
            aux.append(rs.getString("horario"));
            aux.append(";");
            
            // adiciona jogo
            aux.append(rs.getString("jogo"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }    
    
  /**  
        String sql = "create table mensalidades("
              +"codigo_cliente integer NOT NULL,"
              +"dia integer NOT NULL,"
              +"horario character varying(5) NOT NULL,"
              +"quadra integer NOT NULL,"
              +"mes integer NOT NULL,"
              +"ano integer NOT NULL,"
              +"primary key (codigo_cliente, dia, horario, quadra, mes, ano),"
              +"foreign key (dia, horario, quadra, codigo_cliente) references horariossquash (dia, horario, quadra, codigo_cliente))";
     */
   public void salvarTabelaMensalidade(ResultSet rs, File file) throws FileNotFoundException, SQLException, IOException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // percorre dados
        while (rs.next()) {
            // cria variável auxiliar
            StringBuilder aux = new StringBuilder();
            
            // adiciona codigo_cliente
            aux.append(rs.getString("codigo_cliente"));
            aux.append(";");

            // adiciona dia
            aux.append(rs.getString("dia"));
            aux.append(";");

            // adiciona quadra
            aux.append(rs.getString("quadra"));
            aux.append(";");

            // adiciona horario
            aux.append(rs.getString("horario"));
            aux.append(";");
            
            // adiciona mes
            aux.append(rs.getString("mes"));
            aux.append(";");
            
            // adiciona ano
            aux.append(rs.getString("ano"));
            aux.append(";");
            
            // adiciona quebra de linha
            aux.append("\n");
            
            // salva no arquivo
            raf.writeBytes(aux.toString());
        }
        
        // fecha arquivo
        raf.close();        
    }      
}
