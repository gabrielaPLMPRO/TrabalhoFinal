
package br.ucs.dante.controller;

import br.ucs.dante.model.Produto;
import br.ucs.dante.repository.DatabaseConnection;
import br.ucs.dante.repository.ProdutoRepository;
import br.ucs.dante.service.ProdutoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Locale;

public class ProdutoController {

    @FXML private TextField campoCodigo;
    @FXML private TextField campoDescricao;
    @FXML private TextField campoPreco;
    @FXML private TextField campoUnidade;

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colunaCodigo;
    @FXML private TableColumn<Produto, String> colunaDescricao;
    @FXML private TableColumn<Produto, String> colunaPreco;
    @FXML private TableColumn<Produto, String> colunaUnidade;

    private ProdutoRepository repository;
    private ProdutoService service;
    private Integer codigoEmEdicao;

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

        repository = new ProdutoRepository(database);
        service = new ProdutoService();

        colunaCodigo.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.valueOf(d.getValue().getCodigo())
            )
        );

        colunaDescricao.setCellValueFactory(d ->
            new SimpleStringProperty(
                d.getValue().getDescricao()
            )
        );

        colunaPreco.setCellValueFactory(d ->
            new SimpleStringProperty(
                String.format(
                    Locale.forLanguageTag("pt-BR"),
                    "%.2f",
                    d.getValue().getPreco()
                )
            )
        );

        colunaUnidade.setCellValueFactory(d ->
            new SimpleStringProperty(
                d.getValue().getUnidade()
            )
        );

        tabelaProdutos.getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, anterior, atual) -> {
                if (atual != null) {
                    preencherFormulario(atual);
                }
            });

        carregarProdutos();
    }

    private boolean verificarRepositorio() {
        if (repository == null || service == null) {
            alerta(
                Alert.AlertType.ERROR,
                "Configuração",
                "O módulo de Produtos não foi configurado."
            );
            return false;
        }

        return true;
    }

    private void carregarProdutos() {
        if (!verificarRepositorio()) return;

        try {
            tabelaProdutos.getItems().setAll(repository.listar());
        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }

    private int lerCodigo(String valor) {
        try {
            int codigo = Integer.parseInt(valor.trim());

            if (codigo <= 0) {
                throw new NumberFormatException();
            }

            return codigo;

        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                "O código deve ser um número inteiro positivo."
            );
        }
    }

    private double lerPreco() {
        String valor = campoPreco.getText().trim();

        if (valor.isEmpty()) {
            throw new IllegalArgumentException(
                "Informe o preço do produto."
            );
        }

        try {
            return Double.parseDouble(
                valor.replace(",", ".")
            );
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                "Informe um preço válido. Exemplo: 12,50."
            );
        }
    }

    private Produto lerFormulario() {
        Integer codigo = codigoEmEdicao;

        if (codigo == null && !campoCodigo.getText().isBlank()) {
            codigo = lerCodigo(campoCodigo.getText());
        }

        return new Produto(
            codigo,
            campoDescricao.getText().trim(),
            lerPreco(),
            campoUnidade.getText().trim()
        );
    }

    @FXML
    private void salvar() {
        if (!verificarRepositorio()) return;

        try {
            Produto produto = lerFormulario();

            if (codigoEmEdicao == null) {
                if (produto.getCodigo() != null &&
                    repository.existe(produto.getCodigo())) {

                    alerta(
                        Alert.AlertType.WARNING,
                        "Produto já cadastrado",
                        "O produto de código " +
                        produto.getCodigo() +
                        " já está cadastrado."
                    );
                    return;
                }
            } else {
                if (!String.valueOf(codigoEmEdicao).equals(
                        campoCodigo.getText().trim())) {

                    alerta(
                        Alert.AlertType.WARNING,
                        "Código inválido",
                        "Não é permitido alterar o código " +
                        "de um produto existente."
                    );
                    return;
                }
            }

            service.validar(produto);
            repository.salvar(produto);

            alerta(
                Alert.AlertType.INFORMATION,
                "Sucesso",
                "Produto salvo com sucesso! Código: " +
                produto.getCodigo()
            );

            limpar();
            carregarProdutos();

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
        if (!verificarRepositorio()) return;

        String valor = campoCodigo.getText().trim();

        if (valor.isEmpty()) {
            carregarProdutos();
            return;
        }

        try {
            int codigo = lerCodigo(valor);
            Produto produto = repository.buscarPorCodigo(codigo);

            if (produto == null) {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Pesquisa",
                    "Produto não encontrado."
                );
            } else {
                preencherFormulario(produto);
            }

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
    private void excluir() {
        if (!verificarRepositorio()) return;

        try {
            String valor = campoCodigo.getText().trim();

            if (valor.isEmpty()) {
                throw new IllegalArgumentException(
                    "Informe ou selecione um produto."
                );
            }

            int codigo = lerCodigo(valor);

            Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir este produto?",
                ButtonType.YES,
                ButtonType.NO
            );

            confirmacao.setHeaderText("Confirmar exclusão");

            if (confirmacao.showAndWait()
                    .orElse(ButtonType.NO) != ButtonType.YES) {
                return;
            }

            if (repository.excluir(codigo)) {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Produto excluído."
                );

                limpar();
                carregarProdutos();
            } else {
                alerta(
                    Alert.AlertType.INFORMATION,
                    "Exclusão",
                    "Produto não encontrado."
                );
            }

        } catch (IllegalArgumentException erro) {
            alerta(
                Alert.AlertType.WARNING,
                "Exclusão",
                erro.getMessage()
            );
        } catch (IllegalStateException erro) {
            alerta(
                Alert.AlertType.WARNING,
                "Exclusão impedida",
                erro.getMessage()
            );
        } catch (SQLException erro) {
            mostrarErro(erro);
        }
    }

    @FXML
    private void limpar() {
        codigoEmEdicao = null;

        tabelaProdutos.getSelectionModel().clearSelection();

        campoCodigo.clear();
        campoDescricao.clear();
        campoPreco.clear();
        campoUnidade.clear();

        campoCodigo.requestFocus();
    }

    private void preencherFormulario(Produto produto) {
        codigoEmEdicao = produto.getCodigo();

        campoCodigo.setText(
            String.valueOf(produto.getCodigo())
        );

        campoDescricao.setText(produto.getDescricao());

        campoPreco.setText(
            String.format(
                Locale.forLanguageTag("pt-BR"),
                "%.2f",
                produto.getPreco()
            )
        );

        campoUnidade.setText(produto.getUnidade());
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
