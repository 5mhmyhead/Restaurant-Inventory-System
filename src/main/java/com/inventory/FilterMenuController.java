package com.inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class FilterMenuController {

    @FXML
    private ToggleButton appetizerFilter;
    @FXML
    private CheckBox availabilityFilter;
    @FXML
    private ToggleButton breakfastFilter;
    @FXML
    private Button cancelButton;
    @FXML
    private ToggleButton dinnerFilter;
    @FXML
    private CheckBox discountFilter;
    @FXML
    private Label errMessage;
    @FXML
    private Button filterMenuButton;
    @FXML
    private ToggleButton lunchFilter;
    @FXML
    private ToggleButton nonVeganFilter;
    @FXML
    private ToggleButton vegetarianFilter;

    @FXML
    void closesPopup(ActionEvent event) {

        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    void filters(ActionEvent event) {

    }

}




    

    


