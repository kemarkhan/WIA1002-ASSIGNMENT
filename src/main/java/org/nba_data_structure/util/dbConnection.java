package org.nba_data_structure.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    public static Connection createConnection(){

        Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/nba?useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "admin";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Printing connection object : " + conn);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
