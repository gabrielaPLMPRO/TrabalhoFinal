
package br.ucs.dante.controller;

import br.ucs.dante.model.Cliente;
import br.ucs.dante.model.Produto;
import br.ucs.dante.model.Consumo;

import br.ucs.dante.repository.DatabaseConnection;
import br.ucs.dante.repository.ClienteRepository;
import br.ucs.dante.repository.ProdutoRepository;
import br.ucs.dante.repository.ConsumoRepository;

import br.ucs.dante.service.ConsumoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConsumoController {

    @FXML private ComboBox<Cliente> campoCliente;
    @FXML private DatePicker campoData;
    @FXML private TextField campoSequencia;
    @FXML private ComboBox<Produto> campoProduto;
    @FXML private TextField campoQuantidade;
    @FXML private TextField campoValor;
    @FXML private CheckBox campoPago;

    @FXML private TableView<Consumo> tabelaConsumos;
    @FXML private TableColumn<Consumo, String> colunaCliente;
    @FXML private TableColumn<Consumo, String> colunaData;
    @FXML private TableColumn<Consumo, String> colunaSequencia;
    @FXML private TableColumn<Consumo, String> colunaProduto;
    @FXML private TableColumn<Consumo, String> colunaQuantidade;
    @FXML private TableColumn<Consumo, String> colunaValor;
    @FXML private TableColumn<Consumo, String> colunaPago;

    private ClienteRepository clientes;
    private ProdutoRepository produtos;
    private ConsumoRepository consumos;
    private ConsumoService service;

    private Consumo consumoEmEdicao;

    private final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private final Locale brasil =
        Locale.forLanguageTag("pt-BR");

    @FXML
    private void initialize() {
        String url = System.getenv("DANTE_DB_URL");
        String usuario = System.getenv("DANTE_DB_USER");
        String senha = System.getenv("DANTE_DB_PASSWORD");

        if (url == null || usuario == null || senha == null) {
            alerta(
                Alert.AlertType.ERROR,
                "Configuração",
                "Configure as variáveis de conexão com o banco."
            );
            return;
        }

        DatabaseConnection database =
            new DatabaseConnection(url, usuario, senha);

        clientes = new ClienteRepository(database);
        produtos = new ProdutoRepository(database);
        consumos = new ConsumoRepository(database);
        service = new ConsumoService();

        configurarSeletores();
        configurarTabela();

        tabelaConsumos.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, anterior, atual) -> {
                if (atual != null) {
                    preencherFormulario(atual);
                }
            });

        carregarDados();
        limpar();
    }

    private void configurarSeletores() {
        campoCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) return "";
                return cliente.getCodigo() + " - " + cliente.getNome();
            }

            @Override
            public Cliente fromString(String texto) {
                return null;
            }
        });

        campoProduto.setConverter(new StringConverter<Produto>() {
            @Override
            public String toString(Produto produto) {
                if (produto == null) return "";
                return produto.getCodigo() + " - " + produto.getDescricao();
            }

            @Override
            public Produto fromString(String texto) {
                return null;
            }
        });
    }

    private void configurarTabela() {
        colunaCliente.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(d.getValue().getCodigoCliente())
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

        colunaProduto.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(d.getValue().getCodigoProduto())
            )
        );

        colunaQuantidade.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(d.getValue().getQuantidade())
            )
        );

        colunaValor.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.format(
                    brasil, "%.2f", d.getValue().getValor()
                )
            )
        );

        colunaPago.setCellValueFactory(d ->
            new SimpleStringProperty(
                Boolean.TRUE.equals(d.getValue().getPago())
                    ? "Sim" : "Não"
            )
        );
    }

    private boolean verificarConfiguracao() {
        if (clientes == null || produtos == null ||
                consumos == null || service == null) {
            alerta(
                Alert.AlertType.ERROR,
                "Configuração",
                "O módulo de Consumos não foi configurado."
            );
            return false;
        }
        return true;
    }

    private void carregarDados() {
        if (!verificarConfiguracao()) return;

        try {
            campoCliente.getItems().setAll(clientes.listar());
            campoProduto.getItems().setAll(produtos.listar());
            tabelaConsumos.getItems().setAll(consumos.listar());

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

    private int lerInteiro(String texto, String campo) {
        try {
            int numero = Integer.parseInt(texto.trim());

            if (numero <= 0) {
                throw new NumberFormatException();
            }

            return numero;

        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                campo + " deve ser um número inteiro positivo."
            );
        }
    }

    private double lerValor() {
        try {
            return Double.parseDouble(
                campoValor.getText().trim().replace(",", ".")
            );
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                "Informe um valor válido. Exemplo: 20,00."
            );
        }
    }

    private Consumo lerFormulario() {
        Cliente cliente = campoCliente.getValue();
        Produto produto = campoProduto.getValue();

        if (cliente == null) {
            throw new IllegalArgumentException(
                "Selecione um cliente."
            );
        }

        if (produto == null) {
            throw new IllegalArgumentException(
                "Selecione um produto."
            );
        }

        if (campoData.getValue() == null) {
            throw new IllegalArgumentException(
                "Informe a data."
            );
        }

        Integer sequencia = consumoEmEdicao == null
            ? null
            : consumoEmEdicao.getSequencia();

        return new Consumo(
            cliente.getCodigo(),
            campoData.getValue(),
            sequencia,
            produto.getCodigo(),
            lerInteiro(campoQuantidade.getText(), "Quantidade"),
            lerValor(),
            campoPago.isSelected()
        );
    }

    private boolean chaveOriginalPreservada() {
        if (consumoEmEdicao == null) return true;

        Cliente cliente = campoCliente.getValue();

        if (cliente == null || campoData.getValue() == null) {
            return false;
        }

        return cliente.getCodigo().equals(
                    consumoEmEdicao.getCodigoCliente())
            && campoData.getValue().equals(
                    consumoEmEdicao.getData())
            && campoSequencia.getText().trim().equals(
                    String.valueOf(consumoEmEdicao.getSequencia()));
    }

    @FXML
    private void salvar() {
        if (!verificarConfiguracao()) return;

        try {
            if (consumoEmEdicao == null &&
                    !campoSequencia.getText().isBlank()) {
                alerta(
                    Alert.AlertType.WARNING,
                    "Novo consumo",
                    "Para cadastrar, deixe a sequência vazia. " +
                    "Para editar, pesquise ou selecione um consumo."
                );
                return;
            }

            if (!chaveOriginalPreservada()) {
                alerta(
                    Alert.AlertType.WARNING,
                    "Identificação do consumo",
                    "Não é permitido alterar cliente, " +
                    "data ou sequência de um consumo existente."
                );
                return;
            }

            Consumo consumo = lerFormulario();
            service.validar(consumo);

            if (consumoEmEdicao == null) {
                consumos.cadastrar(consumo);
            } else {
                if (!consumos.atualizar(consumo)) {
                    alerta(
                        Alert.AlertType.WARNING,
                        "Registro não encontrado",
                        "O consumo não existe mais no banco."
                    );
                    return;
                }
            }

            alerta(
                Alert.AlertType.INFORMATION,
                "Sucesso",
                "Consumo salvo! Sequência: " +
                consumo.getSequencia()
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

            int sequencia = lerInteiro(
                campoSequencia.getText(), "Sequência"
            );

            Consumo encontrado = consumos.buscar(
                cliente.getCodigo(), data, sequencia
            );

            if (encontrado == null) {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Pesquisa",
                    "Consumo não encontrado."
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
            if (consumoEmEdicao == null) {
                throw new IllegalArgumentException(
                    "Pesquise ou selecione um consumo para excluir."
                );
            }

            Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir este consumo?",
                ButtonType.YES,
                ButtonType.NO
            );

            confirmacao.setHeaderText("Confirmar exclusão");

            if (confirmacao.showAndWait()
                    .orElse(ButtonType.NO) != ButtonType.YES) {
                return;
            }

            if (consumos.excluir(
                    consumoEmEdicao.getCodigoCliente(),
                    consumoEmEdicao.getData(),
                    consumoEmEdicao.getSequencia())) {

                alerta(
                    Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Consumo excluído."
                );

                carregarDados();
                limpar();

            } else {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Exclusão",
                    "Consumo não encontrado."
                );
            }

        } catch (IllegalArgumentException |
                 IllegalStateException erro) {
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
        consumoEmEdicao = null;

        tabelaConsumos.getSelectionModel().clearSelection();

        campoCliente.setValue(null);
        campoData.setValue(LocalDate.now());
        campoSequencia.clear();
        campoProduto.setValue(null);
        campoQuantidade.clear();
        campoValor.clear();
        campoPago.setSelected(false);

        campoCliente.requestFocus();
    }

    private void preencherFormulario(Consumo consumo) {
        consumoEmEdicao = consumo;

        campoCliente.getItems().stream()
            .filter(c -> c.getCodigo().equals(
                consumo.getCodigoCliente()))
            .findFirst()
            .ifPresent(campoCliente::setValue);

        campoData.setValue(consumo.getData());

        campoSequencia.setText(
            String.valueOf(consumo.getSequencia())
        );

        campoProduto.getItems().stream()
            .filter(p -> p.getCodigo().equals(
                consumo.getCodigoProduto()))
            .findFirst()
            .ifPresent(campoProduto::setValue);

        campoQuantidade.setText(
            String.valueOf(consumo.getQuantidade())
        );

        campoValor.setText(
            String.format(brasil, "%.2f", consumo.getValor())
        );

        campoPago.setSelected(consumo.getPago());
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
            tipo, mensagem, ButtonType.OK
        );

        janela.setTitle(titulo);
        janela.setHeaderText(null);
        janela.showAndWait();
    }
}
