package com.bmn.bootstrap.listener.event;

import com.bmn.bootstrap.context.ApplicationContext;
import com.bmn.bootstrap.listener.ApplicationEvent;

/**
 * @author 562655151@qq.com
 * @date 2019/5/21
 */
public class ContextClosedEvent extends ApplicationEvent {

    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
