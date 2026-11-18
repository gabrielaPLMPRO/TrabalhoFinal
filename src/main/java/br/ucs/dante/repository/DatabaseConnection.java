
package br.ucs.dante.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private final String url;
    private final String usuario;
    private final String senha;

    public DatabaseConnection(
        String url,
        String usuario,
        String senha
    ) {
        if (url == null || url.isBlank() || !url.trim().startsWith("jdbc:postgresql:")) {
            throw new IllegalArgumentException("Configure DANTE_DB_URL com uma URL JDBC PostgreSQL.");
        }
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("Configure DANTE_DB_USER.");
        }
        if (senha == null) {
            throw new IllegalArgumentException("Configure DANTE_DB_PASSWORD.");
        }
        this.url = url.trim();
        this.usuario = usuario.trim();
        this.senha = senha;
    }

    public Connection conectar() throws SQLException {
        Properties propriedades = new Properties();
        propriedades.setProperty("user", usuario);
        propriedades.setProperty("password", senha);
        propriedades.setProperty("connectTimeout", "5");
        propriedades.setProperty("socketTimeout", "10");
        return DriverManager.getConnection(url, propriedades);
    }
}
