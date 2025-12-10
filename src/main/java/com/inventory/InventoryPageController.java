package com.inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// TODO: ADD FUNCTIONALITY TO MAKE AVAILABLE FOOD UNAVAILABLE ONCE STOCK REACHES 0
// TODO: ADD FUNCTIONALITY OF DISCOUNT FIELD
public class InventoryPageController implements Initializable
{
    @FXML private AnchorPane parentContainer;
    @FXML private AnchorPane selectedBar;
    @FXML private AnchorPane imageDropPane;
    @FXML private Label welcomeMessage;

    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button analyticsButton;

    @FXML private ImageView fridgePink;
    @FXML private ImageView cutleryWhite;
    @FXML private ImageView chickenWhite;
    @FXML private ImageView hatWhite;
    @FXML private ImageView importImageDrop;

    @FXML private TableView<Product> inventoryTable;
    @FXML private TableColumn<Product, Number> inventoryProductID;
    @FXML private TableColumn<Product, String> inventoryProductName;
    @FXML private TableColumn<Product, String> inventoryProductCategory;
    @FXML private TableColumn<Product, String> inventoryProductType;
    @FXML private TableColumn<Product, Number> inventoryProductStock;
    @FXML private TableColumn<Product, Number> inventoryProductPrice;
    @FXML private TableColumn<Product, Number> inventoryProductDiscount;
    @FXML private TableColumn<Product, String> inventoryProductStatus;

    @FXML TextField searchBar;
    @FXML TextField prodIDField;
    @FXML TextField prodNameField;
    @FXML TextField prodStockField;
    @FXML TextField prodPriceField;
    @FXML TextField prodDiscountField;

    @FXML ComboBox<String> categoryDrop;
    @FXML ComboBox<String> typeDrop;
    @FXML ComboBox<String> statusDrop;

    @FXML Button signOutButton;
    @FXML Button filterButton;
    @FXML Button importImageButton;

    private Connection conn;
    private File selectedImage;

    // master list of products
    private ObservableList<Product> data = FXCollections.observableArrayList();

    // stores the last state of filter buttons
    private boolean breakfastFilter, lunchFilter, dinnerFilter, appetizerFilter, vegetarianFilter, nonVegetarianFilter;

    // initializes combo boxes and the table
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
                if (empty || item == null) setText("Select Category");
                else setText(item);
            }
        });

        typeDrop.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) setText("Select Type");
                else setText(item);
            }
        });

        statusDrop.setButtonCell(new ListCell<>() 
        {
            @Override
            protected void updateItem(String item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) setText("Set Status");
                else setText(item);
            }
        });
        
        // bind columns to Product properties
        inventoryProductID.setCellValueFactory(cellData -> cellData.getValue().prodIDProperty());
        inventoryProductName.setCellValueFactory(cellData -> cellData.getValue().prodNameProperty());
        inventoryProductCategory.setCellValueFactory(cellData -> cellData.getValue().prodCategoryProperty());
        inventoryProductType.setCellValueFactory(cellData -> cellData.getValue().prodTypeProperty());
        inventoryProductStock.setCellValueFactory(cellData -> cellData.getValue().prodAmountStockProperty());
        inventoryProductPrice.setCellValueFactory(cellData -> cellData.getValue().prodPriceProperty());
        inventoryProductDiscount.setCellValueFactory(cellData -> cellData.getValue().prodAmountDiscountProperty());
        inventoryProductStatus.setCellValueFactory(cellData -> cellData.getValue().prodStatusProperty());

        // listener for search bar
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->
        {
            filterTable(newValue, breakfastFilter, lunchFilter, dinnerFilter, appetizerFilter, vegetarianFilter, nonVegetarianFilter);
        });

        // gets the username of the person from the session
        welcomeMessage.setText("Welcome, " + Session.getUsername() + "!");

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
        playAnimation("analyticsPage_IncomeView", 400, 300);
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("openingAnimation", App.WIDTH, App.HEIGHT);
    }

    // opens the filter menu for inventory
    @FXML
    private void switchToFilterInventory()
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("filterInventory.fxml"));
            Parent root = loader.load();

            filterInventoryController popupController = loader.getController();
            popupController.setController(this);

            Stage popupStage = new Stage();
            popupStage.setTitle("Filter Inventory");
            popupStage.setResizable(false);
            popupStage.setScene(new Scene(root));
            popupStage.initOwner(parentContainer.getScene().getWindow());
            popupStage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    // allow main app to inject DB connection
    public void setConnection(Connection conn) 
    {
        this.conn = conn;
    }

    // load products from database
    public void loadItems() 
    {
        data.clear();
        String sql = "SELECT prod_id, prod_name, category, type, prod_price, amount_sold, amount_stock, amount_discount, status FROM Product";

        try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) 
            {
                data.add(new Product
                (
                    rs.getInt("prod_id"),
                    rs.getString("prod_name"),
                    rs.getString("category"),
                    rs.getString("type"),
                    rs.getDouble("prod_price"),
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

        // shows items in the table
        inventoryTable.setItems(data);

        // reapplies any search if user has typed something
        String currentSearch = searchBar.getText();
        if(currentSearch != null && !currentSearch.isEmpty())
        {
            filterTable(currentSearch, breakfastFilter, lunchFilter, dinnerFilter, appetizerFilter, vegetarianFilter, nonVegetarianFilter);
        }
    }

    // applies filters to the table to show/hide different data
    public void filterTable(String keyword, boolean breakfast, boolean lunch, boolean dinner, boolean appetizer, boolean vegetarian, boolean nonVegetarian)
    {
        ObservableList<Product> filtered = FXCollections.observableArrayList();

        for (Product p : data) 
        {
            String name = p.getProdName() != null ? p.getProdName().trim() : "";
                
            // filters by case-sensitive keyword on search bar
            boolean matchesSearch =
                (keyword == null || keyword.trim().isEmpty() || name.contains(keyword.trim()));
                
            // filters by category
            boolean matchesCategory =
                (!breakfast && !lunch && !dinner && !appetizer) ||
                (breakfast && "Breakfast".equals(p.getProdCategory())) ||
                (lunch && "Lunch".equals(p.getProdCategory())) ||
                (dinner && "Dinner".equals(p.getProdCategory())) ||
                (appetizer && "Appetizer".equals(p.getProdCategory()));

            // filters by type
            boolean matchesType =
                (!vegetarian && !nonVegetarian) ||
                (vegetarian && "Vegetarian".equals(p.getProdType())) ||
                (nonVegetarian && "Non-Vegetarian".equals(p.getProdType()));

            if (matchesSearch && matchesCategory && matchesType) 
            {
                filtered.add(p);
            }
        }
        inventoryTable.setItems(filtered);
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

        selectedImage = null;
        importImageDrop.setImage(null);
    }

    // handles image importing
    // opens FileChooser when button is clicked
    @FXML
    private void chooseImage()
    {
        FileChooser chooseImg = new FileChooser();
        chooseImg.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = chooseImg.showOpenDialog(importImageButton.getScene().getWindow());
        if (file != null)
        {
            selectedImage = file;
            importImageDrop.setImage(new Image(file.toURI().toString()));
        }
    }

    // handles dragging over images
    @FXML
    private void handleDragOver (DragEvent event)
    {
        if (event.getDragboard().hasFiles())
        {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    // handles dropping images
    @FXML
    private void handleDragDropped (DragEvent event)
    {
        Dragboard db = event.getDragboard();
        if (db.hasFiles())
        {
            File file = db.getFiles().get(0);
            if (file.getName().matches(".*\\.(png|jpg|jpeg)$"))
            {
                selectedImage = file;
                importImageDrop.setImage(new Image(file.toURI().toString()));
            }
        }

        event.setDropCompleted(true);
        event.consume();
    }

    // adds items into the inventory
    @FXML
    private void addItem() {
        // validation check
        if (prodNameField.getText().trim().isEmpty() || 
            prodPriceField.getText().trim().isEmpty() || 
            prodStockField.getText().trim().isEmpty() || 
            categoryDrop.getValue().trim().isEmpty() || 
            typeDrop.getValue().trim().isEmpty() || 
            statusDrop.getValue().trim().isEmpty()) 
        {
            // TODO: ADD ERROR MESSAGE ON WINDOW INSTEAD FROM CONSOLE
            System.out.println("Please fill in all fields before adding a product.");
            return;
        }

        // collect values
        String name = prodNameField.getText().trim();
        double prod_price = Double.parseDouble(prodPriceField.getText().trim());
        int stock = Integer.parseInt(prodStockField.getText().trim());
        String type = typeDrop.getValue().trim();
        String status = statusDrop.getValue().trim();
        String category = categoryDrop.getValue().trim();

        try (Connection conn = SQLite_Connection.connect()) 
        {
            // check for duplicate name
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM Product WHERE prod_name = ?")) 
            {
                checkStmt.setString(1, name);
                try (ResultSet rs = checkStmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        // TODO: ADD ERROR MESSAGE FOR THIS
                        System.out.println("Item with the same name already exists!");
                        return; // stop execution
                    }
                }
            }

            // insert if no duplicate
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Product (prod_name, category, type, prod_price, amount_sold, amount_stock, amount_discount, status, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) 
            {
                ps.setString(1, name);
                ps.setString(2, category);
                ps.setString(3, type);
                ps.setDouble(4, prod_price);
                ps.setInt(5, 0);
                ps.setInt(6, stock);
                ps.setInt(7, 0); 
                ps.setString(8, status);

                // adds image if there is
                if (selectedImage != null)
                {
                    try (FileInputStream fis = new FileInputStream(selectedImage))
                    {
                        ps.setBinaryStream(9, fis, (int) selectedImage.length());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        ps.setNull(9, java.sql.Types.BLOB);
                    }
                }
                else
                {
                    ps.setNull(9, java.sql.Types.BLOB);
                }

                // refresh table and clears all fields
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) 
                {
                    // TODO: ADD THIS MESSAGE TO WINDOW
                    System.out.println("Item added successfully!");
                    loadItems();
                    clearFields();
                }

                // get auto-generated prod_id
                try (ResultSet rs = ps.getGeneratedKeys()) 
                {
                    if (rs.next()) 
                    {
                        int newId = rs.getInt(1);
                        System.out.println("Inserted product with ID: " + newId);
                    }
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    // updates items that are inside the inventory
    @FXML
    private void updateItem ()
    {
        // validation check
        if (prodIDField.getText().trim().isEmpty()) 
        {
            // TODO: ADD ERROR MESSAGE
            System.out.println("Please fill in product ID before updating a product.");
            return; // stop execution
        }

        // collect values
        String prodID = prodIDField.getText().trim();
        String prodName = prodNameField.getText().trim();
        String prodPrice = prodPriceField.getText().trim();
        String prodStock = prodStockField.getText().trim();
        String prodCategory = categoryDrop.getValue() != null ? categoryDrop.getValue().trim() : "";
        String prodType = typeDrop.getValue() != null ? typeDrop.getValue().trim() : "";
        String prodStatus = statusDrop.getValue() != null ? statusDrop.getValue().trim() : "";

        // makes query statement based on what values are present 
        // keep in mind that category, type, and drop still need to be filled
        StringBuilder sql = new StringBuilder("UPDATE Product SET ");
        List<Object> params = new ArrayList<>();

        if (!prodName.isEmpty()) 
        {
            sql.append("prod_name = ?, ");
            params.add(prodName);
        }
    
        if (!prodPrice.isEmpty()) 
        {
            sql.append("prod_price = ?, ");
            params.add(Double.parseDouble(prodPrice));
        }
    
        if (!prodStock.isEmpty()) 
        {
            sql.append("amount_stock = ?, ");
            params.add(Integer.parseInt(prodStock));
        }
    
        if (!prodCategory.isEmpty())
        {
            sql.append("category = ?, ");
            params.add(prodCategory);
        }

        if (!prodType.isEmpty()) 
        {
            sql.append("type = ?, ");
            params.add(prodType);
        }

        if (!prodStatus.isEmpty()) 
        {
            sql.append("status = ?, ");
            params.add(prodStatus);
        }

        if (selectedImage != null)
        {
            sql.append("image = ?, ");
            params.add(selectedImage);
        }

        if (params.isEmpty()) 
        {
            // TODO: ADD ERROR MESSAGE FOR THIS
            System.out.println("No changes provided. Update aborted.");
            return;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE prod_id = ?");
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
                else if (value instanceof File)
                    {
                        try (FileInputStream fis = new FileInputStream((File) value)) 
                        { 
                            updateStmt.setBinaryStream(i + 1, fis, (int) ((File) value).length());
                        }  
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    } 
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
                // TODO: ADD ERROR MESSAGE FOR THIS
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

    // TODO: HANDLE EDGE CASES FOR DELETING AN ITEM E.G ITEM BEING DELETED WAS ON AN ORDER
    // deletes items inside the inventory
    @FXML
    private void deleteItem ()
    {
        String prodID = prodIDField.getText().trim();
        String prodName  = prodNameField.getText().trim();

        // validation (using && because it only needs at least one of them to be filled)
        if (prodID.isEmpty() && prodName.isEmpty()) 
        {
            // TODO: ADD ERROR MESSAGE FOR THIS
            System.out.println("Please fill in id or name field before deleting a product");
            return; // stop execution
        }

        try (Connection conn = SQLite_Connection.connect()) 
        {
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Product WHERE prod_id = ? OR prod_name = ?")) 
            {
                deleteStmt.setString(1, prodID);
                deleteStmt.setString(2, prodName);

                // refresh table and clears all fields
                int dataTouched = deleteStmt.executeUpdate();
                if (dataTouched == 0) 
                {
                    // TODO: ADD ERROR MESSAGE FOR THIS
                    System.out.println("Item not found!");
                } 
                else 
                {
                    // TODO: ADD MESSAGE FOR THIS IN WINDOW
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
