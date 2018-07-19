package com.crypto;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.HttpsURLConnection;

public class HttpsCoderTest {

    private String passwd = "123123";

    private String keyStorePath = "";
    private String trustStorePath = "";
    private String httpsUrl = "";

    public void test()
        throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        URL url = new URL(httpsUrl);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setDoInput(true);

        HttpsCoder.configSSLSocketFactory(conn, passwd, keyStorePath, trustStorePath);

        int len = conn.getContentLength();
        if (len != -1) {
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte[] data = new byte[len];
            dis.readFully(data);
            dis.close();
        }

        conn.disconnect();
    }

}
