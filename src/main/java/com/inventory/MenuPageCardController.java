package com.inventory;

import java.io.ByteArrayInputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class MenuPageCardController 
{
    @FXML private ImageView prodImage;
    @FXML private Label prodName;
    @FXML private Button addButton;

    public void setData (Product product)
    {
        prodName.setText(product.getProdName());

        byte[] imageBytes = product.getProdImage();
        if (imageBytes != null)
        {
            Image image = new Image (new ByteArrayInputStream(imageBytes));
            prodImage.setImage(image);
        }
    }
}
