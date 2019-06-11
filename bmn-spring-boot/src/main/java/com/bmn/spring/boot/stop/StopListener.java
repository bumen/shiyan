package com.bmn.spring.boot.stop;

import org.springframework.boot.actuate.cassandra.CassandraHealthIndicator;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/18
 */
public class StopListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

        CassandraHealthIndicator indicator;
    }
}
