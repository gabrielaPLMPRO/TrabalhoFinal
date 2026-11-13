/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.bd;

import dao.bd.DaoBackupTabelas;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.bo.cadastros.BoCliente;
import model.bo.cadastros.BoConsumo;
import model.bo.cadastros.BoPagamento;
import model.bo.cadastros.BoProduto;
import model.bo.cadastros.BoValores;
import model.bo.servicos.BoHorarioSquash;
import model.bo.servicos.BoMensalidade;
import model.bo.servicos.BoSauna;
import model.vo.cadastros.VoCliente;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoPagamento;
import model.vo.cadastros.VoProduto;
import model.vo.cadastros.VoValores;
import model.vo.servicos.VoHorarioSquash;
import model.vo.servicos.VoMensalidade;

/**
 *
 * @author dlnotari
 */
public class BoBackupTabelas {
    // atributos
    private DaoBackupTabelas dao;
    private BoConexao conexao;
    private String    erro;
    
    // construtor
    public BoBackupTabelas(BoConexao conexao) {
        this.dao = new DaoBackupTabelas();
        this.conexao = conexao;
    }

    /***
     * salvar dados da tabela produtos
     * @return 
     */
    public boolean salvarTabelaProdutos() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoProduto bo = new BoProduto(new VoProduto(), this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista(true);
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de produtos está vazia!");
            return false;
        }
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_produtos.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaProdutos(rs, file);
        
        // retorna
        return true;        
    }
    
    /***
     * salvar dados da tabela clientes
     * @return 
     */
    public boolean salvarTabelaClientes() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoCliente bo = new BoCliente(new VoCliente(), this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista(true);
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de clientes está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_clientes.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaClientes(rs, file);
        
        // retorna
        return true;        
    }    
    
    /***
     * salvar dados da tabela valores
     * @return 
     */
    public boolean salvarTabelaValores() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoValores bo = new BoValores(new VoValores(), this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista();
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de valores está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_valores.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaValores(rs, file);
        
        // retorna
        return true;        
    }     
    
    /***
     * salvar dados da tabela consumos
     * @return 
     */
    public boolean salvarTabelaConsumos() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoConsumo bo = new BoConsumo(new VoConsumo(), this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista(true);
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de consumos está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_consumos.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaConsumos(rs, file);
        
        // retorna
        return true;        
    }     
    
    /***
     * salvar dados da tabela consumosSauna
     * @return 
     */
    public boolean salvarTabelaConsumosSauna() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoSauna bo = new BoSauna(this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista(1);
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de consumosSauna está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_consumossauna.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaConsumosSauna(rs, file);
        
        // retorna
        return true;        
    }     

    /***
     * salvar dados da tabela pagamentos
     * @return 
     */
    public boolean salvarTabelaPagamentos() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoPagamento bo = new BoPagamento(new VoPagamento(), this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista(true);
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de pagamentos está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_pagamentos.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaPagamentos(rs, file);
        
        // retorna
        return true;        
    }     

    
    /***
     * salvar dados da tabela horariosSquash
     * @return 
     */
    public boolean salvarTabelaHorariosSquash() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoHorarioSquash bo = new BoHorarioSquash(new VoHorarioSquash(), this.getConexao(), null);
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista(true);
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de horariosSquash está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_horariossquash.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaHorariosSquash(rs, file);
        
        // retorna
        return true;        
    }     
    
    /***
     * salvar dados da tabela horariosSquash
     * @return 
     */
    public boolean salvarTabelaMensalidade() throws FileNotFoundException, SQLException, IOException{
        // gera timezone
        long timeZone = java.util.Calendar.getInstance().getTimeInMillis();
        
        // cria objeto 
        BoMensalidade bo = new BoMensalidade(new VoMensalidade(), this.getConexao());
        
        // obtem a lista de produtos
        ResultSet rs = bo.obterLista();
        
        // testar resultado 
        if (rs == null){
            // não há dados a serem salvos
            this.setErro("tabela de mensalidade está vazia!");
            return false;
        }        
        
        // testa se houve erro
        if (!bo.getErro().isEmpty()) {
            this.setErro(bo.getErro());
            return false;
        }
        
        // criar arquivo
        File file = new File(timeZone+"_mensalidade.txt");        
        
        // salva as informações
        this.getDao().salvarTabelaMensalidade(rs, file);
        
        // retorna
        return true;        
    }      
    
    // getters and setters
    private DaoBackupTabelas getDao() {
        return dao;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}
