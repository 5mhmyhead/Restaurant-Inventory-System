package com.inventory;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class OrdersPageController {
    @FXML private Button analyticsButton;
    @FXML private Button filterMenuButton;
    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button signOutButton;
    @FXML private Label welcomeMessage;

    @FXML
    void signOut(ActionEvent event) throws IOException {
        App.setRoot("titlePage", App.WIDTH, App.HEIGHT);
    }

    @FXML
    void switchToAnalytics(ActionEvent event) throws IOException {
        App.setRoot("analyticsPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    void switchToFilterOrder(ActionEvent event) throws IOException {
        App.setRoot("filterOrders", App.WIDTH, App.HEIGHT);
    }

    @FXML
    void switchToInventory(ActionEvent event) throws IOException {
        App.setRoot("inventoryPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    void switchToMenu(ActionEvent event) throws IOException {
        App.setRoot("menuPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

}
