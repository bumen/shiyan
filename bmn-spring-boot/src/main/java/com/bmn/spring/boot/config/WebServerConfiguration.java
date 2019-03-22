package com.bmn.spring.boot.config;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zyq
 * @date: 2019/3/21
 *
 * tomcat 长连接时默认一个连接最大请求数为100。必免重复创建连接保证连接复用性修改为1000
 *
 */
@Configuration
public class WebServerConfiguration {

    @Bean
    public ServletWebServerFactory tomcatFactory()
    {
        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
        tomcatFactory.addConnectorCustomizers(connector -> {
            ((AbstractHttp11Protocol) connector.getProtocolHandler()).setMaxKeepAliveRequests(1000);
            ((AbstractHttp11Protocol) connector.getProtocolHandler()).setKeepAliveTimeout(300000);
        });
        return tomcatFactory;
    }

}
