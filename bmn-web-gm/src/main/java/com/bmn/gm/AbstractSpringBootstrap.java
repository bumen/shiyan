package com.bmn.gm;

import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class AbstractSpringBootstrap {

    protected XmlWebApplicationContext context;

    public AbstractSpringBootstrap(String contextId) {
    }

    protected String contextId;

    protected abstract void doBootstrap();

    public String[] getClassPathConfigLocations() {
        return null;
    }
}
