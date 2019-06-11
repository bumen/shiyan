package com.bmn.bootstrap.listener;

/**
 * 事件发布者接口
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
@FunctionalInterface
public interface ApplicationEventPublisher {

    default void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

    void publishEvent(Object event);
}
