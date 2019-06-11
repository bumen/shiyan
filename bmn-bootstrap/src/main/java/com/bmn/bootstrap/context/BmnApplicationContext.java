package com.bmn.bootstrap.context;

import com.bmn.bootstrap.config.ApplicationConfig;
import com.bmn.bootstrap.listener.ApplicationEvent;
import com.bmn.bootstrap.listener.ApplicationEventMulticaster;
import com.bmn.bootstrap.listener.ApplicationListener;
import com.bmn.bootstrap.listener.SimpleApplicationEventMulticaster;
import com.bmn.bootstrap.listener.event.ContextClosedEvent;
import com.bmn.bootstrap.listener.event.ContextRefreshedEvent;
import com.bmn.bootstrap.listener.event.PayloadApplicationEvent;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class BmnApplicationContext implements ApplicationContext {
    private final Logger logger = LoggerFactory.getLogger(BmnApplicationContext.class);

    private ApplicationEventMulticaster initialMulticaster = new SimpleApplicationEventMulticaster();
    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();
    private Thread shutdownHook;
    private final Object startupShutdownMonitor = new Object();

    @Override
    public void refresh() {
        logger.info("refresh application context start");
        this.closed.set(false);
        this.active.set(true);

        try {
            logger.info("refresh application context spurs config start");
            ApplicationConfig.getInstance().load();
            logger.info("refresh application context spurs config finished");
        } catch (IOException e) {
            logger.error("refresh spurs config error ", e);
        }

        publishEvent(new ContextRefreshedEvent(this));

        logger.info("refresh application context finished");
    }


    public void registerShutdownHook() {
        if (this.shutdownHook == null) {
            this.shutdownHook = new Thread() {
                @Override
                public void run() {
                    synchronized (startupShutdownMonitor) {
                        doClose();
                    }
                }
            };
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        }
    }

    @Override
    public void close() {
        synchronized (this.startupShutdownMonitor) {
            doClose();
            if (this.shutdownHook != null) {
                try {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                }
                catch (IllegalStateException ex) {
                    // ignore - VM is already shutting down
                }
            }
        }
    }

    protected void doClose() {
        if (this.active.get() && this.closed.compareAndSet(false, true)) {
            logger.debug("Closing BmnApplicationContext");

            try {
                publishEvent(new ContextClosedEvent(this));
            }
            catch (Throwable ex) {
                logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
            }

            this.active.set(false);
        }
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        initialMulticaster.addApplicationListener(listener);
    }

    @Override
    public void publishEvent(Object event) {
        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent) event;
        }
        else {
            applicationEvent = new PayloadApplicationEvent<>(this, event);
        }
        initialMulticaster.multicastEvent(applicationEvent);
    }
}
