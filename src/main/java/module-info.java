
module br.ucs.dante {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    exports br.ucs.dante;

    opens br.ucs.dante.controller to javafx.fxml;
}
