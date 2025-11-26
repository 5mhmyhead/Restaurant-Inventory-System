package com.inventory;

import java.io.IOException;

import javafx.fxml.FXML;

public class CreateAccountController 
{    
    @FXML
    private void switchToLoginPage() throws IOException 
    {
        App.setRoot("loginPage");
    }
}
