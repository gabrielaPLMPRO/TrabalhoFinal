package br.ucs.dante;

import br.ucs.dante.repository.DatabaseCheck;
import br.ucs.dante.repository.DatabaseConnection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

/** Prepara as dependências sem conhecer JavaFX ou criar conexões permanentes. */
public final class Inicializacao {
    @FunctionalInterface
    public interface Verificador {
        String verificar(DatabaseConnection database) throws SQLException;
    }

    public record Resultado(DatabaseConnection database, String mensagem) {
        public boolean conectado() { return database != null; }
    }

    private final Verificador verificador;

    public Inicializacao() {
        this(database -> new DatabaseCheck(database).verificar());
    }

    public Inicializacao(Verificador verificador) {
        this.verificador = Objects.requireNonNull(verificador);
    }

    public Resultado executar(Map<String, String> ambiente) {
        Objects.requireNonNull(ambiente);
        try {
            DatabaseConnection database = new DatabaseConnection(
                ambiente.get("DANTE_DB_URL"), ambiente.get("DANTE_DB_USER"),
                ambiente.get("DANTE_DB_PASSWORD"));
            String banco = verificador.verificar(database);
            if (banco == null || banco.isBlank()) {
                return new Resultado(null, "O banco não confirmou a conexão. Verifique a configuração e reinicie.");
            }
            return new Resultado(database, "Conexão com PostgreSQL verificada.");
        } catch (IllegalArgumentException erro) {
            return new Resultado(null, erro.getMessage() + " Consulte Configurações.");
        } catch (SQLException | IllegalStateException erro) {
            // Mensagens do driver podem conter endereços ou credenciais.
            return new Resultado(null,
                "Não foi possível conectar ao PostgreSQL. Verifique o serviço, o banco e as credenciais e reinicie.");
        }
    }
}
