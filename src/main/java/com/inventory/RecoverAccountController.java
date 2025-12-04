package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class RecoverAccountController implements Initializable
{
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmField;
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
    private void switchToLoginPage() throws IOException 
    {
        App.setRoot("loginPage", App.WIDTH, App.HEIGHT);
    }

    // database connection helper
    private Connection connect() throws SQLException 
    {
        String url = "jdbc:sqlite:src/main/database/Restaurant.db";
        return DriverManager.getConnection(url);
    }

    @FXML
    private void resetPassword() throws IOException
    {

        String email = emailField.getText().trim();
        String pass = passField.getText().trim();
        String check = confirmField.getText().trim();

        // basic validation
        if (email.isEmpty() || pass.isEmpty() || check.isEmpty()) 
        {
            errorMessage.setText("Please fill out all fields.");
            transition.jumpTo(Duration.ZERO);
            transition.stop();
            transition.play();
            return;
        }

        if (!pass.equals(check)) 
        {
            errorMessage.setText("Passwords do not match.");
            transition.jumpTo(Duration.ZERO);
            transition.stop();
            transition.play();
            return;
        }

        try (Connection conn = connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Account WHERE email = ?")) 
        {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    // Email exists — update password
                    updatePassword(conn, email, pass);
                    System.out.println("Successfully updated password!");
                    switchToLoginPage();
                } 
                else 
                {
                    errorMessage.setText("Email was not found.");
                    transition.jumpTo(Duration.ZERO);
                    transition.stop();
                    transition.play();
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    // ---------------------------------------
    // Update Password
    // ---------------------------------------
    private void updatePassword(Connection conn, String email, String newPass) throws SQLException{
        try (PreparedStatement prep = conn.prepareStatement(
                     "UPDATE Account SET password = ? WHERE email = ?")) {

            prep.setString(1, newPass);
            prep.setString(2, email);

            int rows = prep.executeUpdate();   // <<--- FIXED

            if (rows > 0) 
            {
                System.out.println("Password updated for: " + email);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
