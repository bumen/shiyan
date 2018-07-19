package com.crypto;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public  abstract class HttpsCoder {

    public static final String PROTOCAL = "TLS";

    public static final String KEYSTORE_TYPE = "PKCS12";

    private static KeyStore getKeyStore(String keyStorePath, String passwd) throws KeyStoreException {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);

        try (FileInputStream is = new FileInputStream(keyStorePath)) {
            ks.load(is, passwd.toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ks;
    }

    private static SSLSocketFactory getSSLSocketFactory(String passwd, String keyStorePath, String trustStorePath)
        throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        KeyStore ks = getKeyStore(keyStorePath, passwd);

        keyManagerFactory.init(ks, passwd.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        KeyStore trustStore = getKeyStore(trustStorePath, passwd);

        trustManagerFactory.init(trustStore);

        SSLContext ctx = SSLContext.getInstance(PROTOCAL);

        ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return ctx.getSocketFactory();
    }

    public static void configSSLSocketFactory(HttpsURLConnection conn, String passwd, String keyStorePath,
        String trustStorePath)
        throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(passwd, keyStorePath, trustStorePath);

        conn.setSSLSocketFactory(sslSocketFactory);
    }
}
