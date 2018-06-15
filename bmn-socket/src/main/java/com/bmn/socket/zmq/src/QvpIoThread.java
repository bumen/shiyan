package com.bmn.socket.zmq.src;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;

/**
 * Created by Administrator on 2017/6/14.
 */
public class QvpIoThread extends QvpZObject implements IQvpPollEvents, Closeable {

    private final QvpMailboxG mailbox;
    private final SelectableChannel mailboxHandle;
    private final QvpPoller poller;
    final String name;

    public QvpIoThread(QvpCtx ctx, int tid) {
        super(ctx, tid);
        name = "qvp-iothread-" + tid;
        poller = new QvpPoller(name);
        mailbox = new QvpMailboxG(name);
        mailboxHandle = mailbox.getFd();
        poller.addHandle(mailboxHandle, this);
        poller.setPollIn(mailboxHandle);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void inEvent() {

    }

    @Override
    public void outEvent() {

    }

    @Override
    public void connectEvent() {

    }

    @Override
    public void acceptEvent() {

    }

    @Override
    public void timerEvent(int id) {

    }

    public QvpMailboxG getMailbox() {
        return this.mailbox;
    }

    public void start() {
        poller.start();
    }

    public void stop() {
    }

    public int getLoad() {
        return 0;
    }
}
