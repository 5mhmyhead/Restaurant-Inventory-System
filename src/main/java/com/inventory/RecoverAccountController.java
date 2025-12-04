package com.inventory;

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
    @FXML private Label errMessage;

    @FXML
    private void switchToLoginPage() throws IOException {
        App.setRoot("loginPage", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void resetPassword() throws IOException {

        String email = emailField.getText().trim();
        String pass = passField.getText().trim();
        String check = confirmField.getText().trim();

        // Basic validation
        if (email.isEmpty() || pass.isEmpty() || check.isEmpty()) {
            showError("Please fill out all fields.");
            return;
        }

        if (!pass.equals(check)) {
            showError("Passwords do not match.");
            return;
        }

        try (Connection conn = SQLite_Connection.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM Account WHERE email = ?")) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    // Email exists — update password
                    updatePassword(email, pass);
                    System.out.println("Successfully updated password!");
                    App.setRoot("loginPage", App.MAIN_WIDTH, App.MAIN_HEIGHT);
                } else {
                    showError("Email not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage());
        }
    }

    // ---------------------------------------
    // Update Password
    // ---------------------------------------
    private void updatePassword(String email, String newPass) {
        try (Connection conn2 = SQLite_Connection.connect();
             PreparedStatement prep = conn2.prepareStatement(
                     "UPDATE Account SET password = ? WHERE email = ?")) {

            prep.setString(1, newPass);
            prep.setString(2, email);

            int rows = prep.executeUpdate();   // <<--- FIXED

            if (rows > 0) {
                System.out.println("Password updated for: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------
    // Error label animation
    // ---------------------------------------
    private void showError(String message) {
        errMessage.setOpacity(1);
        errMessage.setText(message);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        FadeTransition fade = new FadeTransition(Duration.seconds(1), errMessage);
        fade.setFromValue(1);
        fade.setToValue(0);

        new SequentialTransition(delay, fade).play();
    }
}
