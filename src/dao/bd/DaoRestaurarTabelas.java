    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.bd;

import dao.cadastros.DaoCliente;
import dao.cadastros.DaoConsumo;
import dao.cadastros.DaoPagamento;
import dao.cadastros.DaoProduto;
import dao.cadastros.DaoValores;
import dao.servicos.DaoConsumoSauna;
import dao.servicos.DaoHorariosSquash;
import dao.servicos.DaoMensalidade;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import model.bo.bd.BoConexao;
import model.vo.cadastros.VoCliente;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoPagamento;
import model.vo.cadastros.VoProduto;
import model.vo.cadastros.VoValores;
import model.vo.servicos.VoConsumoSauna;
import model.vo.servicos.VoHorarioSquash;
import model.vo.servicos.VoMensalidade;

/**
 *
 * @author Daniel
 */
public class DaoRestaurarTabelas {
    // atributos
    private BoConexao conexao;
    
    // construtor
    public DaoRestaurarTabelas(BoConexao conexao) {
        this.conexao = conexao;
    }
    
    
    /**
     * @param file 
     */
    public void recuperarTabelaProdutos(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoProduto dao = new DaoProduto(null, this.getConexao());
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");
            
            // criar objeto
            VoProduto vo = new VoProduto(Integer.parseInt(valores[0]),
                    valores[1], Double.parseDouble(valores[2]), valores[3]);
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }
    
    /**
     * @param file 
     */
    public void recuperarTabelaClientes(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoCliente dao = new DaoCliente(null, this.getConexao());
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");
            
            // criar objeto
            VoCliente vo = new VoCliente(Integer.parseInt(valores[0]),
                    valores[1], valores[4], valores[2], valores[3]);
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }  
    
    /**
     * @param file 
     */
    public void recuperarTabelaValores(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoValores dao = new DaoValores(null, this.getConexao());
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");
            
            // criar objeto
            VoValores vo = new VoValores(Integer.parseInt(valores[0]),
                                         Double.parseDouble(valores[1]),
                                         Double.parseDouble(valores[2]),
                                         Double.parseDouble(valores[3]),
                                         Double.parseDouble(valores[4]),
                                         Double.parseDouble(valores[5]),
                                         Integer.parseInt(valores[6]),
                                         Integer.parseInt(valores[7]),
                                         Integer.parseInt(valores[8]));
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }    
    
    /**
     * @param file 
     */
    public void recuperarTabelaConsumos(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoConsumo dao = new DaoConsumo(this.getConexao(), null);
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");
            
            // converte boolean
            boolean valor = false;
            if (valores[6].equalsIgnoreCase("t")){
                valor = true;
            }
            
            // criar objeto
            VoConsumo vo = new VoConsumo(Integer.parseInt(valores[0]),
                                         valores[1], 
                                         Integer.parseInt(valores[2]),
                                         Integer.parseInt(valores[3]),
                                         Integer.parseInt(valores[4]),
                                         Double.parseDouble(valores[5]), 
                                         valor);
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }    

    /**
     * @param file 
     */
    public void recuperarTabelaConsumosSauna(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoConsumoSauna dao = new DaoConsumoSauna(this.getConexao());
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");
            
            // criar objeto
            VoConsumoSauna vo = new VoConsumoSauna(Integer.parseInt(valores[0]),
                                         valores[1], 
                                         Integer.parseInt(valores[2]),
                                         valores[3]);
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }    
    
    /**
     * @param file 
     */
    public void recuperarTabelaPagamentos(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoPagamento dao = new DaoPagamento(this.getConexao(), null);
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");
            
            // converte boolean
            boolean valor = false;
            if (valores[4].equalsIgnoreCase("t")){
                valor = true;
            }
            
            // criar objeto
            VoPagamento vo = new VoPagamento(Integer.parseInt(valores[0]),
                                             Integer.parseInt(valores[1]),
                                             Integer.parseInt(valores[2]),
                                             Double.parseDouble(valores[3]),
                                             valor);
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }    

    /**
     * @param file 
     */
    public void recuperarTabelaHorariosSquash(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoHorariosSquash dao = new DaoHorariosSquash(null, this.getConexao());
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");

            // converte boolean
            boolean valor = false;
            if (valores[4].equalsIgnoreCase("t")){
                valor = true;
            }
            
            
            // criar objeto
            VoHorarioSquash vo = new VoHorarioSquash(Integer.parseInt(valores[0]),
                                             Integer.parseInt(valores[1]),
                                             Integer.parseInt(valores[2]),
                                             valores[3],
                                             valor);
            
            // setar vo
            dao.setVo(vo);
            
            // salvar
            dao.salvar(false);
        }
    }    

    /**
     * @param file 
     */
    public void recuperarTabelaMensalidade(File file) throws FileNotFoundException, SQLException, IOException, E_BD, ClassNotFoundException{
        // abre arquivo
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        
        // criar objeto dao
        DaoMensalidade dao = new DaoMensalidade(null, this.getConexao());
        
        // percorre dados
        String linha;
        while ((linha = raf.readLine()) !=  null) {
            // quebra linha
            String valores[] = linha.split(";");

            // criar objeto
            VoMensalidade vo = new VoMensalidade(Integer.parseInt(valores[0]),
                                             Integer.parseInt(valores[1]),
                                             Integer.parseInt(valores[2]),
                                             valores[3],
                                             Integer.parseInt(valores[4]),
                                             Integer.parseInt(valores[5]));
            
            // setar vo
            dao.setVoMensalidade(vo);
            
            // salvar
            dao.salvar(false);
        }
    }    
    
    
    // getter and setters
    private BoConexao getConexao() {
        return conexao;
    }
    
}
