package com.bmn.db.java.jdbc;

public class MysqlDriver {

    static {
        DriveManager.setConnection(new MysqlConnection());
    }
}
