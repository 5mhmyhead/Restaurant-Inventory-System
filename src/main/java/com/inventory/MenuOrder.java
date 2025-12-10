package com.inventory;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MenuOrder 
{
    private final int mealId;
    private final SimpleDoubleProperty prod_price;
    private final SimpleStringProperty prodName;
    private final SimpleIntegerProperty quantity;

    public MenuOrder(int mealId, double prod_price, String prodName, int quantity)
    {
        this.mealId = mealId;
        this.prod_price = new SimpleDoubleProperty(prod_price);
        this.prodName = new SimpleStringProperty(prodName);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getMealId () {return mealId;}
    public double getPrice () {return prod_price.get();}
    public String getProdName () {return prodName.get();}
    public int getQuantity () {return quantity.get();}
}
