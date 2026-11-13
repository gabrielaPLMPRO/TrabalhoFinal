
package br.ucs.dante.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final String url;
    private final String usuario;
    private final String senha;

    public DatabaseConnection(
        String url,
        String usuario,
        String senha
    ) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(
            url,
            usuario,
            senha
        );
    }
}
