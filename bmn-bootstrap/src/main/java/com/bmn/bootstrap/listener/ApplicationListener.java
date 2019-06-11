package com.bmn.bootstrap.listener;

import java.util.EventListener;

/**
 *
 * 服务器事件监听器
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * Handle an application event.
     * @param event the event to respond to
     */
    void onApplicationEvent(E event);

    boolean supportsEventType(ApplicationEvent eventType);

}