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

                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        updatePassword(email, pass);
                        System.out.println("Successfully updated password!");
                        App.setRoot("loginPage", App.WIDTH, App.HEIGHT);
                    } else {
                        showError("Email not found");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showError("Database error.");
            }
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
