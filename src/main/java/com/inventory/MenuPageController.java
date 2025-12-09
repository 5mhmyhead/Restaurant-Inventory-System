package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class MenuPageController implements Initializable
{    
    @FXML private FlowPane menuCardContainer;

    @FXML private AnchorPane parentContainer;
    @FXML private AnchorPane selectedBar;
    @FXML private Label welcomeMessage;

    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button analyticsButton;

    @FXML private ImageView fridgeWhite;
    @FXML private ImageView cutleryPink;
    @FXML private ImageView chickenWhite;
    @FXML private ImageView hatWhite;

    @FXML private TableView<MenuOrder> menuOrdersTable;
    @FXML private TableColumn<MenuOrder, Double > orderTablePrice;
    @FXML private TableColumn<MenuOrder, String>  orderTableProdName;
    @FXML private TableColumn<MenuOrder, Integer>  orderTableQuantity;

    @FXML private Label amountDue;
    @FXML private Label totalAmount;
    @FXML private Label amountChange;
    @FXML private TextField amountPay;

    @FXML Button filterMenuButton;
    @FXML Button signOutButton;

    private double dueTotal = 0.0;
    private static int customerCounter = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // gets the username of the person from the session
        welcomeMessage.setText("Welcome, " + Session.getUsername() + "!");

        // loads the menu cards
        loadMenu();
        orderTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        orderTableProdName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        orderTableQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menuOrdersTable.getItems().addListener((ListChangeListener<MenuOrder>) change -> updateAmountDue());

        // below is the code for animating the sidebar slide
        Color fromColor = new Color(0.906, 0.427, 0.541, 1.0);
        Color toColor = new Color(0.973, 0.914, 0.898, 1.0);

        Timeline timelineMenu = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(menuButton.textFillProperty(), fromColor)),
            new KeyFrame(Duration.millis(100), new KeyValue(menuButton.textFillProperty(), toColor))
        );

        FadeTransition fadeCutlery = new FadeTransition(Duration.millis(100), cutleryPink);
        fadeCutlery.setFromValue(1);
        fadeCutlery.setToValue(0);

        inventoryButton.setOnAction(e -> {
            // disable the button
            inventoryButton.setDisable(true);
            inventoryButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineInventory = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(inventoryButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(300), new KeyValue(inventoryButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeFridge = new FadeTransition(Duration.millis(300), fridgeWhite);
            fadeFridge.setFromValue(1);
            fadeFridge.setToValue(0);
            // play the animations and fade
            timelineMenu.play();
            timelineInventory.play();

            fadeFridge.play();
            fadeCutlery.play();
            // switch to inventory
            try { switchToInventory(); }
            catch (IOException e2) { e2.printStackTrace(); }
        });

        ordersButton.setOnAction(e -> {
            // disable the button
            ordersButton.setDisable(true);
            ordersButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineOrders = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(ordersButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(300), new KeyValue(ordersButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeChicken = new FadeTransition(Duration.millis(300), chickenWhite);
            fadeChicken.setFromValue(1);
            fadeChicken.setToValue(0);
            // play the animations and fade
            timelineMenu.play();
            timelineOrders.play();

            fadeCutlery.play();
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
                    new KeyFrame(Duration.millis(350), new KeyValue(analyticsButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeHat = new FadeTransition(Duration.millis(350), hatWhite);
            fadeHat.setFromValue(1);
            fadeHat.setToValue(0);
            // play the animations and fade
            timelineMenu.play();
            timelineAnalytics.play();

            fadeCutlery.play();
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
    private void switchToInventory() throws IOException 
    {
        playAnimation("inventoryPage", 300, -100);
    }

    @FXML
    private void switchToOrders() throws IOException 
    {
        playAnimation("ordersPage", 300, 100);
    }

    @FXML
    private void switchToAnalytics() throws IOException 
    {
       playAnimation("analyticsPage_IncomeView", 350, 200);
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("openingAnimation", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToFilterMenu() throws IOException 
    { 
        App.setRoot("filterMenu", App.WIDTH, App.HEIGHT);
    }

    // loads product cards
    private void loadMenu()
    {
        menuCardContainer.getChildren().clear();
        String sql = "SELECT prod_id, meal_name, category, type, prod_price, amount_sold, amount_stock, amount_discount, status, image FROM Product";

        try (Connection conn = SQLite_Connection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) 
            {
                byte[] imageBytes = rs.getBytes("image");
                Product product = new Product
                (
                    rs.getInt("prod_id"),
                    rs.getString("meal_name"),
                    rs.getString("category"),
                    rs.getString("type"),
                    rs.getDouble("prod_price"),
                    rs.getInt("amount_sold"),
                    rs.getInt("amount_stock"),
                    rs.getInt("amount_discount"),
                    rs.getString("status"),
                    imageBytes
                );

                FXMLLoader loader = new FXMLLoader(getClass().getResource("menuCard.fxml"));
                AnchorPane card = loader.load();
                MenuPageCardController controller = loader.getController();
                controller.setData(product);
                controller.setOrdersTable(menuOrdersTable);
                menuCardContainer.getChildren().add(card);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // updates amount due
    private void updateAmountDue()
    {
        dueTotal = menuOrdersTable.getItems().stream().mapToDouble(order -> order.getPrice() * order.getQuantity()).sum();
        amountDue.setText("Amount Due: P" + String.format("%.2f", dueTotal));
    }

    @FXML
    private void payOrder (ActionEvent event)
    {
        double payment;
        try 
        {
            payment = Double.parseDouble(amountPay.getText());
        }
        catch (NumberFormatException e)
        {
            return;
        }

        if (payment < dueTotal)
        {
            return;
        }
        customerCounter++;
        int customerId = customerCounter;

        // updates stock for each ordered item in the database
        for (MenuOrder order : menuOrdersTable.getItems()) 
        {
            try (Connection conn = SQLite_Connection.connect(); PreparedStatement pstmt = conn.prepareStatement("UPDATE Product SET amount_stock = amount_stock - ? WHERE meal_name = ?")) 
            {
                pstmt.setInt(1, order.getQuantity());
                pstmt.setString(2, order.getProdName());
                pstmt.executeUpdate();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            // insert into Orders
            try (Connection conn = SQLite_Connection.connect(); PreparedStatement pstmtOrder = conn.prepareStatement("INSERT INTO Orders (user_id, prod_id, customer_id, total_amount, order_quantity, order_status, order_date) " + "VALUES (?, ?, ?, ?, ?, ?, ?)")) 
            {
                pstmtOrder.setInt(1, Session.getUserId());
                pstmtOrder.setInt(2, order.getMealId());
                pstmtOrder.setInt(3, customerId);
                pstmtOrder.setDouble(4, order.getPrice() * order.getQuantity());
                pstmtOrder.setInt(5, order.getQuantity());
                pstmtOrder.setString(6, "Pending");
                pstmtOrder.setString(7, java.time.LocalDateTime.now().toString());
                pstmtOrder.executeUpdate();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }

        double change = payment - dueTotal;
        amountChange.setText("Change: P" + String.format("%.2f", change));

        menuOrdersTable.getItems().clear();
        updateAmountDue();
        amountPay.clear();
    }
}
