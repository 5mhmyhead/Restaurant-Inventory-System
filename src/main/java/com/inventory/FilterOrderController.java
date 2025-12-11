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

public class FilterOrderController implements Initializable
{
    private OrdersPageController actualController;
    public void setController(OrdersPageController controller)
    {
        this.actualController = controller;
    }

    @FXML private TextField CashierUser;
    @FXML private TextField endDate;
    @FXML private TextField enterProd;
    @FXML private TextField startDate;
    
    @FXML private Label errMsg;
    
    @FXML private Button filterButton;
    @FXML private Button revertButton;
    @FXML private Button prevButton;
    @FXML private Button clearButton;

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
    void applyFilters(ActionEvent event) throws IOException 
    {
        String cashier = CashierUser.getText().trim();
        String productID = enterProd.getText().trim();
        String startingDate = startDate.getText().trim();
        String endingDate = endDate.getText().trim();

        // print error message if all fields are empty
        if (cashier.isEmpty() && productID.isEmpty() && startingDate.isEmpty() && endingDate.isEmpty()) 
        {
            errMsg.setText("Please enter at least one filter.");
            playAnimation();
            return;
        }

        actualController.filterTable(actualController.searchBar.getText(), cashier, productID, startingDate, endingDate);
        closePopup();
    }
  
    // clears all filters and shows all items in the table
    @FXML
    private void clearFilters()
    {
        CashierUser.clear();
        enterProd.clear();
        startDate.clear();
        endDate.clear();

        actualController.filterTable(actualController.searchBar.getText(), null, null, null, null);
        closePopup();
    }

    @FXML
    private void clearFields()
    {
        CashierUser.clear();
        enterProd.clear();
        startDate.clear();
        endDate.clear();
    }

    //error message animation helper
    private void playAnimation() 
    {
        transition.jumpTo(Duration.ZERO);
        transition.stop();
        transition.play();
    }

    // closes the pop up
    @FXML
    private void closePopup ()
    {
        prevButton.getScene().getWindow().hide();
    }

    @FXML
    void SwitchToOrder(ActionEvent event)   throws IOException 
    {
        App.setRoot("ordersPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }
}