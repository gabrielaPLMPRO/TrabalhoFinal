
package br.ucs.dante.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;

public class PrincipalController {

    @FXML
    private Label tituloPagina;

    @FXML
    private Label tituloConteudo;

    @FXML
    private Label descricaoConteudo;

    @FXML
    private VBox conteudoCentral;

    private List<Node> componentesIniciais;


@FXML
private void initialize() {
    componentesIniciais = new ArrayList<>(
        conteudoCentral.getChildren()
    );
}

private void restaurarInicio() {
    conteudoCentral.getChildren().setAll(componentesIniciais);
}


    private void mostrarModulo(String nome) {
        restaurarInicio();

        tituloPagina.setText(nome);
        tituloConteudo.setText("Módulo: " + nome);
        descricaoConteudo.setText(
            "A interface deste módulo será implementada nas próximas etapas."
        );
    }

    @FXML
    private void abrirInicio() {
        restaurarInicio();

        tituloPagina.setText("Visão Geral");
        tituloConteudo.setText("Bem-vindo ao Dante Sauna & Squash!");
        descricaoConteudo.setText(
            "Selecione um módulo no menu lateral para começar."
        );
    }

    @FXML
    private void abrirClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/clientes.fxml")
            );

            Parent formulario = loader.load();

            tituloPagina.setText("Clientes");
            conteudoCentral.getChildren().setAll(formulario);

        } catch (IOException erro) {
            Alert alerta = new Alert(
                Alert.AlertType.ERROR,
                "Não foi possível abrir o cadastro: " +
                erro.getMessage(),
                ButtonType.OK
            );

            alerta.showAndWait();
        }
    }

  
@FXML
private void abrirProdutos() {
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/produtos.fxml")
        );

        Parent formulario = loader.load();

        tituloPagina.setText("Produtos");
        conteudoCentral.getChildren().setAll(formulario);

    } catch (IOException erro) {
        Alert alerta = new Alert(
            Alert.AlertType.ERROR,
            "Não foi possível abrir o cadastro de produtos: "
                + erro.getMessage(),
            ButtonType.OK
        );

        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}



@FXML
private void abrirConsumos() {
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/consumos.fxml")
        );

        Parent formulario = loader.load();

        tituloPagina.setText("Consumos");
        conteudoCentral.getChildren().setAll(formulario);

    } catch (IOException erro) {
        Alert alerta = new Alert(
            Alert.AlertType.ERROR,
            "Não foi possível abrir o módulo de Consumos: "
                + erro.getMessage(),
            ButtonType.OK
        );

        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}



@FXML
private void abrirPagamentos() {
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/pagamentos.fxml")
        );

        Parent formulario = loader.load();

        tituloPagina.setText("Pagamentos");
        conteudoCentral.getChildren().setAll(formulario);

    } catch (IOException erro) {
        Alert alerta = new Alert(
            Alert.AlertType.ERROR,
            "Não foi possível abrir o módulo de Pagamentos: "
                + erro.getMessage(),
            ButtonType.OK
        );

        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}


    @FXML
    private void abrirReservas() {
        mostrarModulo("Reservas de Squash");
    }

    @FXML
    private void abrirSauna() {
        mostrarModulo("Sauna");
    }

    @FXML
    private void abrirMensalidades() {
        mostrarModulo("Mensalidades");
    }

    @FXML
    private void abrirRelatorios() {
        mostrarModulo("Relatórios");
    }

    @FXML
    private void abrirConfiguracoes() {
        mostrarModulo("Configurações");
    }
}
