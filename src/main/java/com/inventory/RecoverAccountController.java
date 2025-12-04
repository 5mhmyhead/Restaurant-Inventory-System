    package com.inventory;

<<<<<<< HEAD
    import java.io.IOException;
    import java.sql.*;
    import javafx.animation.FadeTransition;
    import javafx.animation.PauseTransition;
    import javafx.animation.SequentialTransition;
    import javafx.fxml.FXML;
    import javafx.scene.control.Label;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.util.Duration;

    public class RecoverAccountController {

        @FXML private TextField emailField;
        @FXML private PasswordField passField;
        @FXML private PasswordField confirmField;
        @FXML private Label errorMes;

        @FXML
        private void switchToLoginPage() throws IOException {
            App.setRoot("loginPage", App.WIDTH, App.HEIGHT);
        }

        @FXML
        private void resetPassword() throws IOException {
            String email = emailField.getText().trim();
            String pass  = passField.getText().trim();
            String check = confirmField.getText().trim();

            // ---- VALIDATIONS ----
            if (email.isEmpty() || pass.isEmpty() || check.isEmpty()) {
                showError("Fields are empty!");
                return;
            }
            if (!pass.equals(check)) {
                showError("Password does not match!");
                return;
            }
            if (!email.contains("@") || !email.contains(".email")) {
                showError("Invalid Email format!");
                return;
            }

            // ---- CHECK IF EMAIL EXISTS ----
            try (Connection conn = SQLite_Connection.connect();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT 1 FROM Account WHERE email = ?")) {
=======
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
>>>>>>> 7900e07468e9dcdecac39e1fc0ef6a67f8f0f9c8

                stmt.setString(1, email);

<<<<<<< HEAD
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        updatePassword(email, pass);
                        System.out.println("Successfully updated password!");
                        App.setRoot("loginPage", App.WIDTH, App.HEIGHT);
                    } else {
                        showError("Email not found");
                    }
=======
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
>>>>>>> 7900e07468e9dcdecac39e1fc0ef6a67f8f0f9c8
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showError("Database error.");
            }
<<<<<<< HEAD
        }

        // ---- UPDATE PASSWORD ----
        private void updatePassword(String email, String newPass) {
            try (Connection conn = SQLite_Connection.connect();
                PreparedStatement prep = conn.prepareStatement(
                        "UPDATE Account SET password = ? WHERE email = ?")) {

                prep.setString(1, newPass);  
                prep.setString(2, email);

                int rows = prep.executeUpdate();
                if (rows > 0) {
                    System.out.println("Password updated for: " + email);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showError("Database error during update.");
            }
        }

        // ---- ERROR MESSAGE HELPER ----
        private void showError(String message) {
            errorMes.setText(message);

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            FadeTransition fade = new FadeTransition(Duration.seconds(2), errorMes);
            fade.setFromValue(1);
            fade.setToValue(0);

            SequentialTransition transition = new SequentialTransition(errorMes, delay, fade);
            transition.jumpTo(Duration.ZERO);
            transition.stop();
            transition.play();
        }
    }
=======
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
>>>>>>> 7900e07468e9dcdecac39e1fc0ef6a67f8f0f9c8
