package com.bmn.bootstrap.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public abstract class FileUtils {

    /**
     * 加载资源文件
     * @param resource
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(Path resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource);
        return props;
    }

    public static void fillProperties(Properties props, Path resource) throws IOException {
        InputStream is = Files.newInputStream(resource);
        try {
            props.load(is);
        } finally {
            is.close();
        }
    }
}
