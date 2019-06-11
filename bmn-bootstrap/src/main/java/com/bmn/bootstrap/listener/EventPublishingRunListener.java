package com.bmn.bootstrap.listener;

import com.bmn.bootstrap.BmnApplication;
import com.bmn.bootstrap.context.ApplicationContext;
import com.bmn.bootstrap.exception.ErrorHandler;
import com.bmn.bootstrap.listener.event.ApplicationContextInitializedEvent;
import com.bmn.bootstrap.listener.event.ApplicationFailedEvent;
import com.bmn.bootstrap.listener.event.ApplicationPreparedEvent;
import com.bmn.bootstrap.listener.event.ApplicationReadyEvent;
import com.bmn.bootstrap.listener.event.ApplicationStartedEvent;
import com.bmn.bootstrap.listener.event.ApplicationStartingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class EventPublishingRunListener implements ApplicationRunListener {

    private final BmnApplication application;

    private final String[] args;

    private final SimpleApplicationEventMulticaster initialMulticaster;

    public EventPublishingRunListener(BmnApplication application, String[] args) {
        this.application = application;
        this.args = args;
        this.initialMulticaster = new SimpleApplicationEventMulticaster();
        for (ApplicationListener<?> listener : application.getListeners()) {
            this.initialMulticaster.addApplicationListener(listener);
        }
    }

    @Override
    public void starting() {
        this.initialMulticaster.multicastEvent(
            new ApplicationStartingEvent(this.application, this.args));
    }

    @Override
    public void contextPrepared(ApplicationContext context) {
        this.initialMulticaster.multicastEvent(new ApplicationContextInitializedEvent(
            this.application, this.args, context));
    }

    @Override
    public void contextLoaded(ApplicationContext context) {
        for (ApplicationListener<?> listener : this.application.getListeners()) {
            context.addApplicationListener(listener);
        }
        this.initialMulticaster.multicastEvent(
            new ApplicationPreparedEvent(this.application, this.args, context));
    }

    @Override
    public void started(ApplicationContext context) {
        context.publishEvent(
            new ApplicationStartedEvent(this.application, this.args, context));
    }

    @Override
    public void running(ApplicationContext context) {
        context.publishEvent(
            new ApplicationReadyEvent(this.application, this.args, context));
    }

    @Override
    public void failed(ApplicationContext context, Throwable exception) {
        ApplicationFailedEvent event = new ApplicationFailedEvent(this.application,
            this.args, context, exception);
        if (context != null && context.isActive()) {
            context.publishEvent(event);
        }  else {
            this.initialMulticaster.setErrorHandler(new LoggingErrorHandler());
            this.initialMulticaster.multicastEvent(event);
        }
    }

    private static class LoggingErrorHandler implements ErrorHandler {
        private static Logger logger = LoggerFactory.getLogger(EventPublishingRunListener.class);

        @Override
        public void handleError(Throwable throwable) {
            logger.warn("Error calling ApplicationEventListener", throwable);
        }

    }
}
