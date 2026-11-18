package br.ucs.dante.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {
    @Test
    void deveRejeitarUrlAusenteAntesDeAbrirUmaTela() {
        assertThrows(IllegalArgumentException.class,
            () -> new DatabaseConnection(null, "usuario", "senha"));
    }

    @Test
    void deveRejeitarUrlVazia() {
        assertThrows(IllegalArgumentException.class,
            () -> new DatabaseConnection("  ", "usuario", "senha"));
    }

    @Test
    void deveRejeitarOutroTipoDeBanco() {
        assertThrows(IllegalArgumentException.class,
            () -> new DatabaseConnection("jdbc:mysql://localhost/dante", "usuario", "senha"));
    }

    @Test
    void deveRejeitarUsuarioEmBranco() {
        assertThrows(IllegalArgumentException.class,
            () -> new DatabaseConnection("jdbc:postgresql://localhost/dante", " ", "senha"));
    }

    @Test
    void deveRejeitarSenhaNaoConfigurada() {
        assertThrows(IllegalArgumentException.class,
            () -> new DatabaseConnection("jdbc:postgresql://localhost/dante", "usuario", null));
    }

    @Test
    void devePreservarSenhaVaziaQuandoExplicitamenteConfigurada() {
        assertDoesNotThrow(
            () -> new DatabaseConnection("jdbc:postgresql://localhost/dante", "usuario", ""));
    }
}
