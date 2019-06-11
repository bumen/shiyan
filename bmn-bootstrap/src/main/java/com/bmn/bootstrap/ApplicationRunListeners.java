package com.bmn.bootstrap;

import com.bmn.bootstrap.context.ApplicationContext;
import com.bmn.bootstrap.listener.ApplicationRunListener;
import com.bmn.bootstrap.util.ReflectionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class ApplicationRunListeners {
    private final List<ApplicationRunListener> listeners;


    ApplicationRunListeners(Collection<? extends ApplicationRunListener> listeners) {
        this.listeners = new ArrayList<>(listeners);
    }

    public void starting() {
        for (ApplicationRunListener listener : this.listeners) {
            listener.starting();
        }
    }

    public void contextPrepared(ApplicationContext context) {
        for (ApplicationRunListener listener : this.listeners) {
            listener.contextPrepared(context);
        }
    }

    public void contextLoaded(ApplicationContext context) {
        for (ApplicationRunListener listener : this.listeners) {
            listener.contextLoaded(context);
        }
    }

    public void started(ApplicationContext context) {
        for (ApplicationRunListener listener : this.listeners) {
            listener.started(context);
        }
    }

    public void running(ApplicationContext context) {
        for (ApplicationRunListener listener : this.listeners) {
            listener.running(context);
        }
    }

    public void failed(ApplicationContext context, Throwable exception) {
        for (ApplicationRunListener listener : this.listeners) {
            callFailedListener(listener, context, exception);
        }
    }

    private void callFailedListener(ApplicationRunListener listener,
        ApplicationContext context, Throwable exception) {
        try {
            listener.failed(context, exception);
        }
        catch (Throwable ex) {
            if (exception == null) {
                ReflectionUtils.rethrowRuntimeException(ex);
            }
        }
    }
}
