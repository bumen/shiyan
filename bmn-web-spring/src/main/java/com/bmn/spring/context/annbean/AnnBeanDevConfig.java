package com.bmn.spring.context.annbean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zyq
 * @date: 2018/11/27
 */
@Configuration
public class AnnBeanDevConfig {

    @Bean
    public Assignment assignment() {
        System.out.println("create new assignment");
        return new Assignment();
    }
}
