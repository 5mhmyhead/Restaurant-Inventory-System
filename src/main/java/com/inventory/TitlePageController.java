package com.inventory;

import java.io.IOException;
import javafx.fxml.FXML;

public class TitlePageController 
{
    @FXML
    private void switchToSecondary() throws IOException 
    {
        App.setRoot("secondary");
    }
}
