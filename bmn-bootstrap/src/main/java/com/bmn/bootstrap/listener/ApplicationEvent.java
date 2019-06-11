package com.bmn.bootstrap.listener;

import java.util.EventObject;

/**
 *
 * 事件接口
 *
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class ApplicationEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
