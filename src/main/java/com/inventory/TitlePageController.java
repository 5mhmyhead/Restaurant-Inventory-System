package com.inventory;

import java.io.IOException;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class TitlePageController
{
    private static final String documentationLink = "https://docs.google.com/document/d/1PhIbAgGxeTrwMWUfY0cXW5aCrBaAwW2MkR0ELerZvIk/edit?usp=sharing"; 

    @FXML private Label errorMessage;
    @FXML private Button enterInventory;
    @FXML private AnchorPane parentContainer;

    // objects in the scene for the intro animation
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane circlePane;
    @FXML private ImageView kittyImage;

    @FXML
    private void switchToLoginPage() throws IOException 
    {
        // disables the button
        enterInventory.setDisable(true);
        enterInventory.setStyle("-fx-opacity: 1;");
        // manually sets the root in order to play an animation to the next scene
        Parent root = App.loadFXML("loginPage");
        Scene scene = enterInventory.getScene();

        root.translateXProperty().set(scene.getWidth());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        Timeline timeline2 = new Timeline();

        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

        KeyValue kv2 = new KeyValue(root.translateXProperty(), 0, Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        KeyFrame kf2 = new KeyFrame(Duration.seconds(1), kv2);
        
        timeline.getKeyFrames().add(kf);
        timeline2.getKeyFrames().add(kf2);

        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(parentContainer);    
        });

        timeline.play();
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
