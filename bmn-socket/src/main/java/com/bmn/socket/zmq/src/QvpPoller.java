package com.bmn.socket.zmq.src;


import java.io.IOException;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/6/14.
 */
public class QvpPoller extends QvpPollerBase implements Runnable {

    private static class PollSet {
        protected IQvpPollEvents handler;
        protected SelectionKey key;
        protected int ops;
        protected boolean cancelled;

        protected PollSet(IQvpPollEvents handler) {
            this.handler = handler;
            key = null;
            cancelled = false;
            ops = 0;
        }
    }

    private final Map<SelectableChannel, PollSet> fdTable;
    private final AtomicBoolean retired = new AtomicBoolean(false);
    private volatile boolean stopping;
    private volatile boolean stopped;

    private Thread worker;
    private Selector selector;
    private final String name;

    public QvpPoller() {
        this("poller");
    }

    public QvpPoller(String name) {
        this.name = name;
        stopping = false;
        stopped = false;
        fdTable = new HashMap<>();
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        if(!stopped) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void addHandle(SelectableChannel fd, IQvpPollEvents handle) {
        fdTable.put(fd, new PollSet(handle));
        adjustLoad(1);
    }

    public final void removeHandle(SelectableChannel fd) {
        fdTable.get(fd).cancelled = true;
        retired.set(true);
        adjustLoad(-1);
    }
    public final void setPollIn(SelectableChannel handle)
    {
        register(handle, SelectionKey.OP_READ, false);
    }

    public final void resetPollOn(SelectableChannel handle)
    {
        register(handle, SelectionKey.OP_READ, true);
    }

    public final void setPollOut(SelectableChannel handle)
    {
        register(handle,  SelectionKey.OP_WRITE, false);
    }

    public final void resetPollOut(SelectableChannel handle)
    {
        register(handle, SelectionKey.OP_WRITE, true);
    }

    public final void setPollConnect(SelectableChannel handle)
    {
        register(handle, SelectionKey.OP_CONNECT, false);
    }

    public final void setPollAccept(SelectableChannel handle)
    {
        register(handle, SelectionKey.OP_ACCEPT, false);
    }
    private final void register(SelectableChannel fd, int ops, boolean negate) {
        PollSet pollSet  = fdTable.get(fd);
        if(negate) {
            pollSet.ops  = pollSet.ops & ~ops;
        } else {
            pollSet.ops = pollSet.ops | ops;
        }

        if(pollSet.key != null){
            pollSet.key.interestOps(pollSet.ops);
        } else {
            retired.set(true);
        }
    }

    public void start() {
        worker = new Thread(this, name);
        worker.setDaemon(true);
        worker.start();
    }

    public void stop() {
        stopping = true;
        selector.wakeup();
    }



    @Override
    public void run() {
        int returnsImmediately = 0;
        while(!stopping) {
            long timeout = executeTimes();

            while(retired.compareAndSet(true, false)) {
                Iterator<Map.Entry<SelectableChannel, PollSet>> it = fdTable.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<SelectableChannel, PollSet> entry = it.next();
                    SelectableChannel ch = entry.getKey();
                    PollSet pollSet = entry.getValue();
                    if(pollSet.key == null) {
                        try {
                            pollSet.key = ch.register(selector, pollSet.ops, pollSet.handler);
                        } catch (ClosedChannelException e) {
                            e.printStackTrace();
                        }
                    }

                    if(pollSet.cancelled || !ch.isOpen()) {
                        if(pollSet.key != null) {
                            pollSet.key.cancel();
                        }
                        it.remove();
                    }
                }
            }

            int rc = 0;
            long start = System.currentTimeMillis();
            try {
                rc = selector.select(timeout);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(rc == 0) {
                if(timeout == 0 || System.currentTimeMillis() - start < timeout / 2) {
                    returnsImmediately++;
                } else {
                    returnsImmediately = 0;
                }

                if(returnsImmediately > 10) {
                    rebuildSelector();
                    returnsImmediately = 0;
                }
                continue;
            }

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()) {
                SelectionKey key = it.next();
                IQvpPollEvents evt = (IQvpPollEvents) key.attachment();
                it.remove();

                try {
                    if(key.isReadable()) {
                        evt.inEvent();
                    } else if(key.isWritable()) {
                        evt.outEvent();
                    } else if(key.isAcceptable()) {
                        evt.acceptEvent();
                    } else if(key.isConnectable()) {
                        evt.connectEvent();
                    }
                } catch (CancelledKeyException e) {

                }

            }
        }
        stopped = true;
    }

    private void rebuildSelector() {
        Selector newSelector = null;

        try {
            newSelector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        selector = newSelector;

        for (PollSet pollSet : fdTable.values()) {
            pollSet.key = null;
        }
        retired.set(true);
    }
}
