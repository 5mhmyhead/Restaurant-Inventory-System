package com.inventory;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;

public class MenuPageCardController 
{
    @FXML private Rectangle rectangleBorder;
    @FXML private Rectangle rectangleImageFrame;
    @FXML private ImageView prodImage;
    @FXML private Label prodName;
    @FXML private Label quantityLabel;
    @FXML private Label closeSpinner;
    @FXML private Line buttonSeparator;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button addButton;

    private Product product;

    private int quantity = 0;
    private int maxStock = 0;

    private TableView<MenuOrder> menuOrdersTable;

    public void setData (Product product)
    {
        this.product = product;

        prodName.setText(product.getProdName());

        byte[] imageBytes = product.getProdImage();
        if (imageBytes != null)
        {
            Image image = new Image (new ByteArrayInputStream(imageBytes));
            prodImage.setImage(image);
        }

        maxStock = product.getAmountStock();
        quantityLabel.setText(String.valueOf(quantity));
        upButton.setOnAction(e -> incrementQuantity());
        downButton.setOnAction(e -> decrementQuantity());
        changeStyle();
    }

    private void changeStyle ()
    {
        //if available
        if (maxStock > 0)
        {
            prodName.getStyleClass().removeAll("menu-card-label-unavailable");
            prodName.getStyleClass().add("menu-card-label-available");
            quantityLabel.getStyleClass().removeAll("menu-card-counter-unavailable");
            quantityLabel.getStyleClass().add("menu-card-counter-available");
            closeSpinner.getStyleClass().removeAll("menu-card-updown-container-unavailable");
            closeSpinner.getStyleClass().add("menu-card-updown-container-available");
            addButton.getStyleClass().removeAll("menu-card-button-unavailable");
            addButton.getStyleClass().add("menu-card-button-available");
            addButton.setDisable(false);
            rectangleBorder.getStyleClass().removeAll("menu-card-box-unavailable");
            rectangleBorder.getStyleClass().add("menu-card-box-available");
            rectangleImageFrame.getStyleClass().removeAll("menu-card-border-unavailable");
            rectangleImageFrame.getStyleClass().add("menu-card-border-available");
            buttonSeparator.setStroke(javafx.scene.paint.Color.web("#EF8FA4"));
        }
        //if out of stock
        else
        {
            prodName.getStyleClass().removeAll("menu-card-label-available");
            prodName.getStyleClass().add("menu-card-label-unavailable");
            quantityLabel.getStyleClass().removeAll("menu-card-counter-available");
            quantityLabel.getStyleClass().add("menu-card-counter-unavailable");
            closeSpinner.getStyleClass().removeAll("menu-card-updown-container-available");
            closeSpinner.getStyleClass().add("menu-card-updown-container-unavailable");
            addButton.getStyleClass().removeAll("menu-card-button-available");
            addButton.getStyleClass().add("menu-card-button-unavailable");
            addButton.setDisable(true);
            rectangleBorder.getStyleClass().removeAll("menu-card-box-available");
            rectangleBorder.getStyleClass().add("menu-card-box-unavailable");
            rectangleImageFrame.getStyleClass().removeAll("menu-card-border-available");
            rectangleImageFrame.getStyleClass().add("menu-card-border-unavailable");
            buttonSeparator.setStroke(javafx.scene.paint.Color.web("#FCB7A4"));
        }
    }

    private void incrementQuantity()
    {
        if (quantity < maxStock)
        {
            quantity++;
            quantityLabel.setText(String.valueOf(quantity));
        }
    }

    private void decrementQuantity()
    {
        if (quantity > 0)
        {
            quantity--;
            quantityLabel.setText(String.valueOf(quantity));
        }
    }

    @FXML
    private void addOrders()
    {
        if (quantity > 0)
        {
            int newStock = product.getAmountStock() - quantity;
            product.setAmountStock(newStock);
            maxStock = newStock;

            if (menuOrdersTable != null)
            {
                MenuOrder order = new MenuOrder(product.getProdId(), product.getProdPrice(), product.getProdName(), quantity);
                menuOrdersTable.getItems().add(order);
            }

            quantity = 0;
            quantityLabel.setText(String.valueOf(quantity));
            changeStyle();
        }
    }

    public void setOrdersTable(TableView<MenuOrder> menuOrdersTable)
    {
        this.menuOrdersTable = menuOrdersTable;
    }
}
