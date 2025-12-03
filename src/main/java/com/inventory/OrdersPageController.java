package com.inventory;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OrdersPageController {
    @FXML Button signOutButton;

    @FXML
    private void switchToInventory() throws IOException 
    {
        App.setRoot("inventoryPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToMenu() throws IOException 
    {
        App.setRoot("menuPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
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
