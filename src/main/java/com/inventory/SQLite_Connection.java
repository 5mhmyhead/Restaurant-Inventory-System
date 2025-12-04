package com.inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite_Connection {
    private static final String url = "jdbc:sqlite:src/main/database/Restaurant.db";

    public static Connection connect() throws SQLException 
    {
        return DriverManager.getConnection(url);
    }

    public static void main(String[] args) 
    {
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