package com.college;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://bpwepkqni7chxjohprxq-mysql.services.clever-cloud.com:3306/bpwepkqni7chxjohprxq?sslMode=REQUIRED";
    private static final String USER = "bpwepkqni7chxjohprxq";
    private static final String PASSWORD = "871m4VmoQGx29JKcG14j";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}