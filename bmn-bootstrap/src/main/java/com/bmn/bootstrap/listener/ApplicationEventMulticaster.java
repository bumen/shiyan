package com.bmn.bootstrap.listener;

/**
 *
 * 事件广播者接口
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void removeAllListeners();

    void multicastEvent(ApplicationEvent event);

}
