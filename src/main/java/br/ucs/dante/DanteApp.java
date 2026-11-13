
package br.ucs.dante;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DanteApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/principal.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 1100, 700);

        scene.getStylesheets().add(
            getClass().getResource("/css/styles.css").toExternalForm()
        );

        stage.setTitle("Dante Sauna & Squash 2.0");
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.setMinHeight(600);


String url = System.getenv("DANTE_DB_URL");
String usuario = System.getenv("DANTE_DB_USER");
String senha = System.getenv("DANTE_DB_PASSWORD");

if (url == null || usuario == null || senha == null) {
    System.err.println(
        "Configure as variáveis de conexão do banco."
    );
} else {
    try {
        var database =
            new br.ucs.dante.repository.DatabaseConnection(
                url, usuario, senha
            );

        var verificacao =
            new br.ucs.dante.repository.DatabaseCheck(database);

        String banco = verificacao.verificar();

        System.out.println(
            "Conexão realizada com o banco: " + banco
        );

    } catch (Exception erro) {
        System.err.println(
            "Falha na conexão: " + erro.getMessage()
        );
    }
}


        stage.show();
    }
}
