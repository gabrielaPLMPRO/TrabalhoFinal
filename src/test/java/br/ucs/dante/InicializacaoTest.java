package br.ucs.dante;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InicializacaoTest {
    private Map<String, String> ambiente() {
        return new HashMap<>(Map.of("DANTE_DB_URL", "jdbc:postgresql://localhost/dante",
            "DANTE_DB_USER", "usuario", "DANTE_DB_PASSWORD", "segredo"));
    }

    @Test
    void deveVerificarBancoUmaUnicaVez() {
        AtomicInteger chamadas = new AtomicInteger();
        var resultado = new Inicializacao(database -> {
            chamadas.incrementAndGet();
            return "dante";
        }).executar(ambiente());
        assertTrue(resultado.conectado());
        assertNotNull(resultado.database());
        assertEquals(1, chamadas.get());
    }

    @Test
    void devePermitirInicioSemBancoEInformarConfiguracao() {
        var resultado = new Inicializacao(database -> {
            fail("Não deve acessar JDBC sem configuração.");
            return null;
        }).executar(Map.of());
        assertFalse(resultado.conectado());
        assertTrue(resultado.mensagem().contains("DANTE_DB_URL"));
    }

    @Test
    void deveRejeitarConfiguracaoParcialAntesDeVerificarBanco() {
        var ambiente = ambiente();
        ambiente.remove("DANTE_DB_PASSWORD");
        var resultado = new Inicializacao(database -> {
            fail("Configuração parcial não deve chegar ao banco.");
            return null;
        }).executar(ambiente);
        assertFalse(resultado.conectado());
        assertTrue(resultado.mensagem().contains("DANTE_DB_PASSWORD"));
    }

    @Test
    void deveTratarFalhaDeConexaoSemExporCredenciais() {
        var resultado = new Inicializacao(database -> {
            throw new SQLException("senha=segredo; usuario privado");
        }).executar(ambiente());
        assertFalse(resultado.conectado());
        assertFalse(resultado.mensagem().contains("segredo"));
        assertFalse(resultado.mensagem().contains("privado"));
    }

    @Test
    void deveRejeitarRespostaVaziaDoBanco() {
        var resultado = new Inicializacao(database -> " ").executar(ambiente());
        assertFalse(resultado.conectado());
    }

    @Test
    void deveTratarBancoSemResultado() {
        var resultado = new Inicializacao(database -> {
            throw new IllegalStateException("O banco não retornou um resultado.");
        }).executar(ambiente());
        assertFalse(resultado.conectado());
    }
}
