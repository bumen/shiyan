package com.bmn.spring.boot.config;

import java.io.IOException;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/18
 */
public class PathMatchingResource {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";


    static ResourcePatternResolver resolver;

    public static void main(String[] args) throws IOException {
        String basePackage = "com.bmn.spring.boot.controller";
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
            resolveBasePackage(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;

        resolver = new PathMatchingResourcePatternResolver();


        Resource[] resources = resolver.getResources(packageSearchPath);

        for(Resource resource : resources) {
        }

    }

    protected static String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }
}
