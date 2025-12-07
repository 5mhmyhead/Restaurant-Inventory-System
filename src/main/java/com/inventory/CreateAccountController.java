package com.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

public class CreateAccountController implements Initializable
{   
    // text and password fields
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private PasswordField managerPasswordField;
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

    // navigation Methods
    @FXML
    private void switchToLoginPage() throws IOException 
    {
        App.setRoot("transitionFromCreateToLogin", App.WIDTH, App.HEIGHT);
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
    private void createAccount() throws SQLException 
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
            if (!Session.getManagerPassword().equals(managerPassword)) 
            {
                errorMessage.setText("Invalid Manager Password. Account cannot be created.");
                playAnimation();
                return;
            }

            account_type = "manager";
        } 
        else 
        {
            // worker view is active
            account_type = "worker";
        }

        String checkSql = "SELECT * FROM Account WHERE username = ?";
        String insertSql = "INSERT INTO Account (username, password, email, account_type) VALUES(?,?,?,?)";

        // checks database if username already exists
        try (Connection conn = connect();
        PreparedStatement check = conn.prepareStatement(checkSql))
        {
            check.setString(1, username);
            try (ResultSet rs = check.executeQuery())
            {
                if (username.isEmpty()) 
                {
                    errorMessage.setText("Please enter all the required fields.");
                    playAnimation();
                    return;
                }

                if (rs.next() && rs.getInt(1) > 0)
                {
                    errorMessage.setText("Username already exists!");
                    playAnimation();
                    conn.close();
                    return;
                }
            }

            conn.close();
        }

        // checks if input is empty
        if (username.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            errorMessage.setText("Please enter all the required fields.");
            playAnimation();
        }
        else if(!email.contains("@") || !email.contains(".")) 
        {
            errorMessage.setText("Invalid email format!");
            playAnimation();
        }
        else
        {
            // inserts new user's data to database
            try (Connection conn = connect();
            PreparedStatement insert = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);) 
            {
                insert.setString(1, username);
                insert.setString(2, password);
                insert.setString(3, email);
                insert.setString(4, account_type);
                insert.executeUpdate();

                try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) 
                {
                    if (rs.next()) 
                    {
                        int newUserId = rs.getInt(1);
                        System.out.println("Account created: " + username + " (" + account_type + "), user_id=" + newUserId);
                    }
                }

                try (ResultSet rs = insert.getGeneratedKeys()) 
                {
                    if (rs.next()) 
                    {
                        int newUserId = rs.getInt(1);
                        System.out.println("Account created: " + username + " (" + account_type + "), user_id = " + newUserId);
                    }
                }
                
                conn.close();
                switchToLoginPage(); // go back to login after creation
            } 
            catch (SQLException | IOException e) 
            {
                e.printStackTrace();
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
