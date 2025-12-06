package com.inventory;

import java.io.IOException;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class InventoryPageController implements Initializable
{
    @FXML private AnchorPane parentContainer;
    @FXML private Label welcomeMessage;

    @FXML private TableView<Product> inventoryTable;
    @FXML private TableColumn<Product, Number> inventoryProductID;
    @FXML private TableColumn<Product, String> inventoryProductName;
    @FXML private TableColumn<Product, String> inventoryCategory;
    @FXML private TableColumn<Product, String> inventoryType;
    @FXML private TableColumn<Product, Number> inventoryStock;
    @FXML private TableColumn<Product, Number> inventoryPrice;
    @FXML private TableColumn<Product, Number> inventoryDiscount;
    @FXML private TableColumn<Product, String> inventoryStatus;

    @FXML TextField prodIDField;
    @FXML TextField prodNameField;
    @FXML TextField prodStockField;
    @FXML TextField prodPriceField;

    @FXML ComboBox<String> categoryDrop;
    @FXML ComboBox<String> typeDrop;
    @FXML ComboBox<String> statusDrop;
    @FXML Button signOutButton;

    private Connection conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
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

    // TODO: make this work
    // change the welcome message to the user
    public void setUsername(String user)
    {
        welcomeMessage.setText("Welcome, " + user + "!");
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
    private void clearFields()
    {
        // clear text fields and resets combo boxes to null while making them show prompt text again
        prodIDField.clear();
        prodNameField.clear();
        prodPriceField.clear();
        prodStockField.clear();
        categoryDrop.setValue(null);
        typeDrop.setValue(null);
        statusDrop.setValue(null);
        categoryDrop.setPromptText("Select Category");
        typeDrop.setPromptText("Select Type");
        statusDrop.setPromptText("Set Status");
    }

    // adds items into the inventory
    @FXML
    private void addItem() {
        // validation check
        if (prodNameField.getText().trim().isEmpty() || prodPriceField.getText().trim().isEmpty() || prodStockField.getText().trim().isEmpty() || categoryDrop.getValue().trim().isEmpty() || typeDrop.getValue().trim().isEmpty() || statusDrop.getValue().trim().isEmpty()) 
        {
            System.out.println("Please fill in all fields before adding a product.");
            return;
        }

        // collect values
        String name = prodNameField.getText().trim();
        double price = Double.parseDouble(prodPriceField.getText().trim());
        int stock = Integer.parseInt(prodStockField.getText().trim());
        String type = typeDrop.getValue().trim();
        String status = statusDrop.getValue().trim();
        String category = categoryDrop.getValue().trim();

        try (Connection conn = SQLite_Connection.connect()) 
        {
            // check for duplicate name
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM Meal WHERE meal_name = ?")) 
            {
                checkStmt.setString(1, name);
                try (ResultSet rs = checkStmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        System.out.println("Item with the same name already exists!");
                        return; // stop execution
                    }
                }
            }

            // insert if no duplicate
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Meal (meal_name, category, type, price, amount_sold, amount_stock, amount_discount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) 
            {
                ps.setString(1, name);
                ps.setString(2, category);
                ps.setString(3, type);
                ps.setDouble(4, price);
                ps.setInt(5, 0);
                ps.setInt(6, stock);
                ps.setInt(7, 0); 
                ps.setString(8, status);

                // refresh table and clears all fields
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) 
                {
                    System.out.println("Item added successfully!");
                    loadItems();
                    clearFields();
                }

                // get auto-generated prod_id
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        System.out.println("Inserted product with ID: " + newId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //updates items that are inside the inventory
    @FXML
    private void updateItem ()
    {
       // validation check
        if (prodIDField.getText().trim().isEmpty() || prodNameField.getText().trim().isEmpty() || prodPriceField.getText().trim().isEmpty() || prodStockField.getText().trim().isEmpty() || categoryDrop.getValue().trim().isEmpty() || typeDrop.getValue().trim().isEmpty() || statusDrop.getValue().trim().isEmpty()) 
        {
            System.out.println("Please fill in all fields before updating a product.");
            return; // stop execution
        }

        try (Connection conn = SQLite_Connection.connect()) 
        {
            try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE Meal SET meal_name = ?, price = ?, amount_stock = ?, category = ?, type = ?, status = ? WHERE meal_id = ?")) 
            {
                // collect values
                String prodID = prodIDField.getText().trim();
                String prodName = prodNameField.getText().trim();
                double prodPrice = Double.parseDouble(prodPriceField.getText().trim());
                int prodStock = Integer.parseInt(prodStockField.getText().trim());
                String category = categoryDrop.getValue().trim();
                String type = typeDrop.getValue().trim();
                String status = statusDrop.getValue().trim();

                // update parameters
                updateStmt.setString(1, prodName);
                updateStmt.setDouble(2, prodPrice);
                updateStmt.setInt(3, prodStock);
                updateStmt.setString(4, category);
                updateStmt.setString(5, type);
                updateStmt.setString(6, status);
                updateStmt.setString(7, prodID);

                // refresh table and clears all fields
                int dataTouched = updateStmt.executeUpdate();
                if (dataTouched == 0) 
                {
                    System.out.println("Item not found!");
                } 
                else 
                {
                    System.out.println("Item updated successfully!");
                    loadItems();
                    clearFields();
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    //deletes items inside the inventory
    @FXML
    private void deleteItem ()
    {
        String prodID = prodIDField.getText().trim();
        String prodName  = prodNameField.getText().trim();

        // validation (using && because it only needs at least one of them to be filled)
        if (prodID.isEmpty() && prodName.isEmpty()) 
        {
            System.out.println("Please fill in id or name field before deleting a product");
            return; // stop execution
        }

        try (Connection conn = SQLite_Connection.connect()) 
        {
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Meal WHERE meal_id = ? OR meal_name = ?")) 
            {
                deleteStmt.setString(1, prodID);
                deleteStmt.setString(2, prodName);

                // refresh table and clears all fields
                int dataTouched = deleteStmt.executeUpdate();
                if (dataTouched == 0) 
                {
                    System.out.println("Item not found!");
                } 
                else 
                {
                    System.out.println("Item deleted successfully!");
                    loadItems();
                    clearFields();
                }
            }
        } 
    
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    //navigation methods
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
