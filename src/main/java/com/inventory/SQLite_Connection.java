package com.inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite_Connection {
    public static void main(String[] args) 
    {
        String url = "jdbc:sqlite:src/main/database/Restaurant.db";

        try(Connection conn = DriverManager.getConnection(url))
        {
            if (conn != null) System.out.println("Connected to database!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}