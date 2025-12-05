package com.inventory;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

// this class is simply to play an animation when opening the app the first time
public class OpeningAnimationController implements Initializable
{
    // objects in the scene for the intro animation
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane circlePane;
    @FXML private ImageView kittyImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // creates an animation where the title page slides into the screen on startup
        TranslateTransition transitionImg = new TranslateTransition();
        TranslateTransition transitionPane = new TranslateTransition();
        TranslateTransition transitionCircle = new TranslateTransition();

        transitionImg.setNode(kittyImage);
        
        transitionImg.setDelay(Duration.millis(500));
        transitionImg.setDuration(Duration.seconds(1));
        
        transitionImg.setInterpolator(Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        transitionImg.setToX(-249);

        transitionPane.setNode(mainPane);

        transitionPane.setDelay(Duration.millis(500));
        transitionPane.setDuration(Duration.seconds(1));
        
        transitionPane.setInterpolator(Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        transitionPane.setToX(-600);

        transitionCircle.setNode(circlePane);

        transitionCircle.setDelay(Duration.millis(500));
        transitionCircle.setDuration(Duration.seconds(1));
        
        transitionCircle.setInterpolator(Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        transitionCircle.setToX(-500);

        transitionImg.play();
        transitionPane.play();
        transitionCircle.play();

        transitionPane.setOnFinished(event -> {
            try 
            {
                switchToLoginPage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void switchToLoginPage() throws IOException 
    {
        App.setRoot("titlePage", App.WIDTH, App.HEIGHT);
    }
}
