package com.bmn.socket.zmq.src;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/6/14.
 */
public abstract class QvpPollerBase {
    private final AtomicInteger load;

    private final class TimerInfo {
        IQvpPollEvents sink;
        int id;
        public TimerInfo(IQvpPollEvents sink, int id) {
            this.sink = sink;
            this.id = id;
        }

        @Override
        public String toString()
        {
            return "TimerInfo [id=" + id + ", sink=" + sink + "]";
        }
    }

    private final Map<Long, TimerInfo> timers;

    private final Map<Long, TimerInfo> addingTimers;

    protected QvpPollerBase() {
        load = new AtomicInteger(0);
        timers = new QvpMultiMap<>();
        addingTimers = new QvpMultiMap<>();
    }

    public final int getLoad() {
        return load.get();
    }

    protected void adjustLoad(int amount) {
        load.addAndGet(amount);
    }

    public void addTimer(long timeout, IQvpPollEvents sink, int id) {
        long expiration = System.currentTimeMillis() + timeout;
        TimerInfo info = new TimerInfo(sink, id);
        addingTimers.put(expiration, info);
    }

    public void cancelTimer(IQvpPollEvents sink, int id) {
        if(!addingTimers.isEmpty()) {
            timers.putAll(addingTimers);
            addingTimers.clear();
        }

        Iterator<Map.Entry<Long, TimerInfo>> it = timers.entrySet().iterator();
        while(it.hasNext()) {
            TimerInfo info = it.next().getValue();
            if(info.sink == sink && info.id == id) {
                it.remove();
                return;
            }
        }
    }

    protected long executeTimes() {
        if(!addingTimers.isEmpty()) {
            timers.putAll(addingTimers);
            addingTimers.clear();
        }
        if(timers.isEmpty()) {
            return 0;
        }

        long current = System.currentTimeMillis();
        Iterator<Map.Entry<Long, TimerInfo>> it = timers.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Long, TimerInfo> o = it.next();
            if(o.getKey() > current) {
                return o.getKey() - current;
            }

            o.getValue().sink.timerEvent(o.getValue().id);
            it.remove();
        }

        if (!addingTimers.isEmpty()) {
            return executeTimes();
        }

        return 0L;
    }
}
