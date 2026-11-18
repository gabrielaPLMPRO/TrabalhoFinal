package br.ucs.dante.controller;

import br.ucs.dante.ui.TelaLoader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class PrincipalController {
    @FXML private Label tituloPagina;
    @FXML private Label tituloConteudo;
    @FXML private Label descricaoConteudo;
    @FXML private Label statusConexao;
    @FXML private VBox conteudoCentral;

    private final TelaLoader telas;
    private List<Node> componentesIniciais;

    public PrincipalController(TelaLoader telas) {
        this.telas = Objects.requireNonNull(telas);
    }

    @FXML
    private void initialize() {
        componentesIniciais = List.copyOf(conteudoCentral.getChildren());
        statusConexao.setText(telas.status());
    }

    private void mensagem(String titulo, String descricao) {
        conteudoCentral.getChildren().setAll(componentesIniciais);
        tituloPagina.setText(titulo);
        tituloConteudo.setText(titulo);
        descricaoConteudo.setText(descricao);
    }

    private void abrirModulo(String tela, String titulo) {
        try {
            Parent formulario = telas.carregar(tela);
            conteudoCentral.getChildren().setAll(formulario);
            tituloPagina.setText(titulo);
        } catch (IllegalStateException erro) {
            mensagem("Conexão indisponível", erro.getMessage());
        } catch (IOException erro) {
            mensagem("Falha ao abrir " + titulo,
                "Não foi possível carregar a tela. Verifique os recursos da instalação.");
        }
    }

    private void pendente(String modulo) {
        mensagem(modulo, "Este módulo permanece pendente na reconstrução JavaFX, conforme a seção 7.2 do relatório.");
    }

    @FXML private void abrirInicio() {
        mensagem("Visão Geral", "Selecione um módulo no menu lateral para começar.");
        tituloConteudo.setText("Bem-vindo ao Dante Sauna & Squash!");
    }
    @FXML private void abrirClientes() { abrirModulo("clientes", "Clientes"); }
    @FXML private void abrirProdutos() { abrirModulo("produtos", "Produtos"); }
    @FXML private void abrirConsumos() { abrirModulo("consumos", "Consumos"); }
    @FXML private void abrirPagamentos() { abrirModulo("pagamentos", "Pagamentos"); }
    @FXML private void abrirReservas() { pendente("Reservas de Squash"); }
    @FXML private void abrirSauna() { pendente("Sauna"); }
    @FXML private void abrirMensalidades() { pendente("Mensalidades"); }
    @FXML private void abrirRelatorios() { pendente("Relatórios"); }
    @FXML private void abrirConfiguracoes() {
        mensagem("Configurações", "Configure DANTE_DB_URL (jdbc:postgresql://servidor:5432/banco), "
            + "DANTE_DB_USER e DANTE_DB_PASSWORD no ambiente antes de iniciar. "
            + "O banco e as tabelas devem existir. Após alterar a configuração, reinicie o programa. "
            + "Situação atual: " + telas.status());
    }
}
