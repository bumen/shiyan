package com.bmn.bootstrap.listener.event;

import com.bmn.bootstrap.BmnApplication;
import com.bmn.bootstrap.context.ApplicationContext;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/20
 */
public class ApplicationReadyEvent extends SpursApplicationEvent {

    private final ApplicationContext context;


    public ApplicationReadyEvent(BmnApplication application,
        String[] args, ApplicationContext context) {
        super(application, args);
        this.context = context;
    }

    public ApplicationContext getApplicationContext() {
        return this.context;
    }

}
