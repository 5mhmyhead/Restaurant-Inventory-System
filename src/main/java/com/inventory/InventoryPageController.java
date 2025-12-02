package com.inventory;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class InventoryPageController {
    @FXML ComboBox<String> typeDrop;
    @FXML ComboBox<String> statusDrop;

    @FXML
    public void initialize ()
    {
        typeDrop.getItems().addAll("Breakfast", "Lunch", "Dinner");
        statusDrop.getItems().addAll("Available", "Unavailable");

        typeDrop.setValue("Breakfast");
        statusDrop.setValue("Available");
    }
}
