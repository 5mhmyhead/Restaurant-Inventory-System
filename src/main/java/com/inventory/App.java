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
        scene = new Scene(loadFXML("titlePage"), WIDTH, HEIGHT);

        stage.setTitle("Kawaii Count");
        stage.setResizable(false);
        
        Image icon = new Image(getClass().getResourceAsStream("/com/inventory/images/helloKittyIcon.png"));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    static void setRoot(String fxml, int width, int height) throws IOException 
    { 
        Stage stage = (Stage) scene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();

        // If this is the inventory page, inject DB connection
        if ("inventoryPage".equals(fxml)) 
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
        stage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException 
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) 
    { 
        launch(args); 
    }
}