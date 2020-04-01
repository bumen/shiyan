package com.bmn.db.java.jdbc;

public class OracleConnection implements Connection
{

    @Override
    public boolean executeUpdate(String sql) {
        return false;
    }
}
