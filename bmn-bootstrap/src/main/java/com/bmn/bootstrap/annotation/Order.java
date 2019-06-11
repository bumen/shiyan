package com.bmn.bootstrap.annotation;

import com.bmn.bootstrap.order.Ordered;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 562655151@qq.com
 * @date 2019/5/17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Order {

    int value() default Ordered.LOWEST_PRECEDENCE;
}
