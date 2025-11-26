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

    @FXML
    private void switchToManagerView() throws IOException 
    {
        App.setRoot("createAccount_ManagerView");
    }

    @FXML
    private void switchToWorkerView() throws IOException 
    {
        App.setRoot("createAccount_WorkerView");
    }
}
