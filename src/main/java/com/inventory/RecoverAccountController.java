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
        App.setRoot("transitionFromRecoverToLogin", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void resetPassword() throws IOException 
    {
        String email = emailField.getText().trim();
        String pass  = passField.getText().trim();
        String check = confirmField.getText().trim();

        // validation
        if (email.isEmpty() || pass.isEmpty() || check.isEmpty()) 
        {
            errorMessage.setText("Please enter all fields.");
            playAnimation();
            return;
        }

        if (!pass.equals(check)) 
        {
            errorMessage.setText("The passwords do not match!");
            playAnimation();
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) 
        {
            errorMessage.setText("Invalid email format!");
            playAnimation();
            return;
        }

       //check if the email exists
        try (Connection conn = SQLite_Connection.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM Account WHERE email = ?")) 
        {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    updatePassword(conn, email, pass);
                    System.out.println("Successfully updated password!");
                    switchToLoginPage();
                } 
                else 
                {
                    errorMessage.setText("The email was not found.");
                    playAnimation();
                }

                conn.close();
            }
            catch (SQLException e) 
            {
                e.printStackTrace();
                errorMessage.setText("Failed to update password.");
                playAnimation();
                conn.close();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            errorMessage.setText("Database error.");
            playAnimation();
        }
    }

    // update password function
    private void updatePassword(Connection conn, String email, String newPass) throws SQLException
    {
        try (PreparedStatement prep = conn.prepareStatement("UPDATE Account SET password = ? WHERE email = ?")) 
        {
            prep.setString(1, newPass);  
            prep.setString(2, email);

            int rows = prep.executeUpdate();
            if (rows > 0) System.out.println("Password updated for: " + email);
            conn.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            errorMessage.setText("Database error during update.");
            playAnimation();
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
