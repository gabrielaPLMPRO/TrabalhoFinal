
package br.ucs.dante.controller;

import br.ucs.dante.model.Cliente;
import br.ucs.dante.repository.ClienteRepository;
import br.ucs.dante.repository.DatabaseConnection;
import br.ucs.dante.service.ClienteService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class ClienteController {

    @FXML private TextField campoCodigo;
    @FXML private TextField campoNome;
    @FXML private TextField campoCpf;
    @FXML private DatePicker campoAniversario;
    @FXML private TextField campoTelefone;

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, String> colunaCodigo;
    @FXML private TableColumn<Cliente, String> colunaNome;
    @FXML private TableColumn<Cliente, String> colunaCpf;
    @FXML private TableColumn<Cliente, String> colunaTelefone;

    private ClienteService service;
    private Integer codigoEmEdicao;

    private final DatabaseConnection database;

    public ClienteController(DatabaseConnection database) {
        this.database = java.util.Objects.requireNonNull(database);
    }

    @FXML
    private void initialize() {


        service = new ClienteService(
            new ClienteRepository(database)
        );

        colunaCodigo.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(d.getValue().getCodigo())
            )
        );

        colunaNome.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNome())
        );

        colunaCpf.setCellValueFactory(d ->
            new SimpleStringProperty(
                texto(d.getValue().getCpf())
            )
        );

        colunaTelefone.setCellValueFactory(d ->
            new SimpleStringProperty(
                texto(d.getValue().getTelefone())
            )
        );

        tabelaClientes.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, anterior, atual) -> {
                if (atual != null) {
                    preencherFormulario(atual);
                }
            });

        carregarClientes();
    }

    private String texto(String valor) {
        return valor == null ? "" : valor;
    }

    private boolean verificarServico() {
        if (service == null) {
            alerta(Alert.AlertType.ERROR,
                "Conexão",
                "Serviço de clientes não configurado.");
            return false;
        }
        return true;
    }

    private void carregarClientes() {
        if (!verificarServico()) return;

        try {
            tabelaClientes.getItems().setAll(service.listar());
        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }



private Cliente lerFormulario() {
    Integer codigo = codigoEmEdicao;

    if (codigo == null && !campoCodigo.getText().isBlank()) {
        try {
            codigo = Integer.parseInt(
                campoCodigo.getText().trim()
            );
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                "O código deve ser um número inteiro."
            );
        }
    }

    return new Cliente(
        codigo,
        campoNome.getText().trim(),
        campoCpf.getText().trim(),
        campoAniversario.getValue(),
        campoTelefone.getText().trim()
    );
}

@FXML
private void salvar() {
    if (!verificarServico()) return;

    try {
        Cliente cliente = lerFormulario();

        if (codigoEmEdicao == null) {
            // Novo cadastro
            if (cliente.getCodigo() != null) {
                Cliente existente = service.buscarPorCodigo(
                    cliente.getCodigo()
                );

                if (existente != null) {
                    alerta(
                        Alert.AlertType.WARNING,
                        "Cliente já cadastrado",
                        "O código " + cliente.getCodigo() +
                        " já está cadastrado."
                    );
                    return;
                }
            }
        } else {
            // Edicao de cliente selecionado ou pesquisado
            if (!String.valueOf(codigoEmEdicao).equals(
                    campoCodigo.getText().trim())) {

                alerta(
                    Alert.AlertType.WARNING,
                    "Código inválido",
                    "Não é permitido alterar o código " +
                    "de um cliente existente."
                );
                return;
            }
        }

        service.salvar(cliente);

        alerta(
            Alert.AlertType.INFORMATION,
            "Sucesso",
            "Cliente salvo com sucesso! Código: " +
            cliente.getCodigo()
        );

        limpar();
        carregarClientes();

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
        if (!verificarServico()) return;

        String valor = campoCodigo.getText().trim();

        if (valor.isEmpty()) {
            carregarClientes();
            return;
        }

        try {
            int codigo = Integer.parseInt(valor);
            Cliente cliente = service.buscarPorCodigo(codigo);

            if (cliente == null) {
                alerta(Alert.AlertType.INFORMATION,
                    "Pesquisa", "Cliente não encontrado.");
            } else {
                preencherFormulario(cliente);
            }

        } catch (NumberFormatException erro) {
            alerta(Alert.AlertType.WARNING,
                "Validação", "Informe um código numérico.");
        } catch (IllegalArgumentException erro) {
            alerta(Alert.AlertType.WARNING,
                "Validação", erro.getMessage());
        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }

    @FXML
    private void excluir() {
        if (!verificarServico()) return;

        try {
            String valor = campoCodigo.getText().trim();

            if (valor.isEmpty()) {
                throw new IllegalArgumentException(
                    "Informe ou selecione um cliente."
                );
            }

            int codigo = Integer.parseInt(valor);

            Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir este cliente?",
                ButtonType.YES,
                ButtonType.NO
            );

            confirmacao.setHeaderText("Confirmar exclusão");

            if (confirmacao.showAndWait()
                    .orElse(ButtonType.NO) != ButtonType.YES) {
                return;
            }

            if (service.excluir(codigo)) {
                alerta(Alert.AlertType.INFORMATION,
                    "Sucesso", "Cliente excluído.");
                limpar();
                carregarClientes();
            } else {
                alerta(Alert.AlertType.INFORMATION,
                    "Exclusão", "Cliente não encontrado.");
            }

        } catch (NumberFormatException erro) {
            alerta(Alert.AlertType.WARNING,
                "Validação", "Código inválido.");
        } catch (IllegalArgumentException | IllegalStateException erro) {
            alerta(Alert.AlertType.WARNING,
                "Exclusão", erro.getMessage());
        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }


@FXML
private void limpar() {
    codigoEmEdicao = null;

    tabelaClientes.getSelectionModel().clearSelection();

    campoCodigo.clear();
    campoNome.clear();
    campoCpf.clear();
    campoAniversario.setValue(null);
    campoTelefone.clear();

    campoCodigo.requestFocus();
}

private void preencherFormulario(Cliente cliente) {
    codigoEmEdicao = cliente.getCodigo();

    campoCodigo.setText(
        String.valueOf(cliente.getCodigo())
    );

    campoNome.setText(cliente.getNome());
    campoCpf.setText(texto(cliente.getCpf()));
    campoAniversario.setValue(cliente.getDataAniversario());
    campoTelefone.setText(texto(cliente.getTelefone()));
}


    private void mostrarErro(SQLException erro) {
        alerta(Alert.AlertType.ERROR,
            "Erro no banco de dados",
            erro.getMessage());
    }

    private void alerta(
            Alert.AlertType tipo,
            String titulo,
            String mensagem) {

        Alert janela = new Alert(tipo, mensagem, ButtonType.OK);
        janela.setTitle(titulo);
        janela.setHeaderText(null);
        janela.showAndWait();
    }
}
