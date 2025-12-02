package com.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountController 
{   
    // hardcoded manager provided password
    private static final String managerProvidedPW = "vivii";

    // text and password fields
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private PasswordField managerPasswordField;

    // navigation Methods
    @FXML
    private void switchToLoginPage() throws IOException 
    {
        App.setRoot("loginPage", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToManagerView() throws IOException 
    {
        App.setRoot("createAccount_ManagerView", App.WIDTH, App.HEIGHT);
    }

    @FXML
    private void switchToWorkerView() throws IOException 
    {
        App.setRoot("createAccount_WorkerView", App.WIDTH, App.HEIGHT);
    }

    // database connection helper
    private Connection connect() throws SQLException 
    {
        String url = "jdbc:sqlite:src/main/database/Restaurant.db";
        return DriverManager.getConnection(url);
    }

    // create Account method
    @FXML
    private void createAccount() 
    {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        // determine account type based on which view is active
        String account_type;
        if (managerPasswordField != null && !managerPasswordField.getText().isEmpty()) 
        {
            // manager view is active
            String managerPassword = managerPasswordField.getText();

            // validate against hardcoded secret
            if (!managerProvidedPW.equals(managerPassword)) 
            {
                System.out.println("Invalid manager password! Account not created.");
                return;
            }

            account_type = "manager";
        } 
        else 
        {
            // worker view is active
            account_type = "worker";
        }

        // insert into database
        String sql = "INSERT INTO Account (username, password, email, account_type) VALUES(?,?,?,?)";

        try (Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) 
        {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, account_type);
            pstmt.executeUpdate();

            try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) 
            {
                if (rs.next()) 
                {
                    int newUserId = rs.getInt(1);
                    System.out.println("Account created: " + username + " (" + account_type + "), user_id=" + newUserId);
                }
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) 
            {
                if (rs.next()) 
                {
                    int newUserId = rs.getInt(1);
                    System.out.println("Account created: " + username + " (" + account_type + "), user_id=" + newUserId);
                }
            }

            switchToLoginPage(); // go back to login after creation
        } 
        catch (SQLException | IOException e) 
        {
            e.printStackTrace();
        }
    }
}
