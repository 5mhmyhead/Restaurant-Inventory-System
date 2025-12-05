package com.inventory;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product 
{
    private final SimpleIntegerProperty prodId;
    private final SimpleStringProperty prodName;
    private final SimpleStringProperty prodCategory;
    private final SimpleStringProperty prodType;
    private final SimpleDoubleProperty prodPrice;
    private final SimpleIntegerProperty amountSold;
    private final SimpleIntegerProperty amountStock;
    private final SimpleIntegerProperty amountDiscount;
    private final SimpleStringProperty prodStatus;

    // constructor
    public Product(int prodId, String prodName, String prodCategory, String prodType, double prodPrice, int amountSold, int amountStock, int amountDiscount, String prodStatus) 
    {
        this.prodId = new SimpleIntegerProperty(prodId);
        this.prodName = new SimpleStringProperty(prodName);
        this.prodCategory = new SimpleStringProperty(prodCategory);
        this.prodType = new SimpleStringProperty(prodType);
        this.prodPrice = new SimpleDoubleProperty(prodPrice);
        this.amountSold = new SimpleIntegerProperty(amountSold);
        this.amountStock = new SimpleIntegerProperty(amountStock);
        this.amountDiscount = new SimpleIntegerProperty(amountDiscount);
        this.prodStatus = new SimpleStringProperty(prodStatus);
    }

    // getters
    public int getProdId() { return prodId.get(); }
    public String getProdName() { return prodName.get(); }
    public String getProdCategory() { return prodCategory.get(); }
    public String getProdType() { return prodType.get(); }
    public double getProdPrice() { return prodPrice.get(); }
    public int getAmountSold() { return amountSold.get(); }
    public int getAmountStock() { return amountStock.get(); }
    public int getAmountDiscount() { return amountDiscount.get(); }
    public String getProdStatus() { return prodStatus.get(); }

    // property getters (needed for TableView binding)
    public SimpleIntegerProperty prodIdProperty() { return prodId; }
    public SimpleStringProperty prodNameProperty() { return prodName; }
    public SimpleStringProperty prodCategoryProperty() { return prodCategory; }
    public SimpleStringProperty prodTypeProperty() { return prodType; }
    public SimpleDoubleProperty prodPriceProperty() { return prodPrice; }
    public SimpleIntegerProperty amountSoldProperty() { return amountSold; }
    public SimpleIntegerProperty amountStockProperty() { return amountStock; }
    public SimpleIntegerProperty amountDiscountProperty() { return amountDiscount; }
    public SimpleStringProperty prodStatusProperty() { return prodStatus; }
}
