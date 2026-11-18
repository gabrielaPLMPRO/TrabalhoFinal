package br.ucs.dante.ui;

import br.ucs.dante.Inicializacao;
import br.ucs.dante.repository.DatabaseConnection;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/** Exercita FXML e navegação reais; a consulta JDBC devolve listas vazias. */
class TelaLoaderTest {
    @BeforeAll
    static void iniciarJavafx() throws Exception {
        CountDownLatch pronto = new CountDownLatch(1);
        Platform.startup(pronto::countDown);
        assertTrue(pronto.await(15, TimeUnit.SECONDS));
    }

    @AfterAll
    static void encerrarJavafx() { Platform.exit(); }

    private void naInterface(Runnable tarefa) throws Exception {
        FutureTask<Void> futuro = new FutureTask<>(tarefa, null);
        Platform.runLater(futuro);
        futuro.get(15, TimeUnit.SECONDS);
    }

    private TelaLoader semBanco() {
        return new TelaLoader(new Inicializacao.Resultado(null, "Banco não configurado."));
    }

    @Test
    void deveDetectarRecursoAusenteComErroDescritivo() {
        IOException erro = assertThrows(IOException.class,
            () -> semBanco().recurso("/fxml/inexistente.fxml"));
        assertTrue(erro.getMessage().contains("Recurso ausente"));
    }

    @Test
    void deveBloquearCrudSemConexaoAntesDeCriarControladores() {
        for (String modulo : List.of("clientes", "produtos", "consumos", "pagamentos")) {
            assertThrows(IllegalStateException.class, () -> semBanco().carregar(modulo));
        }
    }

    @Test
    void deveRejeitarTelaDesconhecida() {
        assertThrows(IOException.class, () -> semBanco().carregar("../clientes"));
    }

    @Test
    void deveCarregarInicioENavegarSemBancoSemAbrirTelasInvalidas() throws Exception {
        naInterface(() -> {
            try {
                Parent raiz = semBanco().carregar("principal");
                Label status = (Label) raiz.lookup("#statusConexao");
                assertEquals("Banco não configurado.", status.getText());
                botao(raiz, "Clientes").fire();
                assertEquals("Conexão indisponível", ((Label) raiz.lookup("#tituloPagina")).getText());
                botao(raiz, "Configurações").fire();
                assertTrue(((Label) raiz.lookup("#descricaoConteudo")).getText().contains("DANTE_DB_PASSWORD"));
                botao(raiz, "Início").fire();
                assertEquals("Visão Geral", ((Label) raiz.lookup("#tituloPagina")).getText());
                assertEquals(2, ((VBox) raiz.lookup("#conteudoCentral")).getChildren().size());
            } catch (IOException erro) { throw new AssertionError(erro); }
        });
    }

    @Test
    void deveCarregarOsQuatroFxmlComDependenciasInjetadas() throws Exception {
        naInterface(() -> {
            DatabaseConnection database = new DatabaseConnection("jdbc:postgresql://localhost/falso", "teste", "") {
                @Override public Connection conectar() { return vazio(Connection.class); }
            };
            TelaLoader telas = new TelaLoader(new Inicializacao.Resultado(database, "Conectado"));
            try {
                Parent raiz = telas.carregar("principal");
                for (String modulo : List.of("Clientes", "Produtos", "Consumos", "Pagamentos")) {
                    botao(raiz, modulo).fire();
                    assertEquals(modulo, ((Label) raiz.lookup("#tituloPagina")).getText());
                    assertEquals(1, ((VBox) raiz.lookup("#conteudoCentral")).getChildren().size());
                }
                botao(raiz, "Início").fire();
                assertEquals(2, ((VBox) raiz.lookup("#conteudoCentral")).getChildren().size());
            } catch (IOException erro) { throw new AssertionError(erro); }
        });
    }

    private Button botao(Parent raiz, String texto) {
        return raiz.lookupAll(".button").stream().filter(Button.class::isInstance)
            .map(Button.class::cast).filter(b -> texto.equals(b.getText())).findFirst().orElseThrow();
    }

    private static <T> T vazio(Class<T> tipo) {
        return tipo.cast(Proxy.newProxyInstance(TelaLoaderTest.class.getClassLoader(), new Class<?>[]{tipo},
            (proxy, metodo, argumentos) -> switch (metodo.getName()) {
                case "prepareStatement" -> vazio(PreparedStatement.class);
                case "executeQuery" -> vazio(ResultSet.class);
                case "next", "isClosed" -> false;
                case "close" -> null;
                case "toString" -> "JDBC vazio para teste de interface";
                default -> throw new UnsupportedOperationException(metodo.getName());
            }));
    }
}
