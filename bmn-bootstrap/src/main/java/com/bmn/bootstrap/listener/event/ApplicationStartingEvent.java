package com.bmn.bootstrap.listener.event;

import com.bmn.bootstrap.BmnApplication;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class ApplicationStartingEvent extends SpursApplicationEvent {

    public ApplicationStartingEvent(BmnApplication application,
        String[] args) {
        super(application, args);
    }
}
