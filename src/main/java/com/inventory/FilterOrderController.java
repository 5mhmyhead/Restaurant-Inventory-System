package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class FilterOrderController implements Initializable{
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

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // the error message waits for 2 seconds
        delay = new PauseTransition(Duration.seconds(3));
        // then it fades out
        fade = new FadeTransition(Duration.seconds(2), errMsg);
        fade.setFromValue(1);
        fade.setToValue(0);
        // the fade plays after the delay
        transition = new SequentialTransition(errMsg, delay, fade);
    }

    @FXML
    void SwitchToOrder(ActionEvent event)   throws IOException {
        App.setRoot("ordersPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }
    

    @FXML
    void filter(ActionEvent event)  throws IOException {
        String cashier = CashierUser.getText().trim();
        String productID = enterProd.getText().trim();
        String start = startDate.getText().trim();
        String end = endDate.getText().trim();

        //function if any  field is empty
        if (cashier.isEmpty() && productID.isEmpty() && start.isEmpty() && end.isEmpty()){
        
        errMsg.setText("Please enter atleast one filter");
        playAnimation();
        return;
        }
        //missing both dates
        if(start.isEmpty() && end.isEmpty()){

            errMsg.setText("Date is empty");
            playAnimation();
            return;
        }
        
        if(!start.isEmpty() && end.isEmpty() || start.isEmpty() && !end.isEmpty()){

            errMsg.setText("Please enter both start and end dates");
            playAnimation();
            return;
        }
        //check if date exists
        
    }

    
 // error message animation helper
    private void playAnimation() 
    {
        transition.jumpTo(Duration.ZERO);
        transition.stop();
        transition.play();
    }
    
    
        }