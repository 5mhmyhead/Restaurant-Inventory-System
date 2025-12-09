package com.inventory;

public class Order 
{
    private int orderId;
    private int userId;
    private int prodId;
    private int customerId;
    private double totalAmount;
    private int orderQuantity;
    private String orderStatus;
    private String orderDate;
    private String username;

    public Order(int orderId, int userId, int prodId, int customerId, double totalAmount, int orderQuantity, String orderStatus, String orderDate, String username) 
    {
        this.orderId = orderId;
        this.userId = userId;
        this.prodId = prodId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderQuantity = orderQuantity;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.username = username;
    }

    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public int getProdId() { return prodId; }
    public int getCustomerId() { return customerId; }
    public double getTotalAmount() { return totalAmount; }
    public int getOrderQuantity() { return orderQuantity; }
    public String getOrderStatus() { return orderStatus; }
    public String getOrderDate() { return orderDate; }
    public String getUsername() { return username; }
}
