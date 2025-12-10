package com.inventory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Session 
{
    // hardcoded manager provided password
    private static String managerProvidedPW = "vivii";
 
    // this function is to get the username/user type of the user from the login page to use for the main scenes
    private static String username;
    private static String userType;

    private static int user_id;

    // get the current date
    static LocalDate today = LocalDate.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    // the current date of the session
    private static String currentDate = today.format(formatter);

    public static String getCurrentDate() 
    {
        return currentDate;
    }

    public static void setUsername(String user) 
    {
        username = user;
    }

    public static void setUserId (int id)
    {
        user_id = id;
    }

    public static void setUserType(String type) 
    {
        userType = type;
    }

    public static void setManagerPassword(String pass) 
    {
        managerProvidedPW = pass;
    }

    public static Integer getUserId()
    {
        return user_id;
    }

    public static String getUsername() 
    {
        return username;
    }

    public static String getUserType() 
    {
        return userType;
    }

    public static String getManagerPassword() 
    {
        return managerProvidedPW;
    }
}
