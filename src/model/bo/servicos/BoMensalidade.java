/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.servicos;

import dao.bd.E_BD;
import dao.cadastros.DaoConsumo;
import dao.servicos.DaoMensalidade;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bo.I_BO;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoCliente;
import model.vo.cadastros.VoCliente;
import model.vo.cadastros.VoConsumo;
import model.vo.cadastros.VoProduto;
import model.vo.cadastros.VoValores;
import model.vo.servicos.VoHorarioSquash;
import model.vo.servicos.VoMensalidade;
import utils.Data;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class BoMensalidade implements I_BO{
    // atributos
    private VoMensalidade voMensalidade;
    private StringBuilder textoJanela;
    private VoHorarioSquash vo;
    private DaoConsumo daoConsumo;
    private BoCliente boCliente;
    private String erro;
    private DaoMensalidade dao;

    // construtor
    public BoMensalidade(VoHorarioSquash vo, BoConexao conexao) {
        this.vo = vo;
        this.erro = "";
        this.dao = new DaoMensalidade(vo, conexao);
        this.daoConsumo = new DaoConsumo(conexao, new VoConsumo());
        this.boCliente = new BoCliente(new VoCliente(), conexao);
    }
    
    // construtor
    public BoMensalidade(VoMensalidade voMensalidade, BoConexao conexao) {
        this.vo = new VoHorarioSquash();
        this.erro = "";
        this.dao = new DaoMensalidade(vo, conexao);
        this.daoConsumo = new DaoConsumo(conexao, new VoConsumo());
        this.boCliente = new BoCliente(new VoCliente(), conexao);
        this.voMensalidade = voMensalidade;
    }    

    /**
     * faz o controle da geração de mensalidades
     *
     * @param dataHoje
     * @param voValores
     * @return
     */
    public boolean gerarMensalidades(int mesHoje, int anoHoje, String dataHoje, VoValores voValores) {
        // gerar mensalidades
        if (!this.gerarMensalidades(voValores.getValorSquashMensal1(),
                voValores.getValorAulaSquash(), dataHoje)) {
            this.setErro(this.getErro() + "As mensalidades deste mês já foram geradas!");
            return false;
        }

        // return
        return true;
    }

    /**
     * *
     * faz o controle do geração de multas
     *
     * @param diaHoje
     * @param dataHoje
     * @param voValores
     * @return
     */
    public boolean gerarMultas(int diaHoje, int mesHoje, int anoHoje, String dataHoje, VoValores voValores) {
        // pega vencimentos
        int dia1 = voValores.getVencimento1();
        int dia2 = voValores.getVencimento2();
        int dia3 = voValores.getVencimento3();
        boolean gerou = false;

        // verifica se a multa 1 já foi gerada
        if (this.verificarMultasGeradas(mesHoje, anoHoje, VoProduto.MultaMensalidade1)) {
            this.setErro(this.getErro() + "As multas deste mês já foram geradas!");
            return false;
        }

        // verificar  se está no período de gerar a multa1
        if ((diaHoje > dia1) && (diaHoje >= dia2) && (diaHoje < dia3)) {
            double valor = voValores.getValorSquashMensal2() - voValores.getValorSquashMensal1();
            this.gerarMulta(valor, dataHoje, mesHoje, anoHoje, VoProduto.MultaMensalidade1);
            gerou = true;
        } else {
            // verifica se a multa 2 já foi gerada
            if (this.verificarMultasGeradas(mesHoje, anoHoje, VoProduto.MultaMensalidade2)) {
                this.setErro(this.getErro() + "As multas deste mês já foram geradas!");
                return false;
            } else {
                if (((diaHoje > dia1) && (diaHoje >= dia3)) || (diaHoje < dia1)) {
                    double valor = voValores.getValorSquashMensal3() - voValores.getValorSquashMensal2();
                    this.gerarMulta(valor, dataHoje, mesHoje, anoHoje, VoProduto.MultaMensalidade2);
                    gerou = true;
                }
            }
        }

        // testa se não gerou
        if (!gerou) {
            this.setErro("As multas não foram geradas porque está fora do período correto!");
            return false;
        }

        // return
        return true;
    }

    /**
     * *
     * este método lê as reservas para cada dia da semana com o número total de
     * clientes, depois busca as informações de cada reserva e gera um novo
     * consumo para cada cliente da reserva ou da aula de squash
     *
     * @param valorMensalidade
     * @param valorAula
     * @param dataHoje
     * @return
     */
    private boolean gerarMensalidades(double valorMensalidade, double valorAula,
            String dataHoje) {
        try {
            // set vo
            this.getDao().setVo(this.getVo());

            // busca todas as reservas com o número de clientes   
            ResultSet rs = this.getDao().obterListaReservasComNumeroClientes();

            // percorre laço
            while (rs.next()) {
                // obtem valores
                String horario = rs.getString("horario");
                int dia = rs.getInt("dia");
                int quadra = rs.getInt("quadra");
                double cont = rs.getDouble(4);
                boolean jogo = rs.getBoolean("jogo");

                // buscar informações dos clientes da reserva
                VoHorarioSquash voAux = new VoHorarioSquash(dia, quadra, horario);

                // set vo
                this.getDao().setVo(voAux);

                // busca os clientes de cada reserva de acordo com a resposta acima
                ResultSet rs1 = this.getDao().obterClientesReserva();

                // calcula o total a ser pago por cada cliente
                double mensalidade;
                if (jogo) {
                    mensalidade = valorMensalidade / cont;
                } else {
                    mensalidade = valorAula * Data.numeroDiasMes(dataHoje, dia);
                }

                // percorre a lista de clientes
                while (rs1.next()) {
                    // pega o código do cliente
                    int codigoCliente = rs1.getInt(1);

                    // criar vo consumo
                    VoConsumo voConsumo = new VoConsumo(codigoCliente, dataHoje,
                            VoProduto.CodigoMensalidade, mensalidade);

                    // set vo
                    this.getDaoConsumo().setVo(voConsumo);
                    
                    // verificar se há mensalidade já foi gerada
                    this.setVoMensalidade(new VoMensalidade(codigoCliente,
                            dia, quadra, horario, ValidarStrings.getMesData(dataHoje),
                            ValidarStrings.getAnoData(dataHoje)));
                    this.getDao().setVoMensalidade(this.getVoMensalidade());
                    if (this.getDao().verificarMensalidadeGerada()) {
                        // já foi gerada, pula para o próximo registro
                        continue;
                    }

                    // gravar mensalidade
                    if (!this.getDaoConsumo().salvar(true)) {
                        // houve erro ao gerar a mensalidade
                        this.setErro("Houve erro ao gerar a mensalidade do cliente como um consumo!");
                        return false;
                    }
                    
                    // salvar que a mensalidade foi gerada
                    this.setVoMensalidade(new VoMensalidade(codigoCliente,
                            dia, quadra, horario, ValidarStrings.getMesData(dataHoje),
                            ValidarStrings.getAnoData(dataHoje)));
                    this.getDao().setVoMensalidade(this.getVoMensalidade());
                    if (!this.getDao().salvar(true)) {
                        // houve erro ao gerar a mensalidade
                        this.setErro("Houve erro ao salvar a mensalidade gerada do cliente!");
                        return false;
                    }                   

                    // buscar nome do cliente para gerar texto para a janela
                    this.getBoCliente().consultarCodigo(codigoCliente);
                    String nome = this.getBoCliente().getVo().getNome();
                    this.getTextoJanela().append("Mensalidade gerada para o cliente "
                            + nome + " no valor de " 
                            + ValidarStrings.formatarDouble(mensalidade) + "\n");
                }
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao gerar as mensalidades para os clientes"
                    + "do squash!\n" + ex.toString());
            return false;
        }

        // return
        return true;
    }

    /**
     * *
     * gera a primeira multa para os clientes devedores
     *
     * @param valorMulta
     * @param dataHoje
     * @return
     */
    private boolean gerarMulta(double valorMulta, String dataHoje, int mes,
            int ano, int codigoMulta) {
        try {
            // set vo
            this.getDao().setVo(this.getVo());

            // busca todas as reservas com o número de clientes   
            ResultSet rs = this.getDao().obterListaReservasComNumeroClientes();

            // percorre laço
            while (rs.next()) {
                // obtem valores
                String horario = rs.getString("horario");
                boolean jogo = rs.getBoolean("jogo");
                int dia = rs.getInt("dia");
                int quadra = rs.getInt("quadra");
                double cont = rs.getDouble(4);
                
                // define valor multa
                double multa = valorMulta / cont;
                
                // testa se é jogo
                if (!jogo){
                    // pula para o próximo registro
                    continue;
                }

                // buscar informações dos clientes da reserva
                VoHorarioSquash voAux = new VoHorarioSquash(dia, quadra, horario);

                // set vo
                this.getDao().setVo(voAux);

                // busca os clientes de cada reserva de acordo com a resposta acima
                ResultSet rs1 = this.getDao().obterClientesReserva();

                // percorre a lista de clientes
                while (rs1.next()) {
                    // pega o código do cliente
                    int codigoCliente = rs1.getInt(1);

                    // pesquisar se a mensalidade foi paga
                    if (!this.verificarMensalidadePaga(codigoCliente, mes, ano, VoProduto.CodigoMensalidade)) {
                        // criar vo consumo
                        VoConsumo voConsumo = new VoConsumo(codigoCliente, dataHoje,
                                codigoMulta, multa);

                        // set vo
                        this.getDaoConsumo().setVo(voConsumo);

                        // gravar mensalidade
                        if (!this.getDaoConsumo().salvar(true)) {
                            // houve erro ao gerar a mensalidade
                            this.setErro("Houve erro ao gerar a multa do cliente como um consumo!");
                            return false;
                        }

                        // buscar nome do cliente para gerar texto para a janela
                        this.getBoCliente().consultarCodigo(codigoCliente);
                        String nome = this.getBoCliente().getVo().getNome();
                        this.getTextoJanela().append("Multa gerada para o cliente "
                                + nome + " no valor de " +
                                ValidarStrings.formatarDouble(multa) + "\n");
                    }
                }
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao gerar a primeira multa para os clientes"
                    + "do squash!\n" + ex.toString());
            return false;
        }

        // return
        return true;
    }

    // verificar se há mensalidade de um mês e ano foi paga
    private boolean verificarMensalidadePaga(int codigoCliente, int mes, int ano,
            int codigoProdutoMensalidade) {
        try {
            // return
            return this.getDao().verificarMensalidadePaga(codigoCliente, mes, ano, codigoProdutoMensalidade);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao pesquisar se a mensalidade de um cliente"
                    + " foi paga!\n" + ex.toString());
            return false;
        }
    }

    // verificar se a multa 1 de um mês já foi gerada
    private boolean verificarMultasGeradas(int mes, int ano, int codigoMulta) {
        try {
            // return
            return this.getDao().verificarMensalidadadesMultasGeradas(mes, ano, codigoMulta);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao pesquisar se a multa de um cliente"
                    + " já foi gerada!\n" + ex.toString());
            return false;
        }
    }

    // getters and setters
    @Override
    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    private DaoMensalidade getDao() {
        return dao;
    }

    public VoHorarioSquash getVo() {
        return vo;
    }

    public void setVo(VoHorarioSquash vo) {
        this.vo = vo;
    }

    public DaoConsumo getDaoConsumo() {
        return daoConsumo;
    }

    public StringBuilder getTextoJanela() {
        return textoJanela;
    }

    public void setTextoJanela(StringBuilder textoJanela) {
        this.textoJanela = textoJanela;
    }

    private BoCliente getBoCliente() {
        return boCliente;
    }

    public VoMensalidade getVoMensalidade() {
        return voMensalidade;
    }

    public void setVoMensalidade(VoMensalidade voMensalidade) {
        this.voMensalidade = voMensalidade;
    }

    @Override
    public boolean salvar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean excluir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean consultarCodigo(int codigo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet obterLista() {
        // obter lista
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista();
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            Logger.getLogger(BoMensalidade.class.getName()).log(Level.SEVERE, null, ex);
            this.setErro("Houve um erro ao consultar as mensalidades geradas do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;    }

    @Override
    public boolean validar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int proximoCodigoLivre() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
