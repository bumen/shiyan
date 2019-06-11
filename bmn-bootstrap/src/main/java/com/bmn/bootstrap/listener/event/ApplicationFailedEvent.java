package com.bmn.bootstrap.listener.event;

import com.bmn.bootstrap.BmnApplication;
import com.bmn.bootstrap.context.ApplicationContext;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class ApplicationFailedEvent extends SpursApplicationEvent {

    private final ApplicationContext context;

    private final Throwable exception;

    public ApplicationFailedEvent(BmnApplication application,
        String[] args, ApplicationContext context, Throwable exception) {
        super(application, args);
        this.context = context;
        this.exception = exception;
    }

    public ApplicationContext getApplicationContext() {
        return this.context;
    }

    public Throwable getException() {
        return this.exception;
    }
}
