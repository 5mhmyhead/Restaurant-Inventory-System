package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;

public class FilterMenuController implements Initializable 
{
    @FXML private ToggleButton appetizerFilter;
    @FXML private ToggleButton breakfastFilter;
    @FXML private ToggleButton cancelButton;
    @FXML private ToggleButton dinnerFilter;
    @FXML private ToggleButton filtersButton;
    @FXML private ToggleButton lunchFilter;
    @FXML private ToggleButton nonVeganFilter;
    @FXML private ToggleButton vegetarianFilter;

    @FXML private CheckBox discountFilter;
    @FXML private CheckBox availabilityFilter;
    @FXML private CheckBox stockFilter;

    @FXML
    void filters(ActionEvent event) 
    {

    }

    @FXML
    void switchToMenu(ActionEvent event) throws IOException {
        App.setRoot("menuPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @Override
   public void initialize(URL location, ResourceBundle resources) 
    {
        
    }

}
