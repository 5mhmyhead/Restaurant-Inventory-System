package com.inventory;

import java.io.IOException;
import java.sql.Connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application 
{
    // global variables that specify the width and height of the scene
    public static final int HEIGHT = 576;
    public static final int WIDTH = 768;
    // height and width for the main scene of the app
    public static final int MAIN_HEIGHT = 720;
    public static final int MAIN_WIDTH = 1280; 

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException 
    {
        // add this temporarily to load table if accessing without going through title page
        FXMLLoader loader = new FXMLLoader(App.class.getResource("inventoryPage.fxml"));
        Parent root = loader.load();

        InventoryPageController controller = loader.getController();
        try 
        {
            Connection conn = SQLite_Connection.connect();
            controller.setConnection(conn);
            controller.loadItems();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        scene = new Scene(root, MAIN_WIDTH, MAIN_HEIGHT);
        // add this temporarily to load table if accessing without going through title page
        // scene = new Scene(loadFXML("openingAnimation"), WIDTH, HEIGHT);

        stage.setTitle("Kawaii Count");
        stage.setResizable(false);
        
        Image icon = new Image(getClass().getResourceAsStream("/com/inventory/images/helloKittyIcon.png"));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void setRoot(String fxml, int width, int height) throws IOException 
    { 
        Stage stage = (Stage) scene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();

        // if this is the inventory page, inject DB connection
        if (fxml.equals("inventoryPage")) 
        {
            InventoryPageController controller = fxmlLoader.getController();
            try 
            {
                Connection conn = SQLite_Connection.connect();
                controller.setConnection(conn);
                controller.loadItems();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }

        scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.sizeToScene();
        // centers the window on screen depending on who called this function
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // this part looks for who called the setRoot function
        if (stackTrace.length > 2) 
        {
            StackTraceElement caller = stackTrace[2];
            String fileName = caller.getFileName();

            if(fileName.equals("InventoryPageController.java") ||
               fileName.equals("AnalyticsPageController.java") ||
               fileName.equals("OrdersPageController.java") ||
               fileName.equals("MenuPageController.java")) 
            {
                stage.centerOnScreen();
            }
        }

        if(fxml.equals("inventoryPage")) 
        {
            // center on stage if it switches to the inventory page
            stage.centerOnScreen();
        }
    }

    public static Parent loadFXML(String fxml) throws IOException 
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) 
    { 
        launch(args); 
    }
}