/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.cadastros;

import dao.cadastros.DaoConsumo;
import dao.servicos.DaoConsumoSauna;
import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bo.bd.BoConexao;
import model.bo.I_BO;
import model.vo.cadastros.VoConsumo;
import model.vo.servicos.VoConsumoSauna;

/**
 *
 * @author dlnotari
 */
public class BoConsumo implements I_BO {
    // atributos

    private VoConsumo vo;
    private String erro;
    private DaoConsumo dao;
    private DaoConsumoSauna daoSauna;
    private BoConexao conexao;

    // construtor
    public BoConsumo(VoConsumo vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
        this.dao = new DaoConsumo(conexao, vo);
        this.daoSauna = new DaoConsumoSauna(conexao);
        this.erro = "";
    }

    /**
     *
     */
    @Override
    public boolean salvar() {
        // validar os campos
        if (!this.validar()) {
            return false;
        }
        try {
            // salvar
            this.getDao().setVo(this.getVo());
            if (!this.getDao().salvar(true)) {
                this.setErro("Houve um erro ao salvar as informações de consumo no banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao salvar as informações de consumo "
                    + "no banco de dados\n" + ex.toString());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     *
     */
    @Override
    public boolean excluir() {
        // testa o valor do código
        if (!this.validarChavePrimaria()) {
            return false;
        }
        try {
            // atribui vo
            this.getDao().setVo(this.getVo());
            
            // verificar se o consumo já foi pago
            if (this.getDao().consultarCodigo()) {
                if (this.getVo().isPago()) {
                    // já foi pago, não pode ser excluído
                this.setErro("Este consumo já foi pago e não pode ser mais excluído!");
                return false;
                    
                }
            }
            
            // excluir
            if (!this.getDao().excluir()) {
                this.setErro("Houve um erro ao excluir um consumo do banco de dados!");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao excluir um consumo do banco de dados\n" + ex.getMessage());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     *
     */
    @Override
    public boolean consultarCodigo(int codigo) {
        // testa o valor do código
        if (!this.validarChavePrimaria()) {
            return false;
        }

        // consultar
        try {
            this.getDao().setVo(this.getVo());
            if (this.getDao().consultarCodigo()) {
                this.setVo(this.getDao().getVo());
                return true;
            } else {
                return true;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar um consumo do banco de dados\n" + ex.getMessage());
            return false;
        }
    }

    /**
     *
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
            this.setErro("Houve um erro ao consultar um produto do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }
    
    /**
     * usado para backup
     */
    public ResultSet obterLista(boolean tudo) {
        // salvar
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista(tudo);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar um produto do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }    

    /**
     * *
     * obtem lista para a tela de pagamento, tela de ficha do cliente e
     * tela de fluxo de caixa     *
     * @return
     */
    public ResultSet obterLista(boolean data, boolean descendente) {
        // salvar
        ResultSet resultado;
        try {
            this.getDao().setVo(this.getVo());
            resultado = this.getDao().obterLista(data, descendente);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar um produto do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }

    /**
     *
     */
    private boolean validarChavePrimaria() {
        // variavel de retorno
        boolean retorno = true;

        // testa o valor do código do cliente
        if (this.getVo().getCodigoCliente() < 0) {
            this.setErro("O cliente deve ser informado!");
            retorno = false;
        }

        // testa o valor da data
        if (this.getVo().getData().isEmpty()) {
            this.setErro("A data do consumo deve ser informada!");
            retorno = false;
        }

        // testa o valor da sequencia
        if (this.getVo().getSequencia() < 0) {
            this.setErro("Houve um problema ao gerar o código do consumo!");
            retorno = false;
        }

        // return, 
        return retorno;
    }

    /**
     * valida as informações da interface gráfica
     *
     * @return
     */
    /**
     * Este método válida se os valores do formulário foram informados
     */
    public boolean validar(String codigoCliente, String codigoProduto,
            String quantidade, String preco) {
        boolean error = true;
        String msg = "";

        // testa se o código do cliente foi informado
        if (codigoCliente.isEmpty()) {
            // codigo cliente não informado
            msg += "Um cliente deve ser selecionado para o consumo de um produto!\n";
            error = false;
        }

        // testa se o código do produto foi informado
        if (codigoProduto.isEmpty()) {
            // codigo produto não informado
            msg += "Um produto deve ser selecionado para o consumo!\n";
            error = false;
        }
        // testa se a quantidade do produto foi informado
        if (quantidade.isEmpty()) {
            // quantidade não informado
            msg += "A quantidade do produto deve ser informada para o consumo!\n";
            msg += "Um produto deve ser selecionado para o consumo!\n";
            error = false;
        }

        // testa se o valor do produto foi informado
        if (preco.isEmpty()) {
            // preço não informado
            msg += "O preço do produto deve ser informada para o consumo!\n";
            error = false;
        }

        // return....
        this.setErro(msg);
        return error;
    }

    /**
     *
     */
    @Override
    public boolean validar() {
        boolean error = true;
        String msg = "";

        // testa o valor chave primária
        if (!this.validarChavePrimaria()) {
            error = false;
            msg = this.getErro() + "\n";
        }

        // testa o valor da quantidade
        if (this.getVo().getQuantidade() <= 0) {
            msg += "A quantidade do Produto deve ser informada!\n";
            error = false;
        }

        // testa o valor do preço
        if (this.getVo().getPreco() <= 0) {
            msg += "O Preço do Produto deve ser informado!\n";
            error = false;
        }

        // testa o valor do código do produto
        if (this.getVo().getCodigo() <= 0) {
            msg += "O código do Produto consumido deve ser informado!\n";
            error = false;
        }

        // return....
        this.setErro(msg);
        return error;
    }

    /**
     *
     */
    @Override
    public String getErro() {
        return this.erro;
    }

    /**
     *
     */
    @Override
    public int proximoCodigoLivre() {
        try {
            this.getDao().setVo(this.getVo());
            return this.getDao().proximoCodigoLivre();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar o próximo código de consumo do banco de dados\n" + ex.getMessage());
        }
        return 1;
    }

    /**
     * *
     * salva o consumo da sauna
     *
     * @return
     */
    public boolean salvarConsumoSauna(VoConsumoSauna voAux) {
        try {
            // salvar
            this.getDaoSauna().setVo(voAux);
            if (!this.getDaoSauna().salvar(true)) {
                this.setErro("Houve um erro ao salvar as informações de consumo de"
                        + "um cliente da sauna no banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao salvar as informações de consumo "
                    + "de um cliente da sauna no banco de dados\n" + ex.toString());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     * insere o consumo da sauna
     */
    public boolean insereConsumoSauna(String nome, double valorSauna, String data) throws SQLException, E_BD, ClassNotFoundException {
        // converte valores
        int codigoCliente = 0;
        int codigoProduto = 1;
        int quantidade = 1;

        // create vo
        this.setVo(new VoConsumo(codigoCliente, data, quantidade, quantidade, false, codigoProduto, valorSauna));

        // gerar sequencia
        this.getVo().setSequencia(this.proximoCodigoLivre());

        // inserir novo cliente na sauna como consumo
        this.getDao().setVo(this.getVo());
        if (!this.getDao().salvar(true)) {
            return false;
        }

        // salvar sauna em consumo da sauna
        VoConsumoSauna temp = new VoConsumoSauna(this.getVo(), nome);
        if (!this.salvarConsumoSauna(temp)) {
            return false;
        }

        // return
        return true;
    }

    /**
     * getters and setters
     */
    public VoConsumo getVo() {
        return vo;
    }

    public void setVo(VoConsumo vo) {
        this.vo = vo;
    }

    public DaoConsumo getDao() {
        return dao;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public DaoConsumoSauna getDaoSauna() {
        return daoSauna;
    }
}
