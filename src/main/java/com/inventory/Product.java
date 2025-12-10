package com.inventory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product 
{
    private final IntegerProperty prodID = new SimpleIntegerProperty();
    private final StringProperty prodName = new SimpleStringProperty();
    private final StringProperty prodCategory = new SimpleStringProperty();
    private final StringProperty prodType = new SimpleStringProperty();
    private final DoubleProperty prodPrice = new SimpleDoubleProperty();
    private final DoubleProperty prodAmountSold = new SimpleDoubleProperty();
    private final IntegerProperty prodAmountStock = new SimpleIntegerProperty();
    private final IntegerProperty prodAmountDiscount = new SimpleIntegerProperty();
    private final StringProperty prodStatus = new SimpleStringProperty();
    private final byte[] prodImage;

    // constructor with image
    public Product(int ID, String name, String category, String type, double price, int amountSold, int amountStock, int amountDiscount, String status, byte[] image)
    {
        this.prodID.set(ID);
        this.prodName.set(name);
        this.prodCategory.set(category);
        this.prodType.set(type);
        this.prodPrice.set(price);
        this.prodAmountSold.set(amountSold);
        this.prodAmountStock.set(amountStock);
        this.prodAmountDiscount.set(amountDiscount);
        this.prodStatus.set(status);
        this.prodImage = image;
    }

    // constructor without image
    public Product(int ID, String name, String category, String type, double price, int amountSold, int amountStock, int amountDiscount, String status)
    {
        this(ID, name, category, type, price, amountSold, amountStock, amountDiscount, status, null);
    }

    // property methods for the table
    public IntegerProperty prodIDProperty() { return prodID; }
    public StringProperty prodNameProperty() { return prodName; }
    public StringProperty prodCategoryProperty() { return prodCategory; }
    public StringProperty prodTypeProperty() { return prodType; }
    public DoubleProperty prodPriceProperty() { return prodPrice; }
    public DoubleProperty prodAmountSoldProperty() { return prodAmountSold; }
    public IntegerProperty prodAmountStockProperty() { return prodAmountStock; }
    public IntegerProperty prodAmountDiscountProperty() { return prodAmountDiscount; }
    public StringProperty prodStatusProperty() { return prodStatus; }

    // getter methods
    public int getProdID() { return prodID.get(); }
    public String getProdName() { return prodName.get(); }
    public String getProdCategory() { return prodCategory.get(); }
    public String getProdType() { return prodType.get(); }
    public double getProdPrice() { return prodPrice.get(); }
    public double getProdAmountSold() { return prodAmountSold.get(); }
    public int getProdAmountStock() { return prodAmountStock.get(); }
    public int getProdAmountDiscount() { return prodAmountDiscount.get(); }
    public String getProdStatus() { return prodStatus.get(); }
    public byte[] getProdImage() { return prodImage; }

    // setter methods
    public void setProdID(int ID) { this.prodID.set(ID); }
    public void setProdName(String name) { this.prodName.set(name); }
    public void setProdCategory(String category) { this.prodCategory.set(category); }
    public void setProdType(String type) { this.prodType.set(type); }
    public void setProdPrice(double price) { this.prodPrice.set(price); }
    public void setProdAmountSold(double amountSold) { this.prodAmountSold.set(amountSold); }
    public void setProdAmountStock(int amountStock) { this.prodAmountStock.set(amountStock); }
    public void setProdAmountDiscount(int amountDiscount) { this.prodAmountDiscount.set(amountDiscount); }
    public void setProdStatus(String status) { this.prodStatus.set(status); }
}
