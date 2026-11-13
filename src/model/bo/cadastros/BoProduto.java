/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.cadastros;

import dao.cadastros.DaoProduto;
import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bo.bd.BoConexao;
import model.bo.I_BO;
import model.vo.cadastros.VoProduto;

/**
 *
 * @author dlnotari
 */
public class BoProduto implements I_BO {
    // atributos

    private VoProduto vo;
    private String erro;
    private DaoProduto dao;
    private BoConexao conexao;

    // construtor
    public BoProduto(VoProduto vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
        this.dao = new DaoProduto(vo, conexao);
        this.erro = "";
    }

    /**
     * *
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
            // salvar
            this.getDao().setVo(this.getVo());
            if (!this.getDao().salvar(true)) {
                this.setErro("Houve um erro ao salvar as informações de produto no banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao salvar as informações de produto "
                    + "no banco de dados\n" + ex.toString());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     * *
     *
     * @return
     */
    @Override
    public boolean excluir() {
        // testa o valor do código
        if (!this.validarCodigo()) {
            return false;
        }

        try {
            // set vo
            this.getDao().setVo(this.getVo());
            
            // verifica se o produto é usado como chave estrangeira em uma outra tabela
            if (this.getDao().existeChaveEstrangeira()) {
                this.setErro("Este produto foi utilizado para consumo e, portanto, "
                        + "não pode ser excluído!");
                return false;
            }

            // excluir
            if (!this.getDao().excluir()) {
                this.setErro("Houve um erro ao excluir um produto do banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao excluir um produto do banco de dados\n" + ex.getMessage());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    /**
     * *
     *
     * @return
     */
    @Override
    public boolean consultarCodigo(int codigo) {
        // testa o valor do código
        this.setVo(new VoProduto(codigo));
        if (!this.validarCodigo()) {
            return false;
        }
        // consultar
        try {
            this.getDao().setVo(this.getVo());
            if (this.getDao().consultarCodigo()) {
                this.setVo(this.getDao().getVo());
                return true;
            } 
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar um produto do banco de dados\n" + ex.getMessage());
            return false;
        }
        
        // erro 
        this.setErro("Codigo de produto não cadastrado!");
        
        // return
        return false;
    }

    /**
     * *
     *
     * @return
     */
    @Override
    public ResultSet obterLista() {
        // salvar
        ResultSet resultado;
        try {
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
     *
     * @return
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
     *
     * @return
     */
    @Override
    public int proximoCodigoLivre() {
        try {
            return this.getDao().proximoCodigoLivre();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar o próximo código de produto do banco de dados\n" + ex.getMessage());
        }
        return 1;
    }

    /**
     * *
     *
     * @return
     */
    public boolean validarCodigo() {
        // testa o valor do código
        if (this.getVo().getCodigo() < 0) {
            this.setErro("O Código do Produto deve ser informado!");
            return false;
        }

        // códigos padrão, não podem ser mexidos
        if ((this.getVo().getCodigo() == 0)
                || (this.getVo().getCodigo() == 1)) {
            this.setErro("Os código 0 e 1 são de uso interno da aplicação! Estes"
                    + "códigos não podem ser modificados!");
            return false;
        }


        // return, sem erros
        return true;
    }

    /**
     * *
     *
     * @return
     */
    @Override
    public boolean validar() {
        boolean error = true;
        String msg = "";
        // testa o valor do código
        if (!this.validarCodigo()) {
            error = false;
            msg = this.getErro() + "\n";
        }

        // testa o valor do descrição
        if (this.getVo().getDescricao().isEmpty()) {
            msg += "A descrição do Produto deve ser informada!\n";
            error = false;
        }

        // testa o valor do descrição
        if (this.getVo().getDescricao().length() > 100) {
            msg += "A descrição do Produto deve ter no máximo 100 caracteres!\n";
            error = false;
        }


        // testa o valor do preço
        if (this.getVo().getPreco() <= 0) {
            msg += "O Preço do Produto deve ser informado!\n";
            error = false;
        }

        // testa o valor do unidade
        if (this.getVo().getUnidade().isEmpty()) {
            msg += "A unidade do Produto deve ser informada!";
            error = false;
        }

        // return....
        this.setErro(msg);
        return error;
    }

    /**
     * *
     *
     * getttes and setters
     */
    public VoProduto getVo() {
        return vo;
    }

    public void setVo(VoProduto vo) {
        this.vo = vo;
    }

    @Override
    public String getErro() {
        return this.erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public DaoProduto getDao() {
        return dao;
    }

    public void setDao(DaoProduto dao) {
        this.dao = dao;
    }

    public BoConexao getConexao() {
        return conexao;
    }
}
