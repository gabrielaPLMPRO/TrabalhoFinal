/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo.servicos;

import dao.servicos.DaoConsumoSauna;
import dao.bd.E_BD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.bo.bd.BoConexao;
import model.bo.cadastros.BoConsumo;
import model.vo.cadastros.VoCliente;
import model.vo.servicos.VoClienteSauna;
import model.vo.cadastros.VoConsumo;
import model.vo.servicos.VoConsumoSauna;

/**
 *
 * @author Daniel
 */
public class BoSauna {
    // atributos

    private ArrayList<VoClienteSauna> vetorClientes;
    private String erro;
    private int numeroClientes;
    private DaoConsumoSauna dao;

    // construtor
    public BoSauna(BoConexao conexao) {
        this.vetorClientes = new ArrayList<>();
        this.erro = "";
        this.numeroClientes = 0;
        this.dao = new DaoConsumoSauna(conexao);
    }

    /**
     * mostra o que um cliente consumiu na sauna
     */
    public ArrayList<String> buscarConsumoCliente(int posicao) throws SQLException, E_BD, ClassNotFoundException {
        // cria objetos temporários
        ArrayList<String> aux = new ArrayList<>(5);
        double total = 0;

        // testa o tamanho do vetor
        if (this.getVetorClientes().isEmpty()) {
            return aux;
        }

        // percorre vetor de consumo
        for (VoConsumo consumo : this.getVetorClientes().get(posicao).getConsumo()) {
            // calcula total parcial
            double parcial = consumo.getPreco() * consumo.getQuantidade();
            total += parcial;

            // adiciona consumo
            StringBuilder builder = new StringBuilder();
            builder.append("Produto = ").append(consumo.getDescricao().trim());
            builder.append("  Qtde = ").append(consumo.getQuantidade());
            builder.append("  Valor = ").append(consumo.getPreco());
            builder.append("  Total = ").append(parcial);

            // adiciona a variável de retorno
            aux.add(builder.toString());
        }

        // verificar pagamento foi feito
        if (this.getVetorClientes().get(posicao).isConsumoPago()) {
            // busca o total pago
            double totalPago = this.getValorASerPago(
                    this.getVetorClientes().get(posicao).getCliente().getNome(),
                    this.getVetorClientes().get(posicao).getConsumo().get(0).getData(),
                    posicao);

            // adiciona total pago
            aux.add("\t\t\tTotal Pago = " + totalPago);
        } else {
            // adiciona total até o momento
            aux.add("\t\t\tTotal parcial = " + total);
        }
        // return
        return aux;
    }

    /**
     * *
     * adiciona um novo cliente a sauna
     */
    public void adicionarCliente(String nome, double valorSauna, String data, BoConsumo boConsumo) throws SQLException, E_BD, ClassNotFoundException {
        // incrementa número de clientes
        this.numeroClientes++;

        // cria objeto do cliente
        VoClienteSauna aux = new VoClienteSauna(new VoCliente(numeroClientes, nome));

        // adiciona um consumo com o valor padrão da sauna
        aux.getConsumo().add(new VoConsumo(numeroClientes, data, valorSauna));

        // adicionar consumo da sauna no banco de dados
        if (!boConsumo.insereConsumoSauna(nome, valorSauna, data)) {
            // houve erro
            this.setErro(boConsumo.getErro());
            return;
        }

        // cria objeto no vetor
        this.getVetorClientes().add(aux);
    }

    /**
     * *
     * exclui um cliente da sauna
     */
    public void excluirCliente(String nome, String data, int posicao) throws SQLException, E_BD, ClassNotFoundException {
        // set vo
        this.getDao().setVo(new VoConsumoSauna(nome, data));

        // excluir da tabela de consumo e da tabela consumoSauna
        this.getDao().excluir();

        // remove da lista de clientes
        this.getVetorClientes().remove(posicao);
    }

    /**
     * *
     * adiciona um novo consumo de cliente a sauna
     */
    public void adicionarConsumoCliente(String nome, String data, int posicao) throws SQLException, E_BD, ClassNotFoundException {
        // cria vo
        this.getDao().getVo().setNome(nome);
        this.getDao().getVo().setData(data);

        // recupera os dados do banco de dados
        ResultSet rs = this.getDao().obterLista();

        // percorre a consulta
        ArrayList<VoConsumo> consumo = new ArrayList<>();
        while (rs.next()) {
            consumo.add(new VoConsumo(rs.getInt("codigo_cliente"), rs.getString("data"),
                    rs.getInt("sequencia"), rs.getInt("quantidade"), false,
                    rs.getInt("codigo_produto"), rs.getString("descricao"),
                    rs.getDouble("valor"), "un"));
        }

        // atribui consumo para o vetor local
        this.getVetorClientes().get(posicao).setConsumo(consumo);
    }

    /**
     * *
     * exclui um cliente da sauna
     */
    public void excluirConsumoCliente(String nome, String data, int posicaoCliente,
            int posicaoConsumo) throws SQLException, E_BD, ClassNotFoundException {
        // set vo
        this.getDao().setVo(new VoConsumoSauna(nome, data));

        // recupera consumo
        VoConsumo aux = this.getVetorClientes().get(posicaoCliente).getConsumo().get(posicaoConsumo);

        // excluir da tabela de consumo e da tabela consumoSauna
        this.getDao().setVo(new VoConsumoSauna(aux, nome));
        this.getDao().excluirConsumo();

        // remove da lista de clientes
        this.getVetorClientes().get(posicaoCliente).getConsumo().remove(posicaoConsumo);
    }

    /**
     * *
     * gerar pagamento cliente
     */
    public boolean gerarPagamentoCliente(String nome, String data, int posicao,
            double total, double valorPago) throws SQLException, E_BD, ClassNotFoundException {

        // calcula diferenca para fazer ajustes
        double diferenca = valorPago - total;

        // set vo
        this.getDao().setVo(new VoConsumoSauna(nome, data));

        // atualizar status para pago dos consumos, fazer ajustes no valor dos consumos e,        
        this.getDao().alterarPagoConsumosClienteSauna(diferenca);

        // setar o consumo do cliente como pago
        this.getVetorClientes().get(posicao).setConsumoPago(true);

        // retornar
        return true;
    }

    /***
     * obtem a lista de clientes e consumo da sauna para uma data especifica
     * @param data 
     */
    public ArrayList<String> obterLista(String data) throws SQLException, E_BD, ClassNotFoundException {
        // set vo
        this.getDao().getVo().setData(data);

        // executa a consulta
        ResultSet rs = this.getDao().obterLista(true);

        // zera número de clientes
        this.numeroClientes = 0;

        // cria a lista de clientes
        this.setVetorClientes(new ArrayList<VoClienteSauna>());

        // criar lista de clientes para o controlador
        ArrayList<String> clientes = new ArrayList<>();

        // monta a lista em memória de clientes da sauna
        VoCliente voCliente;
        while (rs.next()) {
            // pega valores
            String nome = rs.getString("nome");
            int codigoCliente = rs.getInt("codigo_cliente");

            // adiciona cliente na lista do controlador
            clientes.add(nome);

            // criar objeto cliente
            voCliente = new VoCliente(codigoCliente, nome);

            // criar vetor de consumos
            ArrayList<VoConsumo> listaConsumo = new ArrayList<>();

            // buscar informações de consumo 
            ResultSet rs1 = this.getDao().obterListaConsumo(nome, data);

            // varre lista de consumos
            boolean pagoTotal = false;
            while (rs1.next()) {
                int codigoProduto = rs1.getInt("codigo_produto");
                int quantidade = rs1.getInt("quantidade");
                int sequencia = rs1.getInt("sequencia");
                double valor = rs1.getDouble("valor");
                String descricao = rs1.getString("descricao");
                boolean pago = rs1.getBoolean("pago");
                String unidade = rs1.getString("unidade");
                pagoTotal = pago;

                // criar objeto consumo
                VoConsumo voConsumo = new VoConsumo(codigoCliente, data, sequencia,
                        quantidade, pago, codigoProduto, descricao, valor, unidade);

                // adiciona consumo
                listaConsumo.add(voConsumo);
            }

            // criar objeto do cliente na sauna            
            VoClienteSauna voClienteSauna = new VoClienteSauna(voCliente, listaConsumo, pagoTotal);

            // adiciona cliente na lista da sauna
            this.getVetorClientes().add(voClienteSauna);
        }

        // return 
        return clientes;
    }

    /**
     * usado para backup
     *
     * @return
     */
    public ResultSet obterLista(int tudo) {
        // salvar
        ResultSet resultado;
        try {
            resultado = this.getDao().obterLista(tudo);
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            this.setErro("Houve um erro ao consultar um produto do banco de dados\n" + ex.getMessage());
            return null;
        }

        // return, tudo ocorreu normal
        return resultado;
    }

    /**
     * *
     * calcula valor a ser pago
     */
    public double getValorASerPago(String nome, String data, int posicao) throws SQLException, E_BD, ClassNotFoundException {
        // cria vo
        this.getDao().getVo().setNome(nome);
        this.getDao().getVo().setData(data);

        // recupera os dados do banco de dados
        double total = this.getDao().getValorASerPago();

        // retorna
        return total;
    }

    /**
     * *
     * validar nome do cliente
     *
     * @return
     */
    public boolean validarNomeCliente(String nome) {
        boolean error = true;
        String msg = "";

        // verificar se o nome foi informado
        if ((nome == null) || (nome.isEmpty())) {
            // mensagem de erro
            msg = "O nome do cliente deve ser informado!\n";
            error = false;
        }

        // verificar nomes iguais
        for (VoClienteSauna aux : this.getVetorClientes()) {
            // compara nome atual com os clientes
            if (aux.getCliente().getNome().equalsIgnoreCase(nome)) {
                // mensagem de erro
                msg = "O nome do cliente deve ser diferente de outro cliente da sauna!\n";
                error = false;
                break;
            }
        }

        // return....
        this.setErro(msg);
        return error;
    }

    /**
     * pesquisa se a conta já foi encerrada de um cliente
     *
     * @param posicao
     * @return
     */
    public boolean isContaClienteFinalizada(int posicao) {
        return this.getVetorClientes().get(posicao).isConsumoPago();
    }

    /**
     * *
     * getters and setters
     *
     * @return
     */
    public ArrayList<VoClienteSauna> getVetorClientes() {
        return vetorClientes;
    }

    private void setVetorClientes(ArrayList<VoClienteSauna> vetorClientes) {
        this.vetorClientes = vetorClientes;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public DaoConsumoSauna getDao() {
        return dao;
    }
}
