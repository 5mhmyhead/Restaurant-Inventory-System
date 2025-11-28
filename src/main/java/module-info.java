module com.inventory {
    requires java.sql;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.inventory to javafx.fxml;
    exports com.inventory;
}
