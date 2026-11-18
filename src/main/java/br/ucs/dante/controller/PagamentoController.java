
package br.ucs.dante.controller;

import br.ucs.dante.model.Cliente;
import br.ucs.dante.model.Pagamento;
import br.ucs.dante.repository.ClienteRepository;
import br.ucs.dante.repository.DatabaseConnection;
import br.ucs.dante.repository.PagamentoRepository;
import br.ucs.dante.service.PagamentoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PagamentoController {

    @FXML private ComboBox<Cliente> campoCliente;
    @FXML private DatePicker campoData;
    @FXML private TextField campoSequencia;
    @FXML private TextField campoValor;
    @FXML private CheckBox campoPagamentoTotal;

    @FXML private TableView<Pagamento> tabelaPagamentos;
    @FXML private TableColumn<Pagamento, String> colunaCliente;
    @FXML private TableColumn<Pagamento, String> colunaData;
    @FXML private TableColumn<Pagamento, String> colunaSequencia;
    @FXML private TableColumn<Pagamento, String> colunaValor;
    @FXML private TableColumn<Pagamento, String> colunaPagamentoTotal;

    private ClienteRepository clientes;
    private PagamentoRepository pagamentos;
    private PagamentoService service;

    private Pagamento pagamentoEmEdicao;

    private final Locale brasil =
        Locale.forLanguageTag("pt-BR");

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private final DatabaseConnection database;

    public PagamentoController(DatabaseConnection database) {
        this.database = java.util.Objects.requireNonNull(database);
    }

    @FXML
    private void initialize() {


        clientes = new ClienteRepository(database);
        pagamentos = new PagamentoRepository(database);
        service = new PagamentoService();

        configurarCliente();
        configurarTabela();

        tabelaPagamentos.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, anterior, atual) -> {
                if (atual != null) {
                    preencherFormulario(atual);
                }
            });

        carregarDados();
        limpar();
    }

    private void configurarCliente() {
        campoCliente.setConverter(new StringConverter<Cliente>() {

            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) return "";

                return cliente.getCodigo() +
                    " - " + cliente.getNome();
            }

            @Override
            public Cliente fromString(String texto) {
                return null;
            }
        });
    }

    private void configurarTabela() {
        colunaCliente.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(
                    d.getValue().getCodigoCliente()
                )
            )
        );

        colunaData.setCellValueFactory(d ->
            new SimpleStringProperty(
                d.getValue().getData().format(formatoData)
            )
        );

        colunaSequencia.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(d.getValue().getSequencia())
            )
        );

        colunaValor.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.format(
                    brasil,
                    "%.2f",
                    d.getValue().getValor()
                )
            )
        );

        colunaPagamentoTotal.setCellValueFactory(d ->
            new SimpleStringProperty(
                Boolean.TRUE.equals(
                    d.getValue().getPagamentoTotal()
                ) ? "Sim" : "Não"
            )
        );
    }

    private boolean verificarConfiguracao() {
        if (clientes == null ||
                pagamentos == null ||
                service == null) {

            alerta(
                Alert.AlertType.ERROR,
                "Configuração",
                "O módulo de Pagamentos não foi configurado."
            );

            return false;
        }

        return true;
    }

    private void carregarDados() {
        if (!verificarConfiguracao()) return;

        try {
            campoCliente.getItems().setAll(clientes.listar());

            tabelaPagamentos.getItems().setAll(
                pagamentos.listar()
            );

        } catch (SQLException erro) {
            mostrarErro(erro);
        } catch (RuntimeException erro) {
            alerta(
                Alert.AlertType.ERROR,
                "Erro ao carregar dados",
                erro.getMessage()
            );
        }
    }

    private int lerSequencia(String texto) {
        try {
            int sequencia = Integer.parseInt(texto.trim());

            if (sequencia <= 0) {
                throw new NumberFormatException();
            }

            return sequencia;

        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                "A sequência deve ser um número inteiro positivo."
            );
        }
    }

    private double lerValor() {
        String texto = campoValor.getText().trim();

        if (texto.isEmpty()) {
            throw new IllegalArgumentException(
                "Informe o valor do pagamento."
            );
        }

        try {
            return Double.parseDouble(
                texto.replace(",", ".")
            );

        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                "Informe um valor válido. Exemplo: 50,00."
            );
        }
    }

    private Pagamento lerFormulario() {
        Cliente cliente = campoCliente.getValue();

        if (cliente == null) {
            throw new IllegalArgumentException(
                "Selecione um cliente."
            );
        }

        if (campoData.getValue() == null) {
            throw new IllegalArgumentException(
                "Informe a data do pagamento."
            );
        }

        Integer sequencia = pagamentoEmEdicao == null
            ? null
            : pagamentoEmEdicao.getSequencia();

        return new Pagamento(
            cliente.getCodigo(),
            campoData.getValue(),
            sequencia,
            lerValor(),
            campoPagamentoTotal.isSelected()
        );
    }

    private boolean chaveOriginalPreservada() {
        if (pagamentoEmEdicao == null) {
            return true;
        }

        Cliente cliente = campoCliente.getValue();

        if (cliente == null || campoData.getValue() == null) {
            return false;
        }

        return cliente.getCodigo().equals(
                    pagamentoEmEdicao.getCodigoCliente())
            && campoData.getValue().equals(
                    pagamentoEmEdicao.getData())
            && campoSequencia.getText().trim().equals(
                    String.valueOf(
                        pagamentoEmEdicao.getSequencia()
                    )
                );
    }

    @FXML
    private void salvar() {
        if (!verificarConfiguracao()) return;

        try {
            if (pagamentoEmEdicao == null &&
                    !campoSequencia.getText().isBlank()) {

                alerta(
                    Alert.AlertType.WARNING,
                    "Novo pagamento",
                    "Para cadastrar, deixe a sequência vazia. " +
                    "Para editar, pesquise ou selecione um pagamento."
                );
                return;
            }

            if (!chaveOriginalPreservada()) {
                alerta(
                    Alert.AlertType.WARNING,
                    "Identificação do pagamento",
                    "Não é permitido alterar cliente, data " +
                    "ou sequência de um pagamento existente."
                );
                return;
            }

            Pagamento pagamento = lerFormulario();
            service.validar(pagamento);

            if (pagamentoEmEdicao == null) {
                pagamentos.cadastrar(pagamento);

            } else {
                if (!pagamentos.atualizar(pagamento)) {
                    alerta(
                        Alert.AlertType.WARNING,
                        "Pagamento não encontrado",
                        "O pagamento não existe mais no banco."
                    );
                    return;
                }
            }

            alerta(
                Alert.AlertType.INFORMATION,
                "Sucesso",
                "Pagamento salvo! Sequência: " +
                pagamento.getSequencia()
            );

            carregarDados();
            limpar();

        } catch (IllegalArgumentException erro) {
            alerta(
                Alert.AlertType.WARNING,
                "Validação",
                erro.getMessage()
            );

        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }

    @FXML
    private void pesquisar() {
        if (!verificarConfiguracao()) return;

        try {
            Cliente cliente = campoCliente.getValue();
            LocalDate data = campoData.getValue();

            if (cliente == null || data == null) {
                throw new IllegalArgumentException(
                    "Selecione o cliente e a data."
                );
            }

            if (campoSequencia.getText().isBlank()) {
                throw new IllegalArgumentException(
                    "Informe a sequência para pesquisar."
                );
            }

            int sequencia = lerSequencia(
                campoSequencia.getText()
            );

            Pagamento encontrado = pagamentos.buscar(
                cliente.getCodigo(),
                data,
                sequencia
            );

            if (encontrado == null) {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Pesquisa",
                    "Pagamento não encontrado."
                );
                return;
            }

            preencherFormulario(encontrado);

        } catch (IllegalArgumentException erro) {
            alerta(
                Alert.AlertType.WARNING,
                "Pesquisa",
                erro.getMessage()
            );

        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }

    @FXML
    private void excluir() {
        if (!verificarConfiguracao()) return;

        try {
            if (pagamentoEmEdicao == null) {
                throw new IllegalArgumentException(
                    "Pesquise ou selecione um pagamento para excluir."
                );
            }

            Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir este pagamento?",
                ButtonType.YES,
                ButtonType.NO
            );

            confirmacao.setHeaderText("Confirmar exclusão");

            if (confirmacao.showAndWait()
                    .orElse(ButtonType.NO) != ButtonType.YES) {
                return;
            }

            boolean excluido = pagamentos.excluir(
                pagamentoEmEdicao.getCodigoCliente(),
                pagamentoEmEdicao.getData(),
                pagamentoEmEdicao.getSequencia()
            );

            if (excluido) {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Pagamento excluído."
                );

                carregarDados();
                limpar();

            } else {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Exclusão",
                    "Pagamento não encontrado."
                );
            }

        } catch (IllegalArgumentException erro) {
            alerta(
                Alert.AlertType.WARNING,
                "Exclusão",
                erro.getMessage()
            );

        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }

    @FXML
    private void limpar() {
        pagamentoEmEdicao = null;

        tabelaPagamentos.getSelectionModel()
            .clearSelection();

        campoCliente.setValue(null);
        campoData.setValue(LocalDate.now());
        campoSequencia.clear();
        campoValor.clear();
        campoPagamentoTotal.setSelected(false);

        campoCliente.requestFocus();
    }

    private void preencherFormulario(Pagamento pagamento) {
        pagamentoEmEdicao = pagamento;

        campoCliente.getItems().stream()
            .filter(cliente ->
                cliente.getCodigo().equals(
                    pagamento.getCodigoCliente()
                )
            )
            .findFirst()
            .ifPresent(campoCliente::setValue);

        campoData.setValue(pagamento.getData());

        campoSequencia.setText(
            String.valueOf(pagamento.getSequencia())
        );

        campoValor.setText(
            String.format(
                brasil,
                "%.2f",
                pagamento.getValor()
            )
        );

        campoPagamentoTotal.setSelected(
            Boolean.TRUE.equals(
                pagamento.getPagamentoTotal()
            )
        );
    }

    private void mostrarErro(SQLException erro) {
        alerta(
            Alert.AlertType.ERROR,
            "Erro no banco de dados",
            erro.getMessage()
        );
    }

    private void alerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem) {

        Alert janela = new Alert(
            tipo,
            mensagem,
            ButtonType.OK
        );

        janela.setTitle(titulo);
        janela.setHeaderText(null);
        janela.showAndWait();
    }
}
