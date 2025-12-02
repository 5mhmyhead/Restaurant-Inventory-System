package com.inventory;

import java.io.IOException;

import javafx.fxml.FXML;

public class RecoverAccountController 
{
    @FXML
    private void switchToLoginPage() throws IOException 
    { 
        App.setRoot("loginPage", App.WIDTH, App.HEIGHT); 
    }    
}
