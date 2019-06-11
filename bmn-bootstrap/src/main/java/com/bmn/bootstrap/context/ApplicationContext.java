package com.bmn.bootstrap.context;

import com.bmn.bootstrap.listener.ApplicationEventPublisher;
import com.bmn.bootstrap.listener.ApplicationListener;

/**
 * 服务器上下文对象
 *
 * 目的是管理服务器中所有资源。及服务器的生命周期
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public interface ApplicationContext extends ApplicationEventPublisher {

    /**
     * 刷新
     */
    void refresh();

    void close();

    boolean isActive();

    void addApplicationListener(ApplicationListener<?> listener);


}
