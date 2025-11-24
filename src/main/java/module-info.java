module com.inventory {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.inventory to javafx.fxml;
    exports com.inventory;
}
