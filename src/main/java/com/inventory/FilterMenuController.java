package com.inventory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;

public class FilterMenuController {

    private MenuPageController actualController;
    public void setController(MenuPageController controller)
    {
        this.actualController = controller;
    }

    @FXML private ToggleButton filterBreakfast;
    @FXML private ToggleButton filterLunch;
    @FXML private ToggleButton filterDinner;
    @FXML private ToggleButton filterAppetizer;
    @FXML private ToggleButton filterNonVegetarian;
    @FXML private ToggleButton filterVegetarian;

    @FXML private Button applyChangesButton;
    @FXML private Button revertChangesButton;
    @FXML private Button cancelButton;
    
    @FXML private CheckBox filterAvailability;
    @FXML private CheckBox filterDiscount;
    
    // shows/hides items in the table based on the filter that was selected
    @FXML
    private void applyFilters()
    {
        boolean breakfast = filterBreakfast.isSelected();
        boolean lunch = filterLunch.isSelected();
        boolean dinner = filterDinner.isSelected();
        boolean appetizer = filterAppetizer.isSelected();

        boolean vegetarian = filterVegetarian.isSelected();
        boolean nonVegetarian = filterNonVegetarian.isSelected();

        boolean availability = filterAvailability.isSelected();
        boolean discount = filterDiscount.isSelected();

        actualController.filterTable(actualController.searchBar.getText(), breakfast, lunch, dinner, appetizer, vegetarian, nonVegetarian, availability, discount);
        closePopup();
    }

    // clears all filters and shows all items in the table
    @FXML
    private void clearFilters()
    {
        filterBreakfast.setSelected(false);
        filterLunch.setSelected(false);
        filterDinner.setSelected(false);
        filterAppetizer.setSelected(false);
        filterVegetarian.setSelected(false);
        filterNonVegetarian.setSelected(false);

        filterAvailability.setSelected(false);
        filterDiscount.setSelected(false);

        actualController.filterTable(actualController.searchBar.getText(), false, false, false, false, false, false, false, false);
        closePopup();
    }

    // closes the pop up
    @FXML
    private void closePopup ()
    {
        cancelButton.getScene().getWindow().hide();
    }
}