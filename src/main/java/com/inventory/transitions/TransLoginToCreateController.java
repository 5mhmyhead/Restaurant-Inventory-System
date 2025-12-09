package com.inventory.transitions;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import com.inventory.App;

// this class is to play an animation from the login to create account
public class TransLoginToCreateController implements Initializable
{
    // objects in the scene for the animation
    @FXML private AnchorPane parentContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // creates an animation
        TranslateTransition transition = new TranslateTransition();
       
        transition.setNode(parentContainer);
        transition.setDuration(Duration.seconds(1));
        
        transition.setInterpolator(Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        transition.setToY(-576);

        transition.play();
        transition.setOnFinished(event -> {
            try { switchToCreateAccount(); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }

    @FXML
    private void switchToCreateAccount() throws IOException 
    {
        App.setRoot("createAccount_ManagerView", App.WIDTH, App.HEIGHT);
    }
}
