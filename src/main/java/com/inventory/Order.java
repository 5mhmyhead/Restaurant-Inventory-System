package com.inventory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order 
{
    private final IntegerProperty prodID = new SimpleIntegerProperty();
    private final IntegerProperty customerID = new SimpleIntegerProperty();
    private final StringProperty prodName = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty cashier = new SimpleStringProperty();

    // constructor for orders
    public Order(int prodID, int customerID, String prodName, int quantity, double totalAmount, String date, String status, String cashier)
    {
        this.prodID.set(prodID);
        this.customerID.set(customerID);
        this.prodName.set(prodName);
        this.quantity.set(quantity);
        this.totalAmount.set(totalAmount);
        this.date.set(date);
        this.status.set(status);
        this.cashier.set(cashier);
    }

    // property methods for the table
    public IntegerProperty prodIDProperty() { return prodID; }
    public IntegerProperty customerIDProperty() { return customerID; }
    public StringProperty prodNameProperty() { return prodName; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    public StringProperty dateProperty() { return date; }
    public StringProperty statusProperty() { return status; }
    public StringProperty cashierProperty() { return cashier; }

    // getter methods
    public int getProdID() { return prodID.get(); }
    public int getCustomerID() { return customerID.get(); }
    public String getProdName() { return prodName.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getTotalAmount() { return totalAmount.get(); }
    public String getDate() { return date.get(); }
    public String getStatus() { return status.get(); }
    public String getCashier() { return cashier.get(); }

    // setter methods
    public void setProdID(int prodID) { this.prodID.set(prodID); }
    public void setCustomerID(int customerID) { this.customerID.set(customerID); }   
    public void setProdName(String prodName) { this.prodName.set(prodName); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); } 
    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }
    public void setDate(String date) { this.date.set(date); }
    public void setStatus(String status) { this.status.set(status); }
    public void setCashier(String cashier) { this.cashier.set(cashier); }
}
