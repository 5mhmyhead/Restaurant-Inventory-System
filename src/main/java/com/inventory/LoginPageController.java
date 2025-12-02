package com.inventory;

import java.io.IOException;
import java.sql.*;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginPageController 
{
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessage;

    @FXML
    private void switchToTitlePage() throws IOException 
    {
        App.setRoot("titlePage", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToCreateAccount() throws IOException 
    {
        App.setRoot("createAccount_ManagerView", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToRecoverAccount() throws IOException 
    {
        App.setRoot("recoverAccount", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void Login() throws IOException 
    {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        try (Connection conn = SQLite_Connection.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Account WHERE username = ? AND password = ?")) 
            {
                stmt.setString(1, user);
                stmt.setString(2, pass);

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        System.out.println("Login successful!");
                        App.setRoot("inventoryPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
                    }
                    else 
                    {
                        errorMessage.setText("Invalid username or password.");
                        // the error message waits for 2 seconds
                        PauseTransition delay = new PauseTransition(Duration.seconds(3));
                        // then it fades out
                        FadeTransition fade = new FadeTransition(Duration.seconds(2), errorMessage);
                        fade.setFromValue(1);
                        fade.setToValue(0);
                        // the fade plays after the delay
                        SequentialTransition transition = new SequentialTransition(errorMessage, delay, fade);
                        transition.jumpTo(Duration.ZERO);
                        transition.stop();
                        transition.play();
                    }
                }
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
                System.out.println("Database error: " + e.getMessage());
            }
    }
}