package com.bmn.bean;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class EventListenerBean {

    private List<EventListener> events = new ArrayList<>();

    public void addEventListener(RtBeanEventListener listener) {
        this.events.add(listener);
    }

    public void removeEventListener(RtBeanEventListener listener) {
        this.events.remove(listener);
    }

    public EventListener[] getEventListeners() {
        return this.events.toArray(new EventListener[0]);
    }

}
