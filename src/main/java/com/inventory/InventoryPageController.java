package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class InventoryPageController implements Initializable
{
    @FXML private AnchorPane parentContainer;
    @FXML private AnchorPane selectedBar;
    @FXML private Label welcomeMessage;

    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button analyticsButton;

    @FXML private ImageView fridgePink;
    @FXML private ImageView cutleryWhite;
    @FXML private ImageView chickenWhite;
    @FXML private ImageView hatWhite;

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
        // populate all combo boxes
        categoryDrop.getItems().addAll("Breakfast", "Lunch", "Dinner", "Appetizer");
        typeDrop.getItems().addAll("Vegetarian", "Non-Vegetarian");
        statusDrop.getItems().addAll("Available", "Unavailable");
        categoryDrop.setPromptText("Select Category");
        typeDrop.setPromptText("Select Type");
        statusDrop.setPromptText("Set Status");

        // configure custom button cells so placeholder text shows when value is null
        categoryDrop.setButtonCell(new ListCell<>() 
        {
            @Override
            protected void updateItem(String item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) 
                setText("Select Category");

                else 
                setText(item);
            }
        });

        typeDrop.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) 
                setText("Select Type");

                else 
                setText(item);
                
            }
        });

        statusDrop.setButtonCell(new ListCell<>() 
        {
            @Override
            protected void updateItem(String item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) 
                setText("Set Status");
                
                else 
                setText(item);
            }
        });
        
        // bind columns to Meal properties
        inventoryProductID.setCellValueFactory(cellData -> cellData.getValue().prodIdProperty());
        inventoryProductName.setCellValueFactory(cellData -> cellData.getValue().prodNameProperty());
        inventoryCategory.setCellValueFactory(cellData -> cellData.getValue().prodCategoryProperty());
        inventoryType.setCellValueFactory(cellData -> cellData.getValue().prodTypeProperty());
        inventoryStock.setCellValueFactory(cellData -> cellData.getValue().amountStockProperty());
        inventoryPrice.setCellValueFactory(cellData -> cellData.getValue().prodPriceProperty());
        inventoryDiscount.setCellValueFactory(cellData -> cellData.getValue().amountDiscountProperty());
        inventoryStatus.setCellValueFactory(cellData -> cellData.getValue().prodStatusProperty());

        // below is the code for animating the sidebar slide
        Color fromColor = new Color(0.906, 0.427, 0.541, 1.0);
        Color toColor = new Color(0.973, 0.914, 0.898, 1.0);

        Timeline timelineInv = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(inventoryButton.textFillProperty(), fromColor)),
            new KeyFrame(Duration.millis(100), new KeyValue(inventoryButton.textFillProperty(), toColor))
        );

        FadeTransition fadeFridge = new FadeTransition(Duration.millis(100), fridgePink);
        fadeFridge.setFromValue(1);
        fadeFridge.setToValue(0);

        menuButton.setOnAction(e -> {
            // disable the button
            menuButton.setDisable(true);
            menuButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineMenu = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(menuButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(300), new KeyValue(menuButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeCutlery = new FadeTransition(Duration.millis(300), cutleryWhite);
            fadeCutlery.setFromValue(1);
            fadeCutlery.setToValue(0);
            // play the animations and fade
            timelineInv.play();
            timelineMenu.play();

            fadeFridge.play();
            fadeCutlery.play();
            // switch to menu
            try { switchToMenu(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

        ordersButton.setOnAction(e -> {
            // disable the button
            ordersButton.setDisable(true);
            ordersButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineOrders = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(ordersButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(350), new KeyValue(ordersButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeChicken = new FadeTransition(Duration.millis(350), chickenWhite);
            fadeChicken.setFromValue(1);
            fadeChicken.setToValue(0);
            // play the animations and fade
            timelineInv.play();
            timelineOrders.play();

            fadeFridge.play();
            fadeChicken.play();
            // switch to orders
            try { switchToOrders(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

        analyticsButton.setOnAction(e -> {
            // disable the button
            analyticsButton.setDisable(true);
            analyticsButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineAnalytics = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(analyticsButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(400), new KeyValue(analyticsButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeHat = new FadeTransition(Duration.millis(400), hatWhite);
            fadeHat.setFromValue(1);
            fadeHat.setToValue(0);
            // play the animations and fade
            timelineInv.play();
            timelineAnalytics.play();

            fadeFridge.play();
            fadeHat.play();
            // switch to analytics
            try { switchToAnalytics(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });
    }

    // animation helper for moving the sidebar up and down
    private void playAnimation(String fxml, int duration, int setToY)
    {
        TranslateTransition transition = new TranslateTransition();
       
        transition.setNode(selectedBar);
        transition.setDuration(Duration.millis(duration));
        
        transition.setInterpolator(Interpolator.SPLINE(0.70, 0.0, 0.30, 1.0));
        transition.setToY(setToY);

        transition.play();
        transition.setOnFinished(event -> {
            try { App.setRoot(fxml, App.MAIN_WIDTH, App.MAIN_HEIGHT); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }

    // navigation methods
    @FXML
    private void switchToMenu() throws IOException 
    {
        playAnimation("menuPage", 300, 100);
    }

    @FXML
    private void switchToOrders() throws IOException 
    {
        playAnimation("ordersPage", 350, 200);
    }

    @FXML
    private void switchToAnalytics() throws IOException 
    {
        playAnimation("analyticsPage", 400, 300);
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("openingAnimation", App.WIDTH, App.HEIGHT);
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
        if (prodIDField.getText().trim().isEmpty()) 
        {
            System.out.println("Please fill in product ID before updating a product.");
            return; // stop execution
        }

        // collect values
        String prodID = prodIDField.getText().trim();
        String prodName = prodNameField.getText().trim();
        String prodPrice = prodPriceField.getText().trim();
        String prodStock = prodStockField.getText().trim();
        String category = categoryDrop.getValue() != null ? categoryDrop.getValue().trim() : "";
        String type = typeDrop.getValue() != null ? typeDrop.getValue().trim() : "";
        String status = statusDrop.getValue() != null ? statusDrop.getValue().trim() : "";

        // makes query statement based on what values are present (keep in mind that category, type, and drop still need to be filled)
        StringBuilder sql = new StringBuilder("UPDATE Meal SET ");
        List<Object> params = new ArrayList<>();

        if (!prodName.isEmpty()) 
        {
            sql.append("meal_name = ?, ");
            params.add(prodName);
        }
        if (!prodPrice.isEmpty()) {
            sql.append("price = ?, ");
            params.add(Double.parseDouble(prodPrice));
        }
        if (!prodStock.isEmpty()) {
            sql.append("amount_stock = ?, ");
            params.add(Integer.parseInt(prodStock));
        }
        if (!category.isEmpty()) {
            sql.append("category = ?, ");
            params.add(category);
        }
        if (!type.isEmpty()) {
            sql.append("type = ?, ");
            params.add(type);
        }
        if (!status.isEmpty()) {
            sql.append("status = ?, ");
            params.add(status);
        }

        if (params.isEmpty()) 
        {
            System.out.println("No changes provided. Update aborted.");
            return;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE meal_id = ?");
        params.add(prodID);

        try (Connection conn = SQLite_Connection.connect();
        PreparedStatement updateStmt = conn.prepareStatement(sql.toString())) 
        {
            // updates parameters
            for (int i = 0; i < params.size(); i++) 
            {
                Object value = params.get(i);
                if (value instanceof String) updateStmt.setString(i + 1, (String) value);
                else if (value instanceof Integer) updateStmt.setInt(i + 1, (Integer) value);
                else if (value instanceof Double) updateStmt.setDouble(i + 1, (Double) value);
                else updateStmt.setObject(i + 1, value);
            }

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
}
