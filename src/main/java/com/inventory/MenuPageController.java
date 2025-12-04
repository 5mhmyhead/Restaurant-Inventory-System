package com.inventory;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuPageController {
    @FXML Button signOutButton;

    @FXML
    private void switchToInventory() throws IOException 
    {
        App.setRoot("inventoryPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToOrders() throws IOException 
    {
        App.setRoot("ordersPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToAnalytics() throws IOException 
    {
        App.setRoot("analyticsPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("titlePage", App.WIDTH, App.HEIGHT);
    }
}
