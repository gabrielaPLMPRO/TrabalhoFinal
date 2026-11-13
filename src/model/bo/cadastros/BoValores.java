/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.cadastros;

import dao.cadastros.DaoValores;
import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bo.bd.BoConexao;
import model.bo.I_BO;
import model.vo.cadastros.VoValores;

/**
 *
 * @author dlnotari
 */
public class BoValores implements I_BO {
    // atributos

    private VoValores vo;
    private DaoValores dao;
    private String erro;
    private BoConexao conexao;

    // construtor
    public BoValores(VoValores vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
        this.dao = new DaoValores(vo, conexao);
        this.erro = "";
    }

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
                this.setErro("Houve um erro ao salvar as informações dos valores"
                        + " no banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao salvar as informações dos valores "
                    + "no banco de dados\n" + ex.toString());
            return false;
        }

        // return, tudo ocorreu normal
        return true;
    }

    @Override
    public boolean excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean consultarCodigo(int codigo) {
        // testa o valor do código
        this.setVo(new VoValores());

        // consultar
        try {
            this.getDao().setVo(this.getVo());
            if (this.getDao().consultarCodigo()) {
                this.setVo(this.getDao().getVo());
                return true;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar os valores do banco de dados\n" + ex.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public ResultSet obterLista() {
        // obter lista
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoProduto.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar os valores do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }

    @Override
    public boolean validar() {
        boolean error = true;
        String msg = "";

        // testa o valor da sauna
        if (this.getVo().getValorSauna() <= 0) {
            msg += "O Valor da sauna deve ser informado!\n";
            error = false;
        }

        // testa o valor da aula de squash
        if (this.getVo().getValorAulaSquash() <= 0) {
            msg += "O Valor da aula do squash deve ser informado!\n";
            error = false;
        }

        // testa o dia de vencimento1 do squash
        if (this.getVo().getVencimento1() <= 0) {
            msg += "O dia de vencimento da quadra de squash deve ser informado,"
                    + "e deve ser um valor entre 1 e 28!\n";
            error = false;
        } else if (!this.validarVencimento(this.getVo().getVencimento1())) {
            msg += "O dia de vencimento da quadra de squash deve ser informado,"
                    + "e deve ser um valor entre 1 e 28!\n";
            error = false;
        }

        // testa o valor1 do squash
        if (this.getVo().getValorSquashMensal1() <= 0) {
            msg += "O Valor da quadra de squash deve ser maior que zero!\n";
            error = false;
        }

        // testa o valor2 do squash
        if ((this.getVo().getValorSquashMensal2() <= 0)
                && (this.getVo().getVencimento2() > 0)) {
            msg += "O Valor em atraso da quadra de squash deve ser informado"
                    + " em conjunto com a data de vencimento (2) de atraso!\n";
            error = false;
        } else if ((this.getVo().getValorSquashMensal2() > 0)
                && (this.getVo().getVencimento2() <= 0)) {
            msg += "O Valor em atraso da quadra de squash deve ser informado"
                    + " em conjunto com a data de vencimento (2) de atraso!\n";
            error = false;
        }

        // testa o dia de vencimento2 do squash
        if (this.getVo().getVencimento2() > 0) {
            if (!this.validarVencimento(this.getVo().getVencimento2())) {
                msg += "O dia de vencimento em atraso (2) da quadra de squash deve"
                        + " ser um valor entre 1 e 28!\n";
                error = false;
            } else if (this.getVo().getVencimento1() == this.getVo().getVencimento2()) {
                msg += "Os dias de vencimentos devem ser diferentes (1 e 2)!\n";
                error = false;
            } else if (this.getVo().getValorSquashMensal2() <= 0) {
                msg += "O Valor em atraso da quadra de squash deve ser informado"
                        + " em conjunto com a data de vencimento (2) de atraso!\n";
                error = false;
            }
        }

        // testa o valor3 do squash
        if ((this.getVo().getValorSquashMensal3() <= 0)
                && (this.getVo().getVencimento3() > 0)) {
            msg += "O Valor em atraso da quadra de squash deve ser informado"
                    + " em conjunto com a data de vencimento (3) de atraso!\n";
            error = false;
        }else if ((this.getVo().getValorSquashMensal3() > 0)
                && (this.getVo().getVencimento3() <= 0)) {
            msg += "O Valor em atraso da quadra de squash deve ser informado"
                    + " em conjunto com a data de vencimento (3) de atraso!\n";
            error = false;
        }

        // testa o dia de vencimento3 do squash
        if (this.getVo().getVencimento3() > 0) {
            if (!this.validarVencimento(this.getVo().getVencimento3())) {
                msg += "O dia de vencimento em atraso (3) da quadra de squash deve"
                        + " ser um valor entre 1 e 28!\n";
                error = false;
            } else if (this.getVo().getVencimento2() == this.getVo().getVencimento3()) {
                msg += "Os dias de vencimentos devem ser diferentes (3 e 2)!\n";
                error = false;
            } else if (this.getVo().getVencimento1() == this.getVo().getVencimento3()) {
                msg += "Os dias de vencimentos devem ser diferentes(1 e 3)!\n";
                error = false;
            } else if (this.getVo().getValorSquashMensal3() <= 0) {
                msg += "O Valor em atraso da quadra de squash deve ser informado"
                        + " em conjunto com a data de vencimento (3) de atraso!\n";
                error = false;
            }
        }

        // return....
        this.setErro(msg);
        return error;
    }

    /**
     * *
     * validar vencimento
     *
     * @return
     */
    private boolean validarVencimento(int dia) {
        // valida dia 
        if (dia >= 1 && dia <= 28) {
            return true;
        }

        // dia informado errado
        return false;
    }

    @Override
    public String getErro() {
        return this.erro;
    }

    @Override
    public int proximoCodigoLivre() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VoValores getVo() {
        return vo;
    }

    public void setVo(VoValores vo) {
        this.vo = vo;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public DaoValores getDao() {
        return dao;
    }

    public BoConexao getConexao() {
        return conexao;
    }
}
