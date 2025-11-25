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
}