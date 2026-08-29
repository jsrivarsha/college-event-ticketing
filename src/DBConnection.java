package com.college;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:mysql://bpwepkqni7chxjohprxq-mysql.services.clever-cloud.com:3306/bpwepkqni7chxjohprxq?useSSL=true&requireSSL=true&verifyServerCertificate=false";
        
    private static final String USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "bpwepkqni7chxjohprxq";
        
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to Clever Cloud MySQL Database.");
            e.printStackTrace();
        }
        return conn;
    }
}