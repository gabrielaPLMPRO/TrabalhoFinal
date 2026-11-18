package br.ucs.dante.ui;

import br.ucs.dante.Inicializacao;
import br.ucs.dante.controller.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/** Centraliza recursos e injeção de dependências para FXML, inclusive na navegação. */
public final class TelaLoader {
    private static final Set<String> MODULOS = Set.of("clientes", "produtos", "consumos", "pagamentos");
    private final Inicializacao.Resultado inicializacao;

    public TelaLoader(Inicializacao.Resultado inicializacao) {
        this.inicializacao = Objects.requireNonNull(inicializacao);
    }

    public String status() { return inicializacao.mensagem(); }

    public URL recurso(String caminho) throws IOException {
        URL url = TelaLoader.class.getResource(caminho);
        if (url == null) { throw new IOException("Recurso ausente: " + caminho); }
        return url;
    }

    public Parent carregar(String tela) throws IOException {
        if (!"principal".equals(tela)) {
            if (!MODULOS.contains(tela)) { throw new IOException("Tela desconhecida: " + tela); }
            if (!inicializacao.conectado()) { throw new IllegalStateException(status()); }
        }
        FXMLLoader loader = new FXMLLoader(recurso("/fxml/" + tela + ".fxml"));
        loader.setControllerFactory(this::criarController);
        return loader.load();
    }

    private Object criarController(Class<?> tipo) {
        var database = inicializacao.database();
        if (tipo == PrincipalController.class) { return new PrincipalController(this); }
        if (tipo == ClienteController.class) { return new ClienteController(database); }
        if (tipo == ProdutoController.class) { return new ProdutoController(database); }
        if (tipo == ConsumoController.class) { return new ConsumoController(database); }
        if (tipo == PagamentoController.class) { return new PagamentoController(database); }
        throw new IllegalArgumentException("Controlador não registrado: " + tipo.getSimpleName());
    }
}
