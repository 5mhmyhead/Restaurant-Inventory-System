package com.inventory;

public class Session {
    // hardcoded manager provided password
    private static String managerProvidedPW = "vivii";
 
    // this function is to get the username/user type of the user from the login page to use for the main scenes
    private static String username;
    private static String userType;

    public static void setUsername(String user) 
    {
        username = user;
    }

    public static void setUserType(String type) 
    {
        userType = type;
    }

    public static void setManagerPassword(String pass) 
    {
        managerProvidedPW = pass;
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
