package com.inventory;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;

public class InventoryPageController 
{
    @FXML private TableView<Product> inventoryTable;
    @FXML private TableColumn<Product, Number> inventoryProductID;
    @FXML private TableColumn<Product, String> inventoryProductName;
    @FXML private TableColumn<Product, String> inventoryCategory;
    @FXML private TableColumn<Product, String> inventoryType;
    @FXML private TableColumn<Product, Number> inventoryStock;
    @FXML private TableColumn<Product, Number> inventoryPrice;
    @FXML private TableColumn<Product, Number> inventoryDiscount;
    @FXML private TableColumn<Product, String> inventoryStatus;

    @FXML TextField prodNameField;
    @FXML TextField prodStockField;
    @FXML TextField prodPriceField;

    @FXML ComboBox<String> categoryDrop;
    @FXML ComboBox<String> typeDrop;
    @FXML ComboBox<String> statusDrop;
    @FXML Button signOutButton;

    private Connection conn;

    @FXML
    public void initialize ()
    {
        categoryDrop.getItems().addAll("Breakfast", "Lunch", "Dinner", "Appetizer");
        typeDrop.getItems().addAll("Vegetarian", "Non-Vegetarian");
        statusDrop.getItems().addAll("Available", "Unavailable");
        categoryDrop.setPromptText("Select Category");
        typeDrop.setPromptText("Select Type");
        statusDrop.setPromptText("Set Status");
        
        // bind columns to Meal properties
        inventoryProductID.setCellValueFactory(cellData -> cellData.getValue().prodIdProperty());
        inventoryProductName.setCellValueFactory(cellData -> cellData.getValue().prodNameProperty());
        inventoryCategory.setCellValueFactory(cellData -> cellData.getValue().prodCategoryProperty());
        inventoryType.setCellValueFactory(cellData -> cellData.getValue().prodTypeProperty());
        inventoryStock.setCellValueFactory(cellData -> cellData.getValue().amountStockProperty());
        inventoryPrice.setCellValueFactory(cellData -> cellData.getValue().prodPriceProperty());
        inventoryDiscount.setCellValueFactory(cellData -> cellData.getValue().amountDiscountProperty());
        inventoryStatus.setCellValueFactory(cellData -> cellData.getValue().prodStatusProperty());
    }

    // allow main app to inject DB connection
    public void setConnection(Connection conn) 
    {
        this.conn = conn;
    }

    // load products from database
    public void loadItems() 
    {
        ObservableList<Product> data = FXCollections.observableArrayList();
        String sql = "SELECT meal_id, meal_name, category, type, price, amount_sold, amount_stock, amount_discount, status FROM Meal";

        try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) 
            {
                data.add(new Product(
                    rs.getInt("meal_id"),
                    rs.getString("meal_name"),
                    rs.getString("category"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getInt("amount_sold"),
                    rs.getInt("amount_stock"),
                    rs.getInt("amount_discount"),
                    rs.getString("status")
                ));
            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        inventoryTable.setItems(data);
    }

    @FXML
    private void addItem() 
    {
        // validation check
        if (prodNameField.getText().trim().isEmpty() 
            || prodPriceField.getText().trim().isEmpty() 
            || prodStockField.getText().trim().isEmpty() 
            || categoryDrop.getValue().trim().isEmpty() 
            || typeDrop.getValue().trim().isEmpty() 
            || statusDrop.getValue().trim().isEmpty()) 
        {
            System.out.println("Please fill in all fields before adding a product.");
            return; // stop execution
        }
        
        String sql = "INSERT INTO MEAL (meal_name, category, type, price, amount_sold, amount_stock, amount_discount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) 
        {
            String name = prodNameField.getText();
            double price = Double.parseDouble(prodPriceField.getText());
            int stock = Integer.parseInt(prodStockField.getText());
            String type = typeDrop.getValue();
            String status = statusDrop.getValue();
            String category = categoryDrop.getValue();

            // set parameters
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, type);
            ps.setDouble(4, price);
            ps.setInt(5, 0);
            ps.setInt(6, stock);
            ps.setInt(7, 0);
            ps.setString(8, status);

            ps.executeUpdate();

            // get auto-generated prod_id
            try (ResultSet rs = ps.getGeneratedKeys()) 
            {
                if (rs.next()) 
                {
                    int newId = rs.getInt("meal_id");
                    System.out.println("Inserted product with ID: " + newId);
                }
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        // refresh table
        loadItems();
    }

    @FXML
    private void deleteItem ()
    {

    }

    @FXML
    private void switchToMenu() throws IOException 
    {
        App.setRoot("menuPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToOrders() throws IOException 
    {
        App.setRoot("ordersPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToAnalytics() throws IOException 
    {
        App.setRoot("analyticsPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("titlePage", App.WIDTH, App.HEIGHT);
    }
}
