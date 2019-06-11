package com.bmn.bootstrap.listener.event;

import com.bmn.bootstrap.listener.ApplicationEvent;

/**
 * @author 562655151@qq.com
 * @date 2019/5/21
 */
public class PayloadApplicationEvent<T> extends ApplicationEvent {

    private final T payload;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PayloadApplicationEvent(Object source, T payload) {
        super(source);

        this.payload = payload;
    }

    public T getPayload() {
        return this.payload;
    }

}
