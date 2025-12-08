package com.inventory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;


public class Product 
{
    private final IntegerProperty prodId = new SimpleIntegerProperty();
    private final StringProperty prodName = new SimpleStringProperty();
    private final StringProperty prodCategory = new SimpleStringProperty();
    private final StringProperty prodType = new SimpleStringProperty();
    private final IntegerProperty amountStock = new SimpleIntegerProperty();
    private final DoubleProperty prodPrice = new SimpleDoubleProperty();
    private final IntegerProperty amountDiscount = new SimpleIntegerProperty();
    private final StringProperty prodStatus = new SimpleStringProperty();
    private final byte[] prodImage;

    // constructor with image
    public Product(int id, String name, String category, String type, double price, int sold, int stock, int discount, String status, byte[] image)
    {
        this.prodId.set(id);
        this.prodName.set(name);
        this.prodCategory.set(category);
        this.prodType.set(type);
        this.prodPrice.set(price);
        this.amountStock.set(stock);
        this.amountDiscount.set(discount);
        this.prodStatus.set(status);
        this.prodImage = image;
    }

    // constructor without image
    public Product(int id, String name, String category, String type, double price, int sold, int stock, int discount, String status)
    {
        this(id, name, category, type, price, sold, stock, discount, status, null);
    }

    // property methods for the table
    public IntegerProperty prodIdProperty() { return prodId; }
    public StringProperty prodNameProperty() { return prodName; }
    public StringProperty prodCategoryProperty() { return prodCategory; }
    public StringProperty prodTypeProperty() { return prodType; }
    public IntegerProperty amountStockProperty() { return amountStock; }
    public DoubleProperty prodPriceProperty() { return prodPrice; }
    public IntegerProperty amountDiscountProperty() { return amountDiscount; }
    public StringProperty prodStatusProperty() { return prodStatus; }

    // getter methods
    public int getProdId() { return prodId.get(); }
    public String getProdName() { return prodName.get(); }
    public String getProdCategory() { return prodCategory.get(); }
    public String getProdType() { return prodType.get(); }
    public int getAmountStock() { return amountStock.get(); }
    public double getProdPrice() { return prodPrice.get(); }
    public int getAmountDiscount() { return amountDiscount.get(); }
    public String getProdStatus() { return prodStatus.get(); }
    public byte[] getProdImage() { return prodImage; }
}
