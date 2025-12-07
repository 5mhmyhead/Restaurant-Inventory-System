package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class FilterOrderController {
    @FXML private TextField CashierUser;
    @FXML private TextField endDate;
    @FXML private TextField enterProd;
    @FXML private Label errMsg;
    @FXML private Button filterButton;
    @FXML private Button prevButton;
    @FXML private TextField startDate;

    // animations for the error message
    PauseTransition delay;
    FadeTransition fade;
    SequentialTransition transition;

    public void initialize(URL location, ResourceBundle resources) 
    {
        // the error message waits for 2 seconds
        delay = new PauseTransition(Duration.seconds(3));
        // then it fades out
        fade = new FadeTransition();
        fade.setFromValue(1);
        fade.setToValue(0);
        // the fade plays after the delay
        transition = new SequentialTransition(errMsg, delay, fade);
    }

    @FXML
    void SwitchToOrder(ActionEvent event)   throws IOException {
        App.setRoot("switchToFilterMenu", App.WIDTH, App.HEIGHT);
    }
    

    @FXML
    void filter(ActionEvent event) {

    }

}
