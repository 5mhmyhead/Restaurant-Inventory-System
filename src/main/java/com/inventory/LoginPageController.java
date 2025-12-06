package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPageController implements Initializable
{
    @FXML private AnchorPane loginPane;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessage;

    // animations for the error message
    PauseTransition delay;
    FadeTransition fade;
    SequentialTransition transition;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // the error message waits for 2 seconds
        delay = new PauseTransition(Duration.seconds(3));
        // then it fades out
        fade = new FadeTransition(Duration.seconds(2), errorMessage);
        fade.setFromValue(1);
        fade.setToValue(0);
        // the fade plays after the delay
        transition = new SequentialTransition(errorMessage, delay, fade);
    }

    @FXML
    private void switchToTitlePage() throws IOException 
    {
        App.setRoot("transitionFromLoginToTitle", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToCreateAccount() throws IOException 
    {
        App.setRoot("transitionFromLoginToCreate", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToRecoverAccount() throws IOException 
    {
        App.setRoot("transitionFromLoginToRecover", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToInventory(String user) throws IOException 
    {
        FadeTransition fade = new FadeTransition();
        fade.setDelay(Duration.millis(300));
        fade.setDuration(Duration.millis(700));
        fade.setNode(loginPane);
        fade.setFromValue(1);
        fade.setToValue(0);

        fade.play();
        fade.setOnFinished(event -> {
            try 
            {
                App.setRoot("inventoryPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void Login() throws IOException 
    {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty())
        {
            errorMessage.setText("Username or password is empty!");
            playAnimation();
        }
        else
        {
            try (Connection conn = SQLite_Connection.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Account WHERE username = ? AND password = ?")) 
            {
                stmt.setString(1, user);
                stmt.setString(2, pass);

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        errorMessage.setText("Login successful!");
                        playAnimation();
                        switchToInventory(user);
                    }
                    else 
                    {
                        errorMessage.setText("Invalid username or password.");
                        playAnimation();
                    }
                }

                conn.close();
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    // error message animation helper
    private void playAnimation() 
    {
        transition.jumpTo(Duration.ZERO);
        transition.stop();
        transition.play();
    }
}