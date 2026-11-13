/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.cadastros;

import dao.cadastros.DaoPagamento;
import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bo.bd.BoConexao;
import model.bo.I_BO;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoPagamento;

/**
 *
 * @author dlnotari
 */
public class BoPagamento implements I_BO {
    // atributos
    private VoPagamento vo;
    private DaoPagamento dao;
    private String erro;
    private BoConexao conexao;
    
    // atributos para controle de pagamento
    private double desconto;
    private double valorPago;

    // construtor
    public BoPagamento(VoPagamento vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
        this.erro = "";
        this.dao = new DaoPagamento(conexao, vo);
    }

    /**
     * 
     * @return 
     */
    @Override
    public boolean salvar() {
        // validar os campos
        if (!this.validar()) {
            return false;
        }
        try {
            // testa se houve diferença entre o valor pago e o total, 
            double diferenca = this.totalPagar() - this.getValorPago() - this.getDesconto();
            
            // se houve diferença, muda o total pago para parcial
            if (diferenca == 0)
                this.getVo().setPagarTudo(true);
            else
                this.getVo().setPagarTudo(false);            
           
            // salvar
            this.getDao().setVo(this.getVo());
            if (!this.getDao().salvar(true)) {
                this.setErro("Houve um erro ao salvar as informações de pagamento no banco de dados");
                return false;
            }

            // após o pagamento feito, altera o status de pago de consumo para true e,
            // testa se houve desconto, se sim, aplica o desconto no primeiro consumo
            this.getDao().alterarPagoConsumosCliente(desconto);

            // testa se houve diferença entre o valor pago e o total, e gera
            // um novo lançamento na tabela de consumo
            if (diferenca != 0){
                // gera um novo registro de consumo
                VoConsumo aux = new VoConsumo(vo.getCodigoCliente(), String.valueOf(vo.getData()), 0,
                        1, false, 0, diferenca);
                BoConsumo boConsumo = new BoConsumo(aux, conexao);
                int sequencia = boConsumo.proximoCodigoLivre();
                aux.setSequencia(sequencia);
                this.getDao().criarConsumoCliente(aux);
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao salvar as informações de pagamento "
                    + "no banco de dados\n" + ex.toString());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     * 
     * @return 
     */    
    @Override
    public boolean excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */    
    @Override
    public boolean consultarCodigo(int codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */    
    @Override
    public ResultSet obterLista() {
        // salvar
        ResultSet resultado;
        try {
            this.getDao().setVo(this.getVo());
            resultado = this.getDao().obterLista();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao pagamento de um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }

    /**
     * usado para backuo
     * @return 
     */    
    public ResultSet obterLista(boolean tudo) {
        // salvar
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista(tudo);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao pagamento de um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }
    
    /**
     * 
     * @return 
     */    
    public boolean validarCodigo() {
        // variavel de retorno
        boolean retorno = true;

        // testa o valor do código do cliente
        if (this.getVo().getCodigoCliente() < 0) {
            this.setErro("O cliente deve ser informado!");
            retorno = false;
        }

        // return, 
        return retorno;
    }

    /**
     * 
     * @return 
     */    
    @Override
    public boolean validar() {
        // variavel de retorno
        boolean retorno = true;

        // validar código do cliente
        if (!this.validarCodigo()) {
            retorno = false;
        }

        // testa o valor a ser pago
        if (this.getVo().getValor() <= 0) {
            this.setErro("O valor a ser pago deve ser informado!");
            retorno = false;
        }

        // return, 
        return retorno;
    }

    /**
     * 
     * @return 
     */    
    @Override
    public String getErro() {
        return this.erro;
    }

    @Override
    public int proximoCodigoLivre() {
        try {
            this.getDao().setVo(this.getVo());
            return this.getDao().proximoCodigoLivre();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar o próximo código de pagamento do banco de dados\n" + ex.getMessage());
        }
        return 1;
    }

    /**
     * calcula o total a ser pago
     *
     * @return
     */
    public double totalPagar() {
        // testa o valor do código
        if (!this.validarCodigo()) {
            return 0;
        }

        // consultar
        try {
            this.getDao().setVo(this.getVo());
            return this.getDao().totalPagar();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao calcular o total a ser pago por um cliente\n" + ex.getMessage());
            return 0;
        }
    }

    /***
     * método usado para fazer o pagamento da sauna
     * @return 
     */
    public boolean salvarConsumoSauna() {
        try {
            // salvar
            this.getDao().setVo(this.getVo());
            if (!this.getDao().salvar(true)) {
                this.setErro("Houve um erro ao salvar as informações de pagamento no banco de dados");
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao salvar as informações de pagamento"
                    + "do consumo da sauna no banco de dados\n" + ex.toString());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    // getters and setters
    public VoPagamento getVo() {
        return vo;
    }

    public void setVo(VoPagamento vo) {
        this.vo = vo;
    }

    public DaoPagamento getDao() {
        return dao;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
}
