package com.tribeglobal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
    //making the sql connection a class that has a method that returns a connection object that we can assign to a connection object
    //making the connection object static that means it remains the same throughout the program
    static Connection connection;
    public static Connection createDBConnection(){
        //connection strings
        String url = "jdbc:mysql://localhost:3306/bankuser";
        String username = "root";
        String password = "couture";
        try {
            //creating the connection
            connection = DriverManager.getConnection(url,username,password);
            Statement statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
        //returning it
        return connection;
    }
}
