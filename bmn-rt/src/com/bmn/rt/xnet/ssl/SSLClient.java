package com.bmn.rt.xnet.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Administrator on 2017/5/26.
 */
public class SSLClient {
    public static void main(String[] args) {
        SSLClient client = new SSLClient();
        client.init();
        client.start();
    }

    private static class ClientTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private SSLSocket sslSocket;

    public void init() {
        try {

            //自己的身份
            KeyStore kk = KeyStore.getInstance(KeyStore.getDefaultType());
            kk.load(new FileInputStream("e:/keytool/clientkey.keystore"), "123123".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(kk, "123123".toCharArray());


            //相信的对方身份
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream("e:/keytool/sslsocket.keystore"), "123123".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            SSLContext ctx = SSLContext.getInstance("SSL");
            //ctx.init(null, new TrustManager[]{new ClientTrustManager()}, null);     //不验证服务器
            //ctx.init(null, tmf.getTrustManagers(), null);   //单向验证，只验证服务器
            //ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);   //双向验证
            ctx.init(kmf.getKeyManagers(), new TrustManager[] {new ClientTrustManager()}, null);    //只验证客户端

            sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket("127.0.0.1", 8088);



            System.out.println("client start");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if(sslSocket == null) {
            return;
        }

        try {
            InputStream is = sslSocket.getInputStream();
            OutputStream out = sslSocket.getOutputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bout = new BufferedOutputStream(out);

            bout.write("client message".getBytes());
            bout.flush();

            byte[] buffer = new byte[20];
            bis.read(buffer);

            System.out.println(new String(buffer));

            sslSocket.close();

            System.out.println("client stop");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
