/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.DaoTabelas;
import gui.controller.principal.CoConsulta;
import gui.view.serviços.GuiHorariosSquash;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.bo.bd.BoConexao;
import model.bo.servicos.BoHorarioSquash;
import model.vo.cadastros.VoCliente;
import model.vo.servicos.VoHorarioSquash;

/**
 *
 * @author Daniel
 */
public class CoHorarioSquash implements gui.controller.I_Controller {
    // atributos

    private VoHorarioSquash vo;
    private GuiHorariosSquash gui;
    private BoHorarioSquash bo;
    private boolean carregarClientes;
    // valores do squash
    private double valorAula;
    private double valorQuadra;
    private ArrayList<VoCliente> clientes;
    private ArrayList<VoCliente> clientesCadastrados;
    
    // construtor
    public CoHorarioSquash(GuiHorariosSquash gui, BoConexao conexao) {
        this.gui = gui;
        this.vo = new VoHorarioSquash();
        this.bo = new BoHorarioSquash(vo, conexao, this);
        this.carregarClientes = true;
        this.clientes = new ArrayList<>();
    }

    /**
     * *
     * carrega a lista de clientes para o combobox
     */
    public void carregarListaClientes() {
        // carrega os clientes apenas uma vez
        if (this.carregarClientes) {
            // carregar clientes
            this.carregarClientes = false;

            // obter lista de clientes
            this.setClientesCadastrados(this.getBo().obterListaClientes());
                        
            // testa se há clientes
            if (this.getClientesCadastrados() == null) {
                // houve erro
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
            }

            // monta combobox
            String aux[] = new String[this.getClientesCadastrados().size()];
            int i = 0;
            for (VoCliente voCliente : this.getClientesCadastrados()) {
                aux[i] = voCliente.getNome();
                i++;
            }

            // seta combobox
            this.getGui().getjComboBoxCliente().setModel(new javax.swing.DefaultComboBoxModel(aux));

            // setar o valor
            this.setarValor("jogo");
        }
    }

    /**
     * adicionar um cliente a reserva de quadra de squash
     */
    public void adicionarCliente() {
        // pega o nome selecionado
        String nome = this.getGui().getjComboBoxCliente().getSelectedItem().toString();
        
        // pega a posicao do item selecionado
        int posicao = this.getGui().getjComboBoxCliente().getSelectedIndex();

        // recupera linha
        String[] aux = this.getGui().getjTextAreaClientes().getText().split("\n");

        // verifica se o nome já foi incluido
        for (String x : aux) {
            if (x.equals(nome)) {
                // nome já existe
                JOptionPane.showMessageDialog(this.getGui(), "Este cliente já foi incluído nesta reserva!",
                        "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // adiciona nome na tela
        this.getGui().getjTextAreaClientes().append(nome + "\n");

        // adiciona o cliente na lista de reservas
        this.getClientes().add(this.getClientesCadastrados().get(posicao));
    }

    
    /**
     * excluir um cliente a reserva de quadra de squash
     */
    public void excluirCliente() {
        // pega o nome selecionado
        String nome = this.getGui().getjComboBoxCliente().getSelectedItem().toString();
        
        // pega a posicao do item selecionado
//        int posicao = this.getGui().getjComboBoxCliente().getSelectedIndex();        

        // recupera linha
        String[] aux = this.getGui().getjTextAreaClientes().getText().split("\n");

        // verifica se o nome já foi incluido
        int posicao = -1, i = 0;
        String texto = "";
        for (String x : aux) {
            // compara o nome a ser excluído
            if (!x.equals(nome)) {
                // nome não encontrado, continua na lista
                texto += x + "\n";
            }
            else if (posicao < 0) {
                posicao = i;
            }
            
            // incrementa contador
            i++;
        }

        // adiciona nomes na tela
        this.getGui().getjTextAreaClientes().setText(texto);

        // remover o cliente da lista de reservas
        for (VoCliente voAux : this.getClientes()) {
            // compara os nomes
            if (voAux.getCodigo() == this.getClientes().get(posicao).getCodigo()) {
                // achou, remove da lista de clientes da reserva
                this.getClientes().remove(voAux);
                
                // para laço
                break;
            }
        }
    }      

    /**
     *
     */
    @Override
    public void salvar() {
        // verifica se ao menos um cliente foi selecionado
        if (this.getGui().getjTextAreaClientes().getText().isEmpty()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), "Um cliente deve ser "
                    + "selecionado para reservar este horário!",
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // create vo
        this.setVo(new VoHorarioSquash());
        this.getVo().setDia(this.getGui().getjComboBoxDia().getSelectedIndex() + 1);
        this.getVo().setHorario(this.getGui().getjComboBoxHorario().getSelectedItem().toString());
        this.getVo().setQuadra(this.getGui().getjComboBoxQuadra().getSelectedIndex() + 1);
        this.getVo().setJogo(this.getGui().getjRadioButtonJogo().isSelected());
        this.getVo().setClientes(this.getClientes());

        // salvar
        this.getBo().setVo(this.getVo());
        if (!this.getBo().salvar()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), "Reserva de Horário Cadastrada com sucesso",
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
            // pede se quer limpar os campos
            int resp = JOptionPane.showConfirmDialog(this.getGui(),
                    "Desejas incluir mais uma reserva para estes clientes?",
                    "Cadastrar Horários Quadra de Squash", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.NO_OPTION) {
                this.limpar();
            }
        }
    }

    /**
     * *
     *
     */
    @Override
    public void excluir() {
        // verifica se ao menos um cliente foi selecionado
        if (this.getGui().getjTextAreaClientes().getText().isEmpty()) {
            // houve erro
            JOptionPane.showMessageDialog(this.getGui(), "Um cliente deve ser "
                    + "selecionado para excluir a reserva deste horário!",
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // pergunta se quer realmente excluir
        int resp = JOptionPane.showConfirmDialog(this.getGui(), "Desejas realmente excluir"
                + " esta reserva do cliente?",
                "Cadastrar Horários Quadra de Squash", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.NO_OPTION) {
            return;
        }

        // create vo
        this.setVo(new VoHorarioSquash());
        this.getVo().setDia(this.getGui().getjComboBoxDia().getSelectedIndex() + 1);
        this.getVo().setHorario(this.getGui().getjComboBoxHorario().getSelectedItem().toString());
        this.getVo().setQuadra(this.getGui().getjComboBoxQuadra().getSelectedIndex() + 1);
        this.getVo().setJogo(this.getGui().getjRadioButtonJogo().isSelected());
        this.getVo().setClientes(this.getClientes());

        // excluir
        this.getBo().setVo(this.getVo());
        if (this.getBo().excluir()) {
            JOptionPane.showMessageDialog(this.getGui(), "Reserva de Horário excluída com sucesso",
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
        }

        // limpar tela
        this.limpar();
    }

    /**
     * *
     *
     */
    @Override
    public void obterLista() {
        // set vo
        this.setVo(new VoHorarioSquash());

        // pergunta se quer realmente excluir
        int resp = JOptionPane.showConfirmDialog(this.getGui(),
                "Desejas fazer uma consulta para as reservas com o dia selecionado (Sim)?"
                + "\nOu desejar consultar as reservas cadastradas para todos os dias da semana(Não)?",
                "Cadastrar Horários Quadra de Squash", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            // set vo
            this.setVo(new VoHorarioSquash());
            this.getVo().setDia(this.getGui().getjComboBoxDia().getSelectedIndex() + 1);
            this.getVo().setHorario(this.getGui().getjComboBoxHorario().getSelectedItem().toString());
            this.getVo().setQuadra(this.getGui().getjComboBoxQuadra().getSelectedIndex() + 1);
        }

        // obter lista de clientes
        this.getBo().setVo(this.getVo());
        this.getBo().setErro("");
        ResultSet rs = this.getBo().obterLista();
        if (!this.getBo().getErro().isEmpty()) {
            JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                    "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // cria objeto controlador da tela de consulta
        String title = "Consulta lista de reservas de clientes";
        CoConsulta controllerConsulta = new CoConsulta((JFrame) this.getGui().getParent(),
                true, rs, title, DaoTabelas.HorariosSquash);

        // mostrar consulta
        controllerConsulta.consultar();

        // pega retorno
        if (controllerConsulta.isRetorno()) {
            // pega objeto selecionado
            ArrayList objeto = controllerConsulta.getObjetoConsulta();

            // cria objeto vo
            this.setVo(new VoHorarioSquash(Integer.parseInt(objeto.get(0).toString()),
                    Integer.parseInt(objeto.get(2).toString()),
                    objeto.get(1).toString()));

            // set vo em bo
            this.getBo().setVo(this.getVo());

            // recupera todos os clientes deste horário
            if (!this.getBo().consultarCodigo(0)) {
                JOptionPane.showMessageDialog(this.getGui(), this.getBo().getErro(),
                        "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // setar vo
            this.setVo(this.getBo().getVo());

            // mostrar campos
            this.setCampos();
        }
    }

    /**
     * *
     *
     */
    @Override
    public void limpar() {
        this.getGui().getjComboBoxDia().setSelectedIndex(0);
        this.getGui().getjComboBoxHorario().setSelectedIndex(0);
        this.getGui().getjComboBoxQuadra().setSelectedIndex(0);
        this.getGui().getjComboBoxCliente().setSelectedIndex(0);
        this.getGui().getjRadioButtonAula().setSelected(false);
        this.getGui().getjRadioButtonJogo().setSelected(true);
        this.setarValor("jogo");
        this.getGui().getjTextAreaClientes().setText("");
        this.setClientes(new ArrayList<VoCliente>());
    }

    /**
     * *
     *
     */
    @Override
    public void setCampos() {
        this.getGui().getjComboBoxDia().setSelectedIndex(this.getVo().getDia() - 1);
        this.getGui().getjComboBoxHorario().setSelectedItem(this.getVo().getHorario());
        this.getGui().getjComboBoxQuadra().setSelectedIndex(this.getVo().getQuadra() - 1);
        this.getGui().getjComboBoxCliente().setSelectedItem(this.getVo().getClientes().get(0).getNome());
        this.getGui().getjRadioButtonAula().setSelected(!this.getVo().isJogo());
        this.getGui().getjRadioButtonJogo().setSelected(this.getVo().isJogo());
        this.setarValor(this.getVo().isJogo());
        this.setClientes(this.getVo().getClientes());

        // adiciona nome dos clientes
        this.getGui().getjTextAreaClientes().setText("");
        for (VoCliente voAux : this.getVo().getClientes()) {
            this.getGui().getjTextAreaClientes().append(voAux.getNome() + "\n");
        }
    }



    /**
     * *
     * seta o valor da aula ou da reserva da quadra
     */
    public void setarValor(String texto) {
        switch (texto) {
            case "aula":
                this.getGui().getjTextFieldValor().setText(String.valueOf(this.getValorAula()));
                break;
            case "jogo":
                this.getGui().getjTextFieldValor().setText(String.valueOf(this.getValorQuadra()));
                break;
        }
    }

    /**
     * *
     * seta o valor da aula ou da reserva da quadra
     */
    public void setarValor(boolean jogo) {
        if (jogo) {
            this.getGui().getjTextFieldValor().setText(String.valueOf(this.getValorQuadra()));
        } else {
            this.getGui().getjTextFieldValor().setText(String.valueOf(this.getValorAula()));
        }
    }

    /**
     * *
     *
     */
    @Override
    public void consultaCodigo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * getters and setters
     *
     * @return
     */
    public GuiHorariosSquash getGui() {
        return gui;
    }

    public VoHorarioSquash getVo() {
        return vo;
    }

    public void setVo(VoHorarioSquash vo) {
        this.vo = vo;
    }

    public BoHorarioSquash getBo() {
        return bo;
    }

    public void setBo(BoHorarioSquash bo) {
        this.bo = bo;
    }

    public double getValorAula() {
        this.valorAula = gui.getGui().getController().getGuiValores().getCo().getVo().getValorAulaSquash();
        return valorAula;
    }

    public double getValorQuadra() {
        this.valorQuadra = gui.getGui().getController().getGuiValores().getCo().getVo().getValorSquashMensal1();
        return valorQuadra;
    }

    private ArrayList<VoCliente> getClientes() {
        return clientes;
    }

    private void setClientes(ArrayList<VoCliente> clientes) {
        this.clientes = clientes;
    }

    private ArrayList<VoCliente> getClientesCadastrados() {
        return clientesCadastrados;
    }

    private void setClientesCadastrados(ArrayList<VoCliente> clientesCadastrados) {
        this.clientesCadastrados = clientesCadastrados;
    }
}
