package com.bmn.bootstrap.suport;

import com.bmn.bootstrap.util.ClassUtils;
import com.bmn.bootstrap.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/18
 */
public class PathMatchingResourcePatternResolver {

    private static final Logger logger = LogManager
        .getLogger(PathMatchingResourcePatternResolver.class);

    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    private PathMatcher pathMatcher = new AntPathMatcher();

    private ClassLoader classLoader;

    public PathMatchingResourcePatternResolver() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PathMatchingResourcePatternResolver( ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * 获取所有需要加载的类路径
     */
    public Path[] getResources(String locationPattern) throws IOException {
        // 类路径
        if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            // 通配符类型
            if (getPathMatcher()
                .isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
                return findPathMatchingResources(locationPattern);
            } else {
                // 目录或指定文件
                return findAllClassPathResources(
                    locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            }
        } else {
            // 文件系统路径
            String newLocationPattern = StringUtils
                .replace(locationPattern, File.separator, "/");
            int prefixEnd = newLocationPattern.indexOf(':') + 1;
            if (getPathMatcher().isPattern(newLocationPattern.substring(prefixEnd))) {
                // a file pattern
                return findPathMatchingResources(newLocationPattern);
            }

            // 目录或指定文件
            return new Path[]{Paths.get(locationPattern)};
        }
    }

    protected Path[] findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Set<Path> result = doFindAllClassPathResources(path);

        logger.trace("Resolved classpath location [{}] to resources ", result);
        return result.toArray(new Path[0]);
    }

    protected Set<Path> doFindAllClassPathResources(String path) throws IOException {
        Set<Path> result = new LinkedHashSet<>(16);
        ClassLoader cl = getClassLoader();
        Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(path)
            : ClassLoader.getSystemResources(path));
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            try {
                result.add(Paths.get(url.toURI()));
            } catch (URISyntaxException e) {
                logger.warn("find all class path:{} error.", url, e);
            }
        }

        if ("".equals(path)) {
            logger.warn("Resolved all path is empty, not support");
        }
        return result;
    }

    /**
     * 通配符类型查找
     * 确定目录中与子目录中查询所有匹配文件
     * @param locationPattern
     * @return
     * @throws IOException
     */
    protected Path[] findPathMatchingResources(String locationPattern) throws IOException {
        String rootDirPath = determineRootDir(locationPattern);
        String subPattern = locationPattern.substring(rootDirPath.length());
        Path[] rootDirResources = getResources(rootDirPath);
        Set<Path> result = new LinkedHashSet<>(16);
        for (Path rootDirResource : rootDirResources) {
            result.addAll(doFindPathMatchingFileResources(rootDirResource, subPattern));
        }

        logger.trace("Resolved location pattern [{}] to resources ", result);
        return result.toArray(new Path[0]);
    }

    /**
     * 使用绝对路径查找
     * @param rootDirResource
     * @param subPattern
     * @return
     * @throws IOException
     */
    protected Set<Path> doFindPathMatchingFileResources(Path rootDirResource, String subPattern)
        throws IOException {

        Path rootDir;
        try {
            rootDir = rootDirResource.toAbsolutePath();
        } catch (Exception ex) {
            logger.info("Failed to resolve {} in the file system", rootDirResource.toString(), ex);
            return Collections.emptySet();
        }
        return doFindMatchingFileSystemResources(rootDir, subPattern);
    }

    protected Set<Path> doFindMatchingFileSystemResources(Path rootDir, String subPattern)
        throws IOException {
        logger.trace("Looking for matching resources in directory tree [{}]", rootDir.toString());
        Set<Path> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
        return matchingFiles;
    }

    protected Set<Path> retrieveMatchingFiles(Path rootDir, String pattern) throws IOException {
        if (!Files.exists(rootDir)) {
            logger.debug("Skipping [{}] because it does not exist", rootDir.toString());
            return Collections.emptySet();
        }
        if (!Files.isDirectory(rootDir)) {
            logger.info("Skipping [{}] because it does not denote a directory", rootDir.toString());
            return Collections.emptySet();
        }
        if (!Files.isReadable(rootDir)) {
            logger.info(
                "Skipping search for matching files underneath directory [{}] because the application is not allowed to read the directory",
                rootDir.toString());
            return Collections.emptySet();
        }
        String fullPattern = StringUtils
            .replace(rootDir.toAbsolutePath().toString(), File.separator, "/");
        if (!pattern.startsWith("/")) {
            fullPattern += "/";
        }
        fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
        Set<Path> result = new LinkedHashSet<>(8);
        doRetrieveMatchingFiles(fullPattern, rootDir, result);
        return result;
    }

    /**
     * 如果有子目录，递归向下查找
     * @param fullPattern
     * @param dir
     * @param result
     * @throws IOException
     */
    protected void doRetrieveMatchingFiles(String fullPattern, Path dir, Set<Path> result)
        throws IOException {
        logger.trace("Searching directory [{}] for files matching pattern [{}]", dir.toString(),
            fullPattern);
        Files.list(dir).forEach(content -> {
            String currPath = StringUtils
                .replace(content.toAbsolutePath().toString(), File.separator, "/");
            if (Files.isDirectory(content) && getPathMatcher()
                .matchStart(fullPattern, currPath + "/")) {
                if (!Files.isReadable(content)) {
                    logger.debug(
                        "Skipping subdirectory [{}] because the application is not allowed to read the directory",
                        content);
                } else {
                    try {
                        doRetrieveMatchingFiles(fullPattern, content, result);
                    } catch (IOException e) {
                        logger.warn("subdirectory [{}] matching files error", content, e);
                    }
                }
            }
            if (getPathMatcher().match(fullPattern, currPath)) {
                result.add(content);
            }
        });
    }

    protected String determineRootDir(String location) {
        int prefixEnd = location.indexOf(':') + 1;
        int rootDirEnd = location.length();
        while (rootDirEnd > prefixEnd && getPathMatcher()
            .isPattern(location.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return location.substring(0, rootDirEnd);
    }

    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader());
    }


}
