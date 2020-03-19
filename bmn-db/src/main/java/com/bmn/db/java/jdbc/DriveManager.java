package com.bmn.db.java.jdbc;

/**
 * sun 提供的驱动类
 */
public class DriveManager {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        connection = connection;
    }
}
