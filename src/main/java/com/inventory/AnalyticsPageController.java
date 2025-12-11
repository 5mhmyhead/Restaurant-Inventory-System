package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class AnalyticsPageController implements Initializable
{
    @FXML private AnchorPane parentContainer;
    @FXML private AnchorPane selectedBar;
    @FXML private Label welcomeMessage;

    @FXML private Button inventoryButton;
    @FXML private Button menuButton;
    @FXML private Button ordersButton;
    @FXML private Button analyticsButton;

    @FXML private Button weeklyChartButton;
    @FXML private Button incomeButton;

    @FXML private ImageView fridgeWhite;
    @FXML private ImageView cutleryWhite;
    @FXML private ImageView chickenWhite;
    @FXML private ImageView hatPink;

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> weeklyChart;

    @FXML private Label totalAmount;
    @FXML private Label totalIncome;
    @FXML private Label todaysIncome;
    @FXML private Label productsSold;
    @FXML private Label customers;

    @FXML private Label topSellingProduct;
    @FXML private Label lowestSales;

    @FXML private Label topSellingLabel;
    @FXML private Label lowestSalesLabel;

    @FXML Button signOutButton;

    // observable list for the pie chart data
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    // XYChart for the bar chart data
    XYChart.Series<String, Number> barChartDataProducts = new XYChart.Series<>();
    XYChart.Series<String, Number> barChartDataCustomers = new XYChart.Series<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // sql queries
        String sqlIncomeToday = "SELECT SUM(o.total_amount) AS total_amount_today " 
                              + "FROM Orders o "
                              + "WHERE o.order_date = '" + Session.getCurrentDate() + "'";

        String sqlTopFiveProduct = "SELECT SUM(amount_sold) AS total_amount_sold " 
                              + "FROM (SELECT amount_sold FROM Product ORDER BY amount_sold DESC LIMIT 5) "
                              + "AS top_meals";

        String sqlTopSelling = "SELECT p.prod_id, p.prod_name, SUM(o.order_quantity) AS total_times_sold "
                             + "FROM Product p "
                             + "JOIN Orders o ON p.prod_id = o.prod_id "
                             + "GROUP BY p.prod_id, p.prod_name "
                             + "ORDER BY total_times_sold DESC LIMIT 1 ";

        String sqlLowestSale = "SELECT p.prod_id, p.prod_name, SUM(o.order_quantity) AS total_times_sold "
                             + "FROM Product p "
                             + "JOIN Orders o ON p.prod_id = o.prod_id "
                             + "GROUP BY p.prod_id, p.prod_name "
                             + "ORDER BY total_times_sold ASC LIMIT 1 ";

        // sql queries for stack bar chart
        String sqlStackedBar = "WITH RECURSIVE last7days(day) AS ( "
                             + "SELECT date('now', '-6 days') UNION ALL "
                             + "SELECT date(day, '+1 day') FROM last7days WHERE day < date('now')) "
                             + "SELECT last7days.day, "
                             + "COALESCE(SUM(order_quantity), 0) AS total_sold, "
                             + "COALESCE(COUNT(DISTINCT Orders.customer_id), 0) AS customers "
                             + "FROM last7days LEFT JOIN Orders ON date(Orders.order_date) = last7days.day "
                             + "GROUP BY last7days.day ORDER BY last7days.day ";

        // pie chart styling
        pieChart.setLegendSide(Side.LEFT);
        pieChart.setLabelsVisible(false);

        barChartDataProducts.setName("Products Sold");
        barChartDataCustomers.setName("Customers");
        
        // connecting to sql database to create analytics
        try (Connection conn = SQLite_Connection.connect()) 
        {
            // find the top 5 most sold meals
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product LIMIT 5")) 
            {
                while (rs.next()) 
                {
                    pieChartData.add(new PieChart.Data(rs.getString("prod_name") + " - ₱" + rs.getInt("amount_sold"), rs.getInt("amount_sold")));
                }
            }

            // the total amount sold of the top five meals in the database
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlTopFiveProduct))
            {
                while (rs.next())
                {
                    int result = rs.getInt("total_amount_sold");
                    totalAmount.setText("" + result);
                }
            }

            // the amount of customers in the database all time
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(DISTINCT customer_id) AS total_customers FROM Orders"))
            {
                while (rs.next())
                {
                    int result = rs.getInt("total_customers");
                    customers.setText("" + result); 
                }
            }

            // the amount sold today 
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlIncomeToday))
            {
                while (rs.next())
                {
                    int result = rs.getInt("total_amount_today");
                    todaysIncome.setText("₱" + result); 
                }
            }

            // the total amount sold of every meal in the database
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(amount_sold) AS total_amount_sold FROM Product"))
            {
                while (rs.next())
                {
                    int result = rs.getInt("total_amount_sold");
                    totalIncome.setText("₱" + result);
                }
            }

            // the amount of products sold all time
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(order_quantity) AS total_products_sold FROM Orders"))
            {
                while (rs.next())
                {
                    int result = rs.getInt("total_products_sold");
                    productsSold.setText("" + result); 
                }
            }

            // finding the top selling meal
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlTopSelling))
            {
                while (rs.next())
                {
                    String meal = rs.getString("prod_name");
                    String amtSold = rs.getString("total_times_sold");
                    if (meal.length() >= 15)
                    {
                        String f = topSellingProduct.getFont().getFamily();
                        topSellingProduct.setFont(Font.font(f, 26));                        
                    }
                    topSellingProduct.setText(meal);
                    topSellingLabel.setText(amtSold + " sales this past month");
                }
            }

            // finding the meal with the lowest sales
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlLowestSale))
            {
                while (rs.next())
                {
                    String meal = rs.getString("prod_name");
                    String amtSold = rs.getString("total_times_sold");
                    if (meal.length() >= 15)
                    {
                        String f = lowestSales.getFont().getFamily();
                        lowestSales.setFont(Font.font(f, 26));                        
                    }
                    lowestSales.setText(meal);
                    lowestSalesLabel.setText(amtSold + " sales this past month");
                }
            }

            // generating the data for the stacked bar, showing the amount_sold for each day for the past week
            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStackedBar))
            {
                while (rs.next())
                {
                    String date = rs.getString("day"); 
                    int total = rs.getInt("total_sold");
                    int customers = rs.getInt("customers");

                    barChartDataProducts.getData().add(new XYChart.Data<>(date, total));
                    barChartDataCustomers.getData().add(new XYChart.Data<>(date, customers));
                }
            }

            conn.close();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        // setting the pie chart and bar chart data
        pieChart.setData(pieChartData);
        weeklyChart.getData().addAll(barChartDataProducts, barChartDataCustomers);

        // gets the username of the person from the session
        welcomeMessage.setText("Welcome, " + Session.getUsername() + "!");
        
        // below is the code for animating the sidebar slide
        Color fromColor = new Color(0.906, 0.427, 0.541, 1.0);
        Color toColor = new Color(0.973, 0.914, 0.898, 1.0);

        Timeline timelineAnalytics = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(analyticsButton.textFillProperty(), fromColor)),
            new KeyFrame(Duration.millis(100), new KeyValue(analyticsButton.textFillProperty(), toColor))
        );

        FadeTransition fadeHat = new FadeTransition(Duration.millis(100), hatPink);
        fadeHat.setFromValue(1);
        fadeHat.setToValue(0);

        inventoryButton.setOnAction(e -> {
            // disable the button
            inventoryButton.setDisable(true);
            inventoryButton.setStyle("-fx-opacity: 1.0;");
            // create a timeline for the text fill transition
            Timeline timelineInventory = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(inventoryButton.textFillProperty(), toColor)),
                    new KeyFrame(Duration.millis(400), new KeyValue(inventoryButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeFridge = new FadeTransition(Duration.millis(400), fridgeWhite);
            fadeFridge.setFromValue(1);
            fadeFridge.setToValue(0);
            // play the animations and fade
            timelineAnalytics.play();
            timelineInventory.play();

            fadeFridge.play();
            fadeHat.play();
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
                    new KeyFrame(Duration.millis(350), new KeyValue(menuButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeCutlery = new FadeTransition(Duration.millis(350), cutleryWhite);
            fadeCutlery.setFromValue(1);
            fadeCutlery.setToValue(0);
            // play the animations and fade
            timelineAnalytics.play();
            timelineMenu.play();

            fadeCutlery.play();
            fadeHat.play();
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
                    new KeyFrame(Duration.millis(300), new KeyValue(ordersButton.textFillProperty(), fromColor))
            );
            // fade of image
            FadeTransition fadeChicken = new FadeTransition(Duration.millis(300), chickenWhite);
            fadeChicken.setFromValue(1);
            fadeChicken.setToValue(0);
            // play the animations and fade
            timelineAnalytics.play();
            timelineOrders.play();

            fadeChicken.play();
            fadeHat.play();
            // switch to orders
            try { switchToOrders(); }
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
    private void switchToWeeklyChartView() throws IOException 
    {
        App.setRoot("analyticsPage_WeeklyChartView", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToIncomeView() throws IOException 
    {
        App.setRoot("analyticsPage_IncomeView", App.MAIN_WIDTH, App.MAIN_HEIGHT);
    }

    @FXML
    private void switchToInventory() throws IOException 
    {
        playAnimation("inventoryPage", 400, -300);
    }

    @FXML
    private void switchToMenu() throws IOException 
    {
        playAnimation("menuPage", 350, -200);
    }

    @FXML
    private void switchToOrders() throws IOException 
    {
        playAnimation("ordersPage", 300, -100);
    }

    @FXML
    private void signOut() throws IOException 
    {
        App.setRoot("openingAnimation", App.WIDTH, App.HEIGHT);
    }
}
