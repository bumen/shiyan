package com.bmn.bean;

import java.util.EventObject;

public class RtBeanEventObject extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RtBeanEventObject(Object source) {
        super(source);
    }
}
