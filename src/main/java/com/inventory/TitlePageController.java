package com.inventory;

import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TitlePageController 
{
    private static final String documentationLink = "https://docs.google.com/document/d/1PhIbAgGxeTrwMWUfY0cXW5aCrBaAwW2MkR0ELerZvIk/edit?usp=sharing"; 

    @FXML private Label errorMessage;

    @FXML
    private void switchToLoginPage() throws IOException 
    {
        LoginPageController controller = new LoginPageController(); 
        App.setRoot("loginPage", App.WIDTH, App.HEIGHT); 
    }

    @FXML
    private void openDocumentation() throws IOException 
    { 
        // check if desktop is supported on the current platform
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) 
        {   
            try 
            {
                // create a URI object for the desired URL
                URI uri = new URI(documentationLink);
                // open the URI in the default browser
                Desktop.getDesktop().browse(uri);
            } 
            catch(URISyntaxException e) 
            {
                e.printStackTrace();
            }
        } 
        else 
        {
            errorMessage.setText("Desktop browsing is not supported on this platform.");
        }  
    }
}
