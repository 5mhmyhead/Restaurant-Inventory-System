module com.inventory {
    requires transitive java.sql;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.inventory to javafx.fxml;
    exports com.inventory;
}
