package com.bmn.spring.core.io;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

public class Test {
    public static void main(String[] args) throws IOException {
        fileSystemResource();
        classPathResource();

        urlResource();
    }

    /**
     * 获取进程外文件系统资源
     * @throws IOException
     */
    private static void fileSystemResource() throws IOException {
        String path = "E:\\project\\bmn\\shiyan\\bmn-web-spring\\config\\application.properties";

        FileSystemResource resource = new FileSystemResource(path);

        Properties p = new Properties();
        p.load(resource.getInputStream());


        System.out.println(p.getProperty("user.name"));
        System.out.println(p.getProperty("user.email"));
    }

    private static void classPathResource() throws IOException {
        String path = "spring/app.xml";

        ClassPathResource resource = new ClassPathResource(path);

        URL url = resource.getURL();

        URI uri = resource.getURI();

    }

    private static void urlResource() throws IOException {
        String path = "http://www.baidu.com";

        UrlResource resource = new UrlResource(path);


        URI uri = resource.getURI();

        URL url = resource.getURL();

        File file = resource.getFile();

    }
}
