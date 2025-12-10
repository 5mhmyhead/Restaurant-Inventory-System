package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OrdersPageController implements Initializable
{
    @FXML private AnchorPane parentContainer;
    @FXML private AnchorPane selectedBar;
    @FXML private Label welcomeMessage;

    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button analyticsButton;

    @FXML private ImageView fridgeWhite;
    @FXML private ImageView cutleryWhite;
    @FXML private ImageView chickenPink;
    @FXML private ImageView hatWhite;
    
    @FXML TextField searchBar;

    @FXML Button filterMenuButton;
    @FXML Button signOutButton;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Number> orderIDTable;
    @FXML private TableColumn<Order, Number> userIDTable;
    @FXML private TableColumn<Order, Number> prodIDTable;
    @FXML private TableColumn<Order, Number> customerIDTable;
    @FXML private TableColumn<Order, Number> totalAmountTable;
    @FXML private TableColumn<Order, Number> quantityTable;
    @FXML private TableColumn<Order, String> statusTable;
    @FXML private TableColumn<Order, String> dateTable;
    @FXML private TableColumn<Order, String> cashierTable;

    // master list of products
    private ObservableList<Order> data = FXCollections.observableArrayList();

    // TODO: ADD FUNCTIONALITY TO CHANGE ORDERS FROM PENDING TO COMPLETED OR CANCELLED
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // loads the orders table
        orderIDTable.setCellValueFactory(cellData -> cellData.getValue().orderIDProperty());
        userIDTable.setCellValueFactory(cellData -> cellData.getValue().orderUserIDProperty());
        prodIDTable.setCellValueFactory(cellData -> cellData.getValue().orderProdIDProperty());
        customerIDTable.setCellValueFactory(cellData -> cellData.getValue().orderCustomerIDProperty());
        totalAmountTable.setCellValueFactory(cellData -> cellData.getValue().orderTotalAmountProperty());
        quantityTable.setCellValueFactory(cellData -> cellData.getValue().orderQuantityProperty());
        statusTable.setCellValueFactory(cellData -> cellData.getValue().orderStatusProperty());
        dateTable.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        cashierTable.setCellValueFactory(cellData -> cellData.getValue().orderCashierProperty());
        loadOrders();

        // gets the username of the person from the session
        welcomeMessage.setText("Welcome, " + Session.getUsername() + "!");

        // below is the code for animating the sidebar slide
        Color fromColor = new Color(0.906, 0.427, 0.541, 1.0);
        Color toColor = new Color(0.973, 0.914, 0.898, 1.0);

        Timeline timelineOrders = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(ordersButton.textFillProperty(), fromColor)),
            new KeyFrame(Duration.millis(100), new KeyValue(ordersButton.textFillProperty(), toColor))
        );

        FadeTransition fadeChicken = new FadeTransition(Duration.millis(100), chickenPink);
        fadeChicken.setFromValue(1);
        fadeChicken.setToValue(0);

        inventoryButton.setOnAction(e -> {
            // disable the button
            inventoryButton.setDisable(true);
            inventoryButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineInventory = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(inventoryButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(350), new KeyValue(inventoryButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeFridge = new FadeTransition(Duration.millis(350), fridgeWhite);
            fadeFridge.setFromValue(1);
            fadeFridge.setToValue(0);
            // play the animations and fade
            timelineOrders.play();
            timelineInventory.play();

            fadeFridge.play();
            fadeChicken.play();
            // switch to inventory
            try { switchToInventory(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

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
            timelineMenu.play();
            timelineOrders.play();

            fadeCutlery.play();
            fadeChicken.play();
            // switch to menu
            try { switchToMenu(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

        analyticsButton.setOnAction(e -> {
            // disable the button
            analyticsButton.setDisable(true);
            analyticsButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineAnalytics = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(analyticsButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(300), new KeyValue(analyticsButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeHat = new FadeTransition(Duration.millis(300), hatWhite);
            fadeHat.setFromValue(1);
            fadeHat.setToValue(0);
            // play the animations and fade
            timelineOrders.play();
            timelineAnalytics.play();

            fadeChicken.play();
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

    @FXML
    private void switchToInventory() throws IOException 
    {
        playAnimation("inventoryPage", 350, -200);
    }

    @FXML
    private void switchToMenu() throws IOException 
    {
        playAnimation("menuPage", 300, -100);
    }

    @FXML
    private void switchToAnalytics() throws IOException 
    {
        playAnimation("analyticsPage_IncomeView", 300, 100);
    }

    @FXML
    private void switchToFilterOrder() throws IOException 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("filterOrders.fxml"));
            Parent root = loader.load();

            FilterOrderController popupController = loader.getController();
            popupController.setController(this);

            Stage popupStage = new Stage();
            popupStage.setTitle("Filter Orders");
            popupStage.setResizable(false);
            popupStage.setScene(new Scene(root));
            popupStage.initOwner(parentContainer.getScene().getWindow());

            Image icon = new Image(getClass().getResourceAsStream("/com/inventory/images/helloKittyIcon.png"));
            popupStage.getIcons().add(icon);

            popupStage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("openingAnimation", App.WIDTH, App.HEIGHT);
    }

    // load orders from database
    public void loadOrders() 
    {
        String sql = "SELECT o.order_id, o.user_id, o.prod_id, o.customer_id, o.total_amount, " 
                   + "o.order_quantity, o.order_status, o.order_date, a.username AS cashier " 
                   + "FROM ORDERS o " 
                   + "JOIN ACCOUNT a ON o.user_id = a.user_id";

        try (Connection conn = SQLite_Connection.connect(); 
        Statement stmt = conn.createStatement(); 
        ResultSet rs = stmt.executeQuery(sql)) 
        {
            ordersTable.getItems().clear();

            while (rs.next()) 
            {
                data.add(new Order
                (
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getInt("prod_id"),
                    rs.getInt("customer_id"),
                    rs.getDouble("total_amount"),
                    rs.getInt("order_quantity"),
                    rs.getString("order_status"),
                    rs.getString("order_date"),
                    rs.getString("cashier")
                ));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // shows items in the table
        ordersTable.setItems(data);

        // TODO: REAPPLY SEARCH
        // reapplies any search if user has typed something
    }

    // applies filters to the table to show/hide different data
    public void filterTable(String keyword, String sql)
    {
        ObservableList<Order> filtered = FXCollections.observableArrayList();

        for (Order o : data) 
        {
            
        }
        
        ordersTable.setItems(filtered);
    }

}
