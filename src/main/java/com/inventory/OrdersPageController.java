package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class OrdersPageController implements Initializable
{
    @FXML private AnchorPane parentContainer;
    @FXML private AnchorPane selectedBar;
    @FXML private Label welcomeMessage;

    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button analyticsButton;

    @FXML private ImageView fridgeWhite;
    @FXML private ImageView cutleryWhite;
    @FXML private ImageView chickenPink;
    @FXML private ImageView hatWhite;

    @FXML Button filterMenuButton;
    @FXML Button signOutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // below is the code for animating the sidebar slide
        Color fromColor = new Color(0.906, 0.427, 0.541, 1.0);
        Color toColor = new Color(0.973, 0.914, 0.898, 1.0);

        Timeline timelineOrders = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(ordersButton.textFillProperty(), fromColor)),
            new KeyFrame(Duration.millis(100), new KeyValue(ordersButton.textFillProperty(), toColor))
        );

        FadeTransition fadeChicken = new FadeTransition(Duration.millis(100), chickenPink);
        fadeChicken.setFromValue(1);
        fadeChicken.setToValue(0);

        inventoryButton.setOnAction(e -> {
            // disable the button
            inventoryButton.setDisable(true);
            inventoryButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineInventory = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(inventoryButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(350), new KeyValue(inventoryButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeFridge = new FadeTransition(Duration.millis(350), fridgeWhite);
            fadeFridge.setFromValue(1);
            fadeFridge.setToValue(0);
            // play the animations and fade
            timelineOrders.play();
            timelineInventory.play();

            fadeFridge.play();
            fadeChicken.play();
            // switch to inventory
            try { switchToInventory(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

        menuButton.setOnAction(e -> {
            // disable the button
            menuButton.setDisable(true);
            menuButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineMenu = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(menuButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(300), new KeyValue(menuButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeCutlery = new FadeTransition(Duration.millis(300), cutleryWhite);
            fadeCutlery.setFromValue(1);
            fadeCutlery.setToValue(0);
            // play the animations and fade
            timelineMenu.play();
            timelineOrders.play();

            fadeCutlery.play();
            fadeChicken.play();
            // switch to menu
            try { switchToMenu(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

        analyticsButton.setOnAction(e -> {
            // disable the button
            analyticsButton.setDisable(true);
            analyticsButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineAnalytics = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(analyticsButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(300), new KeyValue(analyticsButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeHat = new FadeTransition(Duration.millis(300), hatWhite);
            fadeHat.setFromValue(1);
            fadeHat.setToValue(0);
            // play the animations and fade
            timelineOrders.play();
            timelineAnalytics.play();

            fadeChicken.play();
            fadeHat.play();
            // switch to analytics
            try { switchToAnalytics(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });
    }

    // animation helper for moving the sidebar up and down
    private void playAnimation(String fxml, int duration, int setToY)
    {
        TranslateTransition transition = new TranslateTransition();
       
        transition.setNode(selectedBar);
        transition.setDuration(Duration.millis(duration));
        
        transition.setInterpolator(Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        transition.setToY(setToY);

        transition.play();
        transition.setOnFinished(event -> {
            try { App.setRoot(fxml, App.MAIN_WIDTH, App.MAIN_HEIGHT); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }

    @FXML
    void switchToInventory() throws IOException 
    {
        playAnimation("inventoryPage", 350, -200);
    }

    @FXML
    void switchToMenu() throws IOException 
    {
        playAnimation("menuPage", 300, -100);
    }

    @FXML
    void switchToAnalytics() throws IOException 
    {
        playAnimation("analyticsPage", 300, 100);
    }

    @FXML
    void switchToFilterOrder() throws IOException 
    {
        App.setRoot("filterOrders", App.WIDTH, App.HEIGHT);
    }

    @FXML
    void signOut() throws IOException 
    {
        App.setRoot("openingAnimation", App.WIDTH, App.HEIGHT);
    }
}
