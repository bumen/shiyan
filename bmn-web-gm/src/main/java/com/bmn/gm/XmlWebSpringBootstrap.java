package com.bmn.gm;

import com.bmn.gm.AbstractSpringBootstrap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by Administrator on 2017/7/11.
 */
public class XmlWebSpringBootstrap extends AbstractSpringBootstrap {

    public XmlWebSpringBootstrap() {
        this("xmlWebSpringBootstrap");
    }

    public XmlWebSpringBootstrap(String contextId) {
        super(contextId);
    }
    @Override
    protected void doBootstrap() {
        String[] defaultConfig = getClassPathConfigLocations();

        context = new XmlWebApplicationContext();

        ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)context;

        if (ObjectUtils.identityToString(cwac).equals(cwac.getId())) {
            if (this.contextId != null) {
                cwac.setId(this.contextId);
            }
            else {
                cwac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + "servletContextName" +
                        "." + "servletName");

            }
        }

        cwac.setConfigLocations(defaultConfig);
        cwac.refresh();
    }
}
