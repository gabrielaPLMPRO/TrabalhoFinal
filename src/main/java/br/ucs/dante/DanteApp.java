package br.ucs.dante;

import br.ucs.dante.ui.TelaLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/** Ponto de entrada: prepara dependências e compõe a janela principal. */
public final class DanteApp extends Application {
    private Inicializacao.Resultado inicializacao;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        // O launcher executa init fora da thread gráfica. JDBC tem timeouts.
        inicializacao = new Inicializacao().executar(System.getenv());
    }

    @Override
    public void start(Stage stage) {
        try {
            // Todas as telas recebem a mesma configuração validada.
            TelaLoader telas = new TelaLoader(inicializacao);
            Scene scene = new Scene(telas.carregar("principal"), 1100, 700);
            scene.getStylesheets().add(telas.recurso("/css/styles.css").toExternalForm());

            stage.setTitle("Dante Sauna & Squash 2.0");
            stage.setMinWidth(850);
            stage.setMinHeight(600);
            stage.setScene(scene);
            stage.show();
        } catch (Exception erro) {
            // Falhas de recursos são apresentadas ao usuário e não ficam só no terminal.
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Falha ao iniciar");
            alerta.setHeaderText("Não foi possível carregar a interface.");
            alerta.setContentText("Verifique os arquivos FXML e CSS da instalação. Detalhe: "
                + erro.getClass().getSimpleName());
            alerta.showAndWait();
            javafx.application.Platform.exit();
        }
    }
}
