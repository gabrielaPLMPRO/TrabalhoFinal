
package br.ucs.dante.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseCheck {

    private final DatabaseConnection database;

    public DatabaseCheck(DatabaseConnection database) {
        this.database = database;
    }

    public String verificar() throws Exception {

        String sql = "SELECT current_database()";

        try (
            Connection conexao = database.conectar();
            Statement statement = conexao.createStatement();
            ResultSet resultado = statement.executeQuery(sql)
        ) {
            if (resultado.next()) {
                return resultado.getString(1);
            }

            throw new IllegalStateException(
                "O banco não retornou um resultado."
            );
        }
    }
}
