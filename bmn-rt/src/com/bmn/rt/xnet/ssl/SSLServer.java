package com.bmn.rt.xnet.ssl;

import javax.net.ssl.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Administrator on 2017/5/26.
 */
public class SSLServer {

    public static void main(String[] args) {
        SSLServer sslServer = new SSLServer();
        sslServer.init();
        sslServer.start();
    }

    private SSLServerSocket serverSocket;

    public void init() {
        try {

            System.out.println("key store default: " + KeyStore.getDefaultType());
            System.out.println("key manager factory default: " + KeyManagerFactory.getDefaultAlgorithm());
            System.out.println("trust manager factory default: " + TrustManagerFactory.getDefaultAlgorithm());

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream("e:/keytool/sslsocket.keystore"), "123123".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "123123".toCharArray());


            /**
             * 也可以创建一个server单独的trustKeyStore，通过import多个client证书导入到一个trustKeyStore。从而可以验证多个client
             */
            KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
            tks.load(new FileInputStream("e:/keytool/clientkey.keystore"), "123123".toCharArray());     //直接导入client，只验证一个client

            /**
             * 用于双向验证
             */
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(tks);


            SSLContext ctx = SSLContext.getInstance("TLSv1");
            ctx.init(kmf.getKeyManagers(), null, null);     //单向验证
            //ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);   //双向验证（单向也可验证）

            System.out.println("ssl context default : " + ctx.getProtocol());


            serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(8088);

            //serverSocket.setNeedClientAuth(true);   //强制双向验证（不能单向验证）


            System.out.println("server start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if(serverSocket == null) {
            return;
        }

        while(true) {
            try {
                System.out.println("server listening...");
                SSLSocket socket = (SSLSocket) serverSocket.accept();

                socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
                    @Override
                    public void handshakeCompleted(HandshakeCompletedEvent event) {
                        try {
                            X509Certificate cert = (X509Certificate) event.getPeerCertificates()[0];
                            String peer = cert.getSubjectDN().getName();
                            System.out.println(" request from " + peer);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                BufferedInputStream bis = new BufferedInputStream(in);
                BufferedOutputStream bos = new BufferedOutputStream(out);

                byte[] buffer = new byte[20];

                bis.read(buffer);

                System.out.println(new String(buffer));

                bos.write("server reply ".getBytes());
                bos.flush();

                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class ServerTrustManager implements X509TrustManager {

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
}
