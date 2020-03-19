package com.bmn.db.java.jdbc;

/**
 * oracle驱动
 */
public class OracleDriver {

    static {
        DriveManager.setConnection(new OracleConnection());
    }

}
