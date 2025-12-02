package com.inventory;

import java.io.IOException;

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
        stage.show();
    }

    static void setRoot(String fxml, int width, int height) throws IOException 
    { 
        Stage stage = (Stage) scene.getWindow();
        // sets a new scene with the specified height and width
        Parent root = loadFXML(fxml);
        scene = new Scene(root, width, height);
        
        stage.setScene(scene);
        stage.sizeToScene();
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