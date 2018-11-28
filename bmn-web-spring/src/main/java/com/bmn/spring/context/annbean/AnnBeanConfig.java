package com.bmn.spring.context.annbean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;

/**
 * @author: zyq
 * @date: 2018/11/27
 */
@Configuration
@Profile("ab")
@Import(AnnBeanDevConfig.class)
public class AnnBeanConfig {

    @Bean
    @Role(BeanDefinition.ROLE_APPLICATION)
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy(false)
    @DependsOn("module")
    public Course course() {
        System.out.println("create new course");
        return new Course();
    }

    @Bean
    public Module module() {
        System.out.println("create new module");
        return new Module();
    }
}
