package com.moneymaker.utilities.ConnectionManager;

import com.moneymaker.main.UsernameData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerUser {
    private static UsernameData usernameData = new UsernameData();
    private static ConnectionManagerUser instance = null;
    private static final String USERNAME = usernameData.getSessionUsername();
    private static final String PASSWORD = usernameData.getSessionPassword();
    private static final String CONN_STRING =
            "jdbc:mysql://localhost/" + UsernameData.getUserSchema();

    private boolean closeActive = false;

    private Connection conn = null;

    private ConnectionManagerUser() {
    }

    public static ConnectionManagerUser getInstance() {
        if(instance == null) {
            instance = new ConnectionManagerUser();
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
        if (closeActive) {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    This suspends the close() method that a connection to a database can not be closed.  In order to close the connection, you must first run the activateClose() method.
    This functionality can be helpful if you know that a number of operations that will interact with the database will be run and want to ensure that the connection remains open the whole time.
    This can be dangerous if you do not reactivate the close operation since it will prevent classes that call the close operation from closing the connection.
     */
    public void suspendClose() {
        closeActive = false;
    }

    public void activateClose() {
        closeActive = true;
    }

}
