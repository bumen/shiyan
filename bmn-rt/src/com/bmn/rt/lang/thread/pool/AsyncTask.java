package com.bmn.rt.lang.thread.pool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/12/1.
 */
public class AsyncTask implements Runnable{
    public static final String IO = "io";      //io 密集型
    public static final String CPU = "cpu";  //cpu 密集型

    private final String type;

    public AsyncTask(String type) {
        this.type = type;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String getURL = "http://baidu.com";
            URL getUrl = new URL(getURL);

            if(type.equals(IO)) {
                 connection = (HttpURLConnection) getUrl.openConnection();
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    // empty loop
                }
            }
        }

        catch (IOException e) {
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch(Exception e) {
                }
            }
            if(connection != null)
                connection.disconnect();
        }

    }

}
