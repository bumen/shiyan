package com.bmn.bootstrap.config;

import com.alibaba.fastjson.JSONObject;
import com.bmn.bootstrap.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * 服务器端配置
 *
 * @author 562655151@qq.com
 * @date 2019/5/21
 */
public final class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static final ApplicationConfig INSTANCE = new ApplicationConfig();

    private static final String JVM_CONFIG_PARAM_KEY = "server.config";
    private static final String DEFAULT_CONFIG_NAME = "application.yaml";

    private ApplicationConfig() {
    }

    public static final ApplicationConfig getInstance() {
        return INSTANCE;
    }

    /**
     * 缓存加载过的配置，也可以自定义配置
     */
    private final Map<String, Object> source = new ConcurrentHashMap<>();
    /**
     * 配置文件
     */
    private volatile JSONObject configFile = new JSONObject();

    private volatile String fileMd5 = "";

    private final Yaml yaml = new Yaml();


    /**
     * 加载配置
     */
    public synchronized void load() throws IOException {
        String configPath = getProperty(JVM_CONFIG_PARAM_KEY);
        if (StringUtils.isEmpty(configPath)) {
            String userIdr = getProperty("user.dir");
            configPath = String.format("%s/config/%s", userIdr, DEFAULT_CONFIG_NAME);
        }
        Path path = Paths.get(configPath);
        if (Files.isDirectory(path)) {
            path = path.resolve(DEFAULT_CONFIG_NAME);
        }

        if (!Files.exists(path)) {
            throw new FileNotFoundException("config file path not exist");
        }

        InputStream stream = Files.newInputStream(path);
        String md5Hex = DigestUtils.md5Hex(stream);
        stream.close();
        if (md5Hex.equals(fileMd5)) {
            logger.info("config not change do not need reload");
            return;
        }

        fileMd5 = md5Hex;

        byte[] data = Files.readAllBytes(path);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        JSONObject obj = yaml.loadAs(in, JSONObject.class);
        in.close();
        if (obj == null) {
            throw new FileNotFoundException("config file error");
        }

        String profile = obj.getString("profile");

        if (StringUtils.isEmpty(profile)) {
            throw new IllegalArgumentException("config file profile not exist");
        }

        JSONObject config = obj.getJSONObject(profile);
        if (config == null) {
            throw new IllegalArgumentException("config file profile:{} not exist");
        }

        configFile = config;

        source.clear();

        logger.info("server runtime config:{} success", configFile.toJSONString());
    }

    /**
     * 获取yaml配置文件中的配置
     * <p>如：dev或dev.lua</p>
     *
     * 如果类型不匹配则抛出异常
     */
    public <T> T getConfig(String key, Class<T> clazz) throws ClassCastException {
        Object cacheValue = getProperty(key);
        if (cacheValue != null) {
            return (T) cacheValue;
        }

        T result = getFileConfig(key, clazz);
        if (result == null) {
            return null;
        }

        source.put(key, result);

        return result;
    }

    public String getStringProperty(String key) {
        return getConfig(key, String.class);
    }

    public int getIntProperty(String key) {
        return getConfig(key, int.class);
    }

    public boolean getBooleanProperty(String key) {
        return getConfig(key, boolean.class);
    }

    /**
     * 获取属性
     */
    public <T> T getProperty(String key) {
        Object obj = source.get(key);
        if (obj != null) {
            return (T) obj;
        }

        obj = System.getProperties().getProperty(key);
        if (obj != null) {
            return (T) obj;
        }

        return null;
    }

    /**
     * 属性是否存在
     */
    public boolean containsProperty(String key) {
        Object obj = getProperty(key);
        if (obj != null) {
            return true;
        }

        obj = getFileConfig(key, Object.class);

        return obj == null ? false : true;
    }


    /***
     * 获取自定义文件中的配置
     * @param key
     * @param clazz
     * @param <T>
     * @return
     * @throws ClassCastException
     */
    public <T> T getFileConfig(String key, Class<T> clazz) throws ClassCastException {
        T result = null;
        JSONObject config = configFile;

        String[] keys = StringUtils.tokenizeToStringArray(key, ".");
        for (int i = 0, l = keys.length; i < l; i++) {
            String k = keys[i];

            if (i == l - 1) {
                result = config.getObject(k, clazz);
            } else {
                config = config.getJSONObject(k);
            }

            if (config == null) {
                return null;
            }
        }
        return result;
    }


    @Override
    public String toString() {
        return "ApplicationConfig{" +
            "configFile=" + configFile.toJSONString() +
            '}';
    }
}
