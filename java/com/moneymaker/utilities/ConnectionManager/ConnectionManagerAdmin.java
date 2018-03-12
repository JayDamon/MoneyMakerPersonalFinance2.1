package com.moneymaker.utilities.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Jay Damon on 2/3/2016.
 */
public class ConnectionManagerAdmin {
    private static ConnectionManagerAdmin instance = null;

    private static final String USERNAME = "appadministrator";
    private static final String PASSWORD = "moneymakeradministrator";
    private static final String CONN_STRING =
            "jdbc:mysql://localhost/";

    private Connection conn = null;

    private ConnectionManagerAdmin() {
    }

    public static ConnectionManagerAdmin getInstance() {
        if(instance == null) {
            instance = new ConnectionManagerAdmin();
        }
        return instance;
    }

    private boolean openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection()
    {
        if (conn == null) {
            if (openConnection()) {
                return conn;
            } else {
                return null;
            }
        }
        return conn;
    }

    public void close() {
        try {
            conn.close();
            conn = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
