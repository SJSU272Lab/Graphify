package com.graphify.db.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Sushant on 21-11-2016.
 */
public class DaoUtil {

    public static final Connection getConnection(String url) {
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
