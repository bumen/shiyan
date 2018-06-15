package com.bmn.http.httpclient;

import org.apache.http.ExceptionLogger;

/**
 * Created by Administrator on 2017/4/13.
 */
public class ErrorLogger implements ExceptionLogger {
    @Override
    public void log(Exception ex) {
        ex.printStackTrace();
    }
}
