package com.bmn.rt.lang.proxy;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/5/11.
 */
public class ProxyTest {
    public static void main(String[] args) {

        RealImpl real = new RealImpl();

        RealHandler handler = new RealHandler(real);

        Object proxy = Proxy.newProxyInstance(real.getClass().getClassLoader(), real.getClass().getInterfaces(), handler);



        Real realProxy = (Real)proxy;
        realProxy.name();

        byte[] data = ProxyGenerator.generateProxyClass("realDataProxy", real.getClass().getInterfaces());

        File file = new File(".");

        try {
            FileOutputStream ost = new FileOutputStream(file.getCanonicalPath() + "\\target\\classes\\com\\qvp\\proxy\\realDataProxy.class");
            ost.write(data);
            ost.flush();
            ost.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
