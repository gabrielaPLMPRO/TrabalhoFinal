/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.bd;

import java.sql.SQLException;

/**
 *
 * @author dlnotari
 */
public class DaoEsquema {

    private DaoConsultarBD bd;

    public DaoEsquema(DaoConectarBD conexao) {
        this.bd = new DaoConsultarBD(conexao);
    }

    private void criarTabelaProduto() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de produto
        String sql = "CREATE TABLE produtos"
                + "("
                + "  codigo int NOT NULL,"
                + "  descricao character varying(100) NOT NULL,"
                + "  preco float NOT NULL,"
                + "  unidade character varying(100) NOT NULL,"
                + "  CONSTRAINT pk_produto PRIMARY KEY (codigo)"
                + ")";
        this.getBd().executaSQL(sql);
    }

    private void criarTabelaCliente() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "CREATE TABLE clientes("
            +"codigo int NOT NULL,"
            +"cpf character varying(11),"
            +"nome character varying(50) NOT NULL,"
            +"dataAniversario character varying(8),"
            +"telefone character varying(15),"
            +"CONSTRAINT pk_cliente PRIMARY KEY (codigo)"
            +")";
       this.getBd().executaSQL(sql);
    }

    private void criarTabelaValores() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "CREATE TABLE valores("
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
            +")";
       this.getBd().executaSQL(sql);
    }    
    
    private void criarTabelaConsumo() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "CREATE TABLE consumos("
            +"codigo_cliente int NOT NULL,"
            +"data  int NOT NULL,"
            +"sequencia int NOT NULL,"
            +"codigo_produto int NOT NULL,"    
            +"quantidade int NOT NULL,"
            +"valor float NOT NULL,"
            +"pago boolean NOT NULL,"
            +"CONSTRAINT pk_consumos  PRIMARY KEY (codigo_cliente, data, sequencia),"
            +"CONSTRAINT fk_consumos  FOREIGN KEY (codigo_cliente) REFERENCES clientes(codigo), "
            +"CONSTRAINT fk_consumos1 FOREIGN KEY (codigo_produto) REFERENCES produtos(codigo) "
            +")";
       this.getBd().executaSQL(sql);
    }       
    
    private void criarTabelaPagamento() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "CREATE TABLE pagamentos ("
            +"codigo_cliente int NOT NULL,"
            +"data  int NOT NULL,"
            +"sequencia int NOT NULL,"
            +"valor float NOT NULL,"
            +"pagto_total boolean NOT NULL,"
            +"CONSTRAINT pk_pagamentos  PRIMARY KEY (codigo_cliente, data, sequencia),"
            +"CONSTRAINT fk_pagamentos  FOREIGN KEY (codigo_cliente) REFERENCES clientes(codigo) "
            +")";
       this.getBd().executaSQL(sql);
    }    
    
    private void criarTabelaConsumoSauna() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "CREATE TABLE consumosSauna("
            +"codigo_cliente int NOT NULL,"
            +"data  int NOT NULL,"
            +"sequencia int NOT NULL,"
            +"nome  varchar(100) NOT NULL,"
            +"CONSTRAINT pk_consumosSauna  PRIMARY KEY (codigo_cliente, data, sequencia),"
            +"CONSTRAINT fk_consumosSauna  FOREIGN KEY (codigo_cliente, data, sequencia)"
                + " REFERENCES consumos(codigo_cliente, data, sequencia), "
            +"CONSTRAINT fk_pagamentos  FOREIGN KEY (codigo_cliente) REFERENCES clientes(codigo)"
            +")";
       this.getBd().executaSQL(sql);
    }       
    
    private void criarTabelaHorariosSquash() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "CREATE TABLE horariosSquash("
            +"dia            int        NOT NULL,"
            +"horario        varchar(5) NOT NULL,"
            +"quadra         int        NOT NULL,"
            +"codigo_cliente int        NOT NULL,"
            +"jogo           boolean    NOT NULL,"
            +"CONSTRAINT pk_horariosSquash  PRIMARY KEY (dia, horario, quadra, codigo_cliente),"
            +"CONSTRAINT fk_pagamentos  FOREIGN KEY (codigo_cliente) REFERENCES clientes(codigo)"
            +")";
       this.getBd().executaSQL(sql);
    }     
    
    private void criarTabelaMensalidades() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabela de cliente
        String sql = "create table mensalidades("
              +"codigo_cliente integer NOT NULL,"
              +"dia integer NOT NULL,"
              +"horario character varying(5) NOT NULL,"
              +"quadra integer NOT NULL,"
              +"mes integer NOT NULL,"
              +"ano integer NOT NULL,"
              +"primary key (codigo_cliente, dia, horario, quadra, mes, ano))";
       this.getBd().executaSQL(sql);
    }     
    
    public void criarTabelas() throws SQLException, E_BD, ClassNotFoundException {
        // criar tabelas básicas
        this.criarTabelaProduto();
        System.out.println("Tabela produtos criada!");
        this.criarTabelaCliente();
        System.out.println("Tabela clientes criada!");
        this.criarTabelaValores();
        System.out.println("Tabela valores criada!");
        this.criarTabelaConsumo();
        System.out.println("Tabela consumos criada!");
        this.criarTabelaPagamento();
        System.out.println("Tabela pagamentos criada!");        
        this.criarTabelaConsumoSauna();
        System.out.println("Tabela pagamentos criada!");  
        this.criarTabelaHorariosSquash();
        System.out.println("Tabela horários squash criada!"); 
        this.criarTabelaMensalidades();
        System.out.println("Tabela Mensalidades criada!"); 

        // commit
        this.getBd().executaSQL("commit");
    }
    
    public void excluirTabelas() throws SQLException, E_BD, ClassNotFoundException {
        String sql;
        // excluir tabelas relacionadas
        sql = "drop table mensalidades";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela mensalidades excluida!");  
        
        sql = "drop table horariosSquash";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela horariosSquash excluida!");   
        
        // excluir tabelas relacionadas
        sql = "drop table consumosSauna";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela consumosSauna excluida!");        
        
        sql = "drop table pagamentos";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela pagamentos excluida!");
        
        sql = "drop table consumos";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela consumos excluida!");
                
        // excluir tabelas básicas
        sql = "drop table valores";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela valores excluida!");

        sql = "drop table clientes";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela clientes excluida!");
        
        sql = "drop table produtos";
        this.getBd().executaSQL(sql);
        System.out.println("Tabela produtos excluida!");
        
        // commit
        this.getBd().executaSQL("commit");
    }

    public void inserirValoresDefault() throws SQLException, E_BD, ClassNotFoundException{
        // inserir um produto para saldo de pagamento
        String sql = "insert into produtos values (0, 'Saldo de pagamento', 0, 'un')";
        this.getBd().executaSQL(sql);        
        
        // inserir um produto para sauna
        sql = "insert into produtos values (1, 'Sauna', 0, 'un')";        
        this.getBd().executaSQL(sql);          
        
        // inserir um produto para mensalidade do squash
        sql = "insert into produtos values (-1, 'Mensalidade do Squash', 0, 'un')";        
        this.getBd().executaSQL(sql);  
        
        // inserir um produto para sauna
        sql = "insert into produtos values (-2, 'Primeira Multa por atraso do Squash', 0, 'un')";        
        this.getBd().executaSQL(sql);  
        
        // inserir um produto para sauna
        sql = "insert into produtos values (-3, 'Segunda Multa por atraso do Squash', 0, 'un')";        
        this.getBd().executaSQL(sql);
        
        // inserir um cliente para a sauna
        sql = "insert into clientes values (0, '0000', 'Cliente Sauna', '0', '0')";
        this.getBd().executaSQL(sql);           
    }
    
    public DaoConsultarBD getBd() {
        return bd;
    }

    public void setBd(DaoConsultarBD bd) {
        this.bd = bd;
    }


}
