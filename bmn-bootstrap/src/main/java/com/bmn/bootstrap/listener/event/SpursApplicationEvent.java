package com.bmn.bootstrap.listener.event;

import com.bmn.bootstrap.BmnApplication;
import com.bmn.bootstrap.listener.ApplicationEvent;

/**
 *
 * 服务器启动相关事件
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class SpursApplicationEvent extends ApplicationEvent {
    private final String[] args;

    public SpursApplicationEvent(BmnApplication application, String[] args) {
        super(application);
        this.args = args;
    }


    public BmnApplication getSpringApplication() {
        return (BmnApplication) getSource();
    }

    public final String[] getArgs() {
        return this.args;
    }
}
