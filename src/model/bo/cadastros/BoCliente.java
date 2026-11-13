/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.cadastros;

import dao.cadastros.DaoCliente;
import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.bo.bd.BoConexao;
import model.bo.I_BO;
import model.vo.cadastros.VoCliente;

/**
 *
 * @author dlnotari
 */
public class BoCliente implements I_BO {
    // atributos
    private VoCliente vo;
    private BoConexao conexao;
    private String erro;
    private DaoCliente dao;

    // construtor
    public BoCliente(VoCliente vo, BoConexao conexao) {
        this.vo = vo;
        this.conexao = conexao;
        this.dao = new DaoCliente(vo, conexao);
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
                this.setErro("Houve um erro ao salvar as informações de cliente no banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao salvar as informações de cliente "
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
        if (!this.validarCodigo()) {
            return false;
        }
        try {
            // set vo
            this.getDao().setVo(this.getVo());
            
            // verifica se o produto é usado como chave estrangeira em uma outra tabela
            if (this.getDao().existeChaveEstrangeira()) {
                this.setErro("Este cliente foi utilizado para consumo ou pagamento "
                        + "e, portanto, não pode ser excluído!");
                return false;
            }
            
            // excluir
            if (!this.getDao().excluir()) {
                this.setErro("Houve um erro ao excluir um cliente do banco de dados");
                return false;
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao excluir um cliente do banco de dados\n" + ex.getMessage());
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
        this.setVo(new VoCliente(codigo));
        if (!this.validarCodigo()) {
            return false;
        }
        // salvar
        try {
            this.getDao().setVo(this.getVo());
            if (this.getDao().consultarCodigo()) {
                this.setVo(this.getDao().getVo());
                return true;
            } 
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um cliente do banco de dados\n" + ex.getMessage());
            return false;
        }
        
        // erro 
        this.setErro("Codigo de cliente não cadastrado!");
        
        // return 
        return false;
    }

    /**
     * 
     */       
    @Override
    public ResultSet obterLista() {
        // consultar
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }
    
    /**
     * usado para backup
     */       
    public ResultSet obterLista(boolean tudo) {
        // consultar
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista(tudo);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um cliente do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }    

    /**
     * 
     */       
    @Override
    public int proximoCodigoLivre() {
        try {
            return this.getDao().proximoCodigoLivre();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar o próximo código de cliente do banco de dados\n" + ex.getMessage());
        }
        return 1;
    }

    /**
     * 
     */       
    public boolean validarCodigo() {
        // testa o valor do código
        if (this.getVo().getCodigo() <= 0) {
            this.setErro("O Código do cliente deve ser informado!");
            return false;
        }

        // return, sem erros
        return true;
    }

     /**
     * 
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

        // testa o valor do cpf
        if (!this.getVo().getCpf().isEmpty()) {
            if (this.getVo().getCpf().length() > 11) {
                msg += "O Cpf do cliente deve ter no máximo 11 caracteres!\n";
                error = false;
            }
        }

        // testa o valor do nome
        if (this.getVo().getNome().isEmpty()) {
            msg += "O nome do cliente deve ser informado!\n";
            error = false;
        }

        // testa o valor do nome
        if (this.getVo().getNome().length() > 50) {
            msg += "O nome do cliente deve ter no máximo 50 caracteres!\n";
            error = false;
        }

        // testa o valor do data de aniverário
        if (!this.getVo().getDataAniversario().isEmpty()) {
            if (this.getVo().getDataAniversario().length() > 8) {
                msg += "A data de aniversário do cliente deve ter no máximo 8 caracteres!\n";
                error = false;
            }
        }

        // testa o valor do telefone
        if (!this.getVo().getTelefone().isEmpty()) {
            if (this.getVo().getTelefone().length() > 15) {
                msg += "O telefone do cliente deve ter no máximo 15 caracteres!\n";
                error = false;
            }
        }

        // return....
        this.setErro(msg);
        return error;
    }

    /**
     * getters and setters
     */       
    @Override
    public String getErro() {
        return this.erro;
    }

    public VoCliente getVo() {
        return vo;
    }

    public void setVo(VoCliente vo) {
        this.vo = vo;
    }

    public BoConexao getConexao() {
        return conexao;
    }

    public DaoCliente getDao() {
        return dao;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}
