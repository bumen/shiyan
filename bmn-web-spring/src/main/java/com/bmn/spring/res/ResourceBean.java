package com.bmn.spring.res;


import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by Administrator on 2016/12/21.
 */
public class ResourceBean {

    InputStreamSource inputStreamSource = null;
    Resource resource = null;
    ResourceLoader resourceLoader = null;
    DefaultResourceLoader defaultResourceLoader = null;


    public static void main(String[] args) {

        String path = "file:\\E:/IdeaProjects/crabapple/out/production/mybatis/com/qvp/classloader/MyClassLoader.class";
        String pathToUse = StringUtils.cleanPath(path);

        ResourcePatternResolver wwwwwwwwwwwwwresourcePatternResolver = null;

        String paths = ClassUtils.classPackageAsResourcePath(ResourceBean.class);
        System.out.println(paths);
        System.out.println(pathToUse);
    }
}
