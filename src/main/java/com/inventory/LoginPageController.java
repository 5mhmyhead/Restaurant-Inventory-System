package com.inventory;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginPageController 
{
    @FXML
    private void switchToTitlePage() throws IOException 
    {
        App.setRoot("titlePage");
    }

    @FXML
    private void switchToCreateAccount() throws IOException 
    {
        App.setRoot("createAccount");
    }

        @FXML
    private void switchToRecoverAccount() throws IOException 
    {
        App.setRoot("recoverAccount");
    }
}