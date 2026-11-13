/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller.servicos;

import dao.bd.E_BD;
import gui.view.serviços.GuiSquash;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import model.bo.bd.BoConexao;
import model.bo.servicos.BoSquash;
import utils.ValidarStrings;

/**
 *
 * @author Daniel
 */
public class CoSquash {
    // atributos

    private boolean configuracaoInicial;
    private BoSquash bo;
    private GuiSquash gui;
    private ArrayList<String> horarios;

    // construtor
    public CoSquash(GuiSquash gui, BoConexao conexao) {
        this.bo = new BoSquash(conexao);
        this.gui = gui;
        this.configuracaoInicial = true;
        this.horarios = new ArrayList<>();
    }

    /**
     * configuração inicial
     */
    public void configuracaoInicial() {
        // testa se a configuração já foi feita
        if (!this.configuracaoInicial) {
            return;
        }

        // set configuração inicial
        this.configuracaoInicial = false;

        try {
            // busca o tamanho da lista de horários
            JComboBox combo = this.getGui().getGui().getController().getGuiHorariosSquash().getjComboBoxHorario();
            int tamanho = combo.getModel().getSize();

            // testa tamanho da lista de horarios
            if (tamanho == 0) {
                // erro ao recuperar os horários
                JOptionPane.showMessageDialog(this.getGui(),
                        "(1)Não foi possível localizar os horários das reservas de squash!"
                        + "\nCom isto, não será possível montar a grade de horários!",
                        "Cadastrar Horários Quadra de Squash", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // busca a lista de horários
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < tamanho; i++) {
                // pega o horário
                String aux = (String) combo.getModel().getElementAt(i);

                // adiciona na lista
                temp.add(aux);
            }

            // atribui os horários
            this.setHorarios(temp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "(2)Não foi possível localizar os horários das reservas de squash!"
                    + "\nCom isto, não será possível montar a grade de horários!",
                    "Cadastrar Horários Quadra de Squash\n" + e, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * *
     * carregar a grade
     */
    public void carregarGrade() {
        // pegar o dia da semana da data selecionada
        int dia = ValidarStrings.getDiaSemana((Calendar) this.getGui().getjComboBoxData().getSelectedItem());

        // monta a grade
        this.montarGradeSquash(dia);
    }

    /**
     * monta a grade para as três quadras
     */
    private void montarGradeSquash(int dia) {
        // variáveis temporárias
        ArrayList<Object[]> gradeQuadra = new ArrayList<>();
        try {
            // varre a lista de horarios
            for (String horario : this.getHorarios()) {
                // cria uma variável temporária
                Object reserva[] = new Object[4];

                // seta horario
                reserva[0] = horario;

                // recupera as informações para o dia, horário e quadra 1
                ResultSet rs1 = this.getBo().obterLista(dia, horario, 1);

                // recupera as informações para o dia, horário e quadra 2
                ResultSet rs2 = this.getBo().obterLista(dia, horario, 2);

                // recupera as informações para o dia, horário e quadra 3
                ResultSet rs3 = this.getBo().obterLista(dia, horario, 3);

                // percorre o resultado da quadra 1
                String aux = " ";
                boolean jogo = false;
                while (rs1.next()) {
                    // pega o resultado
                    if (!aux.equals(" ")) {
                        aux += ", \n";
                    }
                    aux += rs1.getString(1);
                    jogo = rs1.getBoolean(2);
                }

                // verifica se é aula
                if ((!aux.equals(" ")) && (!jogo)) {
                    aux += " (AULA)";
                }

                // adiciona o resultado da quadra 1
                reserva[1] = aux;

                // percorre o resultado da quadra 2
                aux = " ";
                jogo = false;
                while (rs2.next()) {
                    // pega o resultado
                    if (!aux.equals(" ")) {
                        aux += ", \n";
                    }
                    aux += rs2.getString(1);
                    jogo = rs2.getBoolean(2);
                }

                // verifica se é aula
                if ((!aux.equals(" ")) && (!jogo)) {
                    aux += " (AULA)";
                }

                // adiciona o resultado da quadra 2
                reserva[2] = aux;

                // percorre o resultado da quadra 3
                aux = " ";
                jogo = false;
                while (rs3.next()) {
                    // pega o resultado
                    if (!aux.equals(" ")) {
                        aux += ", \n";
                    }
                    aux += rs3.getString(1);
                    jogo = rs3.getBoolean(2);
                }

                // verifica se é aula
                if ((!aux.equals(" ")) && (!jogo)) {
                    aux += " (AULA)";
                }

                // adiciona o resultado da quadra 2
                reserva[3] = aux;

                // adiciona no hashmap
                gradeQuadra.add(reserva);
            }
        } catch (SQLException | E_BD | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.getGui(),
                    "(3)Não foi possível localizar os horários das reservas de squash!"
                    + "\nCom isto, não será possível montar a grade de horários!",
                    "Cadastrar Horários Quadra de Squash\n" + ex, JOptionPane.ERROR_MESSAGE);
        }

        // montar grade 
        this.montarJTable(gradeQuadra);
    }

    /**
     * monta o jtable
     */
    private void montarJTable(ArrayList<Object[]> gradeQuadra) {
        // criar cabecalho
        Object[] vetorColunas = new Object[]{"Hora", "Quadra 1", "Quadra 2", "Quadra 3"};

        // modelo tabela
        DefaultTableModel dataModel = new DefaultTableModel(vetorColunas, 0);

        // adiciona linhas
        for (Object[] aux : gradeQuadra) {
            // adiciona a linha
            dataModel.addRow(aux);
        }

        // montar jtable
        this.getGui().setjTableGrade(new JTable(dataModel));


        // configurar coluna quadra 1
        TableColumnModel colModel = this.getGui().getjTableGrade().getColumnModel();
        TableColumn col = colModel.getColumn(1);
        col.setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row, int column) {
                JTextArea textArea = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                textArea.setText((String) value);
                textArea.setFont(new java.awt.Font("Arial", 1, 15));
                scrollPane.setToolTipText((String) value);
                return scrollPane;
            }
        });

        // configurar coluna quadra 2
        TableColumnModel colModel2 = this.getGui().getjTableGrade().getColumnModel();
        TableColumn col2 = colModel2.getColumn(2);
        col2.setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row, int column) {
                JTextArea textArea = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                textArea.setText((String) value);
                textArea.setFont(new java.awt.Font("Arial", 1, 15));
                scrollPane.setToolTipText((String) value);
                return scrollPane;
            }
        });

       // configurar coluna quadra 3
        TableColumnModel colModel3 = this.getGui().getjTableGrade().getColumnModel();
        TableColumn col3 = colModel3.getColumn(3);
        col3.setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row, int column) {
                JTextArea textArea = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                textArea.setText((String) value);
                textArea.setFont(new java.awt.Font("Arial", 1, 15));
                scrollPane.setToolTipText((String) value);
                return scrollPane;
            }
        });        
        
        // configurar jtable
        this.getGui().getjTableGrade().setFillsViewportHeight(true);
        this.getGui().getjTableGrade().setFont(new java.awt.Font("Arial", 1, 24));
        this.getGui().getjTableGrade().setEnabled(false);
        this.getGui().getjTableGrade().setRowHeight(100);
        this.getGui().getjTableGrade().setBorder(new BevelBorder(1, Color.black, Color.black));
        this.getGui().getjTableGrade().setShowGrid(true);
        this.getGui().getjTableGrade().getTableHeader().getColumnModel().getColumn(0).setMaxWidth(90);
        this.getGui().getjTableGrade().getTableHeader().getColumnModel().getColumn(1).setMaxWidth(240);
        this.getGui().getjTableGrade().getTableHeader().getColumnModel().getColumn(2).setMaxWidth(240);
        this.getGui().getjTableGrade().getTableHeader().getColumnModel().getColumn(3).setMaxWidth(240);
        JTableHeader header = this.getGui().getjTableGrade().getTableHeader();
        header.setForeground(Color.black);
        header.setFont(new java.awt.Font("Arial", 1, 24));
        header.setBorder(new BevelBorder(1, Color.black, Color.black));

        // adiciona ao container
        Rectangle aux = this.getGui().getjScrollPane5().getBounds();
        this.getGui().remove(this.getGui().getjScrollPane5());
        this.getGui().setjScrollPane5(new JScrollPane(this.getGui().getjTableGrade()));
        this.getGui().getjScrollPane5().setBounds(aux);
        this.getGui().add(this.getGui().getjScrollPane5());
    }

    /**
     * *
     * mostrar tela de horarios
     */
    public void mostrarTelaHorarios() {
        this.getGui().getGui().getController().mostrarTelaHorariosSquash();
    }

    /**
     * *
     * mostrar tela de horarios
     */
    public void mostrarTelaConsumos() {
        this.getGui().getGui().getController().mostrarTelaConsumo();
    }

    /**
     * *
     * mostrar tela de pagamentos
     */
    public void mostrarTelaPagamentos() {
        this.getGui().getGui().getController().mostrarTelaPagamento();
    }

    /**
     * getters and setters
     */
    public BoSquash getBo() {
        return bo;
    }

    public GuiSquash getGui() {
        return gui;
    }

    private ArrayList<String> getHorarios() {
        return horarios;
    }

    private void setHorarios(ArrayList<String> horarios) {
        this.horarios = horarios;
    }
}
