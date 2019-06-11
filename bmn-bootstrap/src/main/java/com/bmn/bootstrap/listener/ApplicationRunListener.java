package com.bmn.bootstrap.listener;

import com.bmn.bootstrap.context.ApplicationContext;

/**
 *
 * 监听服务器启动事件监听器
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public interface ApplicationRunListener {

    void starting();

    void contextPrepared(ApplicationContext context);

    void contextLoaded(ApplicationContext context);

    void started(ApplicationContext context);

    void running(ApplicationContext context);

    void failed(ApplicationContext context, Throwable exception);
}
