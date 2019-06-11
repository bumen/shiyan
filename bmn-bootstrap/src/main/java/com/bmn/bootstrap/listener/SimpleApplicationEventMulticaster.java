package com.bmn.bootstrap.listener;

import com.bmn.bootstrap.exception.ErrorHandler;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {

    private ErrorHandler errorHandler;

    public final Set<ApplicationListener<?>> applicationListeners = new CopyOnWriteArraySet<>();

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        this.applicationListeners.add(listener);
    }


    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        this.applicationListeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        this.applicationListeners.clear();
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        Set<ApplicationListener<?>> applicationListeners = this.applicationListeners;
        for (ApplicationListener listener : applicationListeners) {
            invokeListener(listener, event);
        }
    }

    protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
        ErrorHandler errorHandler = this.errorHandler;
        if (errorHandler != null) {
            try {
                doInvokeListener(listener, event);
            } catch (Throwable err) {
                errorHandler.handleError(err);
            }
        } else {
            doInvokeListener(listener, event);
        }
    }

    private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
        if (listener.supportsEventType(event)) {
            listener.onApplicationEvent(event);
        }
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
