package com.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginPageController 
{
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void switchToTitlePage() throws IOException 
    {
        App.setRoot("titlePage");
    }

    @FXML
    private void switchToCreateAccount() throws IOException 
    {
        App.setRoot("createAccount_ManagerView");
    }

    @FXML
    private void switchToRecoverAccount() throws IOException 
    {
        App.setRoot("recoverAccount");
    }

    @FXML
    private void Login() throws IOException 
    {
        String user = usernameField.getText();
        String pw = passwordField.getText();

        try (Connection conn = SQLite_Connection.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Account WHERE username = ? AND password = ?")) 
            {
                stmt.setString(1, user);
                stmt.setString(2, pw);

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        System.out.println("Login successful!");
                        App.setRoot("inventoryPage");
                    }
                    else 
                    {
                        System.out.println("Invalid username or password");
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