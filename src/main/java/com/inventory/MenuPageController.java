package com.inventory;

import java.io.IOException;
import javafx.fxml.FXML;

public class MenuPageController {
    @FXML
    private void signOut() throws IOException 
    { 
        App.setRoot("loginPage", App.WIDTH, App.HEIGHT); 
    }
}
