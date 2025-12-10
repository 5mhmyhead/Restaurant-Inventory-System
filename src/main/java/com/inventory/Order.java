package com.inventory;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order 
{
    private final IntegerProperty orderID = new SimpleIntegerProperty(); 
    private final IntegerProperty orderUserID = new SimpleIntegerProperty(); 
    private final IntegerProperty orderProdID = new SimpleIntegerProperty(); 
    private final IntegerProperty orderCustomerID = new SimpleIntegerProperty(); 
    private final DoubleProperty orderTotalAmount = new SimpleDoubleProperty(); 
    private final IntegerProperty orderQuantity = new SimpleIntegerProperty(); 
    private final StringProperty orderStatus = new SimpleStringProperty(); 
    private final StringProperty orderDate = new SimpleStringProperty(); 
    private final StringProperty orderCashier = new SimpleStringProperty();

    public Order(int ID, int userID, int prodID, int customerID, double totalAmount, int quantity, String status, String date, String cashier) 
    {
        this.orderID.set(ID);
        this.orderUserID.set(userID);
        this.orderProdID.set(prodID);
        this.orderCustomerID.set(customerID);
        this.orderTotalAmount.set(totalAmount);
        this.orderQuantity.set(quantity);
        this.orderStatus.set(status);
        this.orderDate.set(date);
        this.orderCashier.set(cashier);
    }

    // property methods for the table
    public IntegerProperty orderIDProperty() { return orderID; }
    public IntegerProperty orderUserIDProperty() { return orderUserID; }
    public IntegerProperty orderProdIDProperty() { return orderProdID; }
    public IntegerProperty orderCustomerIDProperty() { return orderCustomerID; }
    public DoubleProperty orderTotalAmountProperty() { return orderTotalAmount; }
    public IntegerProperty orderQuantityProperty() { return orderQuantity; }
    public StringProperty orderStatusProperty() { return orderStatus; }
    public StringProperty orderDateProperty() { return orderDate; }
    public StringProperty orderCashierProperty() { return orderCashier; }

    // getter methods
    public int getOrderID() { return orderID.get(); }
    public int getOrderUserID() { return orderUserID.get(); }
    public int getOrderProdID() { return orderProdID.get(); }
    public int getOrderCustomerID() { return orderCustomerID.get(); }
    public double getOrderTotalAmount() { return orderTotalAmount.get(); }
    public int getOrderQuantity() { return orderQuantity.get(); }
    public String getOrderStatus() { return orderStatus.get(); }
    public String getOrderDate() { return orderDate.get(); }
    public String getOrderCashier() { return orderCashier.get(); }

    // setter methods
    public void setOrderID(int ID) { this.orderID.set(ID); }
    public void setOrderUserID(int userID) { this.orderUserID.set(userID); }
    public void setOrderProdID(int prodID) { this.orderProdID.set(prodID); }
    public void setOrderCustomerID(int customerID) { this.orderCustomerID.set(customerID); }
    public void setOrderTotalAmount(double totalAmount) { this.orderTotalAmount.set(totalAmount); }
    public void setOrderQuantity(int quantity) { this.orderQuantity.set(quantity); }
    public void setOrderStatus(String status) { this.orderStatus.set(status); }
    public void setOrderDate(String date) { this.orderDate.set(date); }
    public void setOrderCashier(String cashier) { this.orderCashier.set(cashier); }
}
