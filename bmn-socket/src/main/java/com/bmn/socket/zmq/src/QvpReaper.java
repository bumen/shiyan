package com.bmn.socket.zmq.src;

import zmq.IPollEvents;
import zmq.Poller;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/6/13.
 */
public class QvpReaper extends QvpZObject implements IPollEvents, Closeable {

    private QvpMailboxG mailbox;

    private final SelectableChannel mailboxHandle;

    private final Poller poller;

    private int socketsReaping;

    private final AtomicBoolean terminating = new AtomicBoolean();

    private String name;

    public QvpReaper(QvpCtx ctx, int tid) {
        super(ctx, tid);
        this.socketsReaping = 0;
        name = "reaper-" + tid;
        poller = new Poller(name);
        mailbox = new QvpMailboxG(name);

        this.mailboxHandle = mailbox.getFd();
        poller.addHandle(mailboxHandle, this);
        poller.setPollIn(mailboxHandle);
    }

    public void start() {
        poller.start();
    }

    public void stop() {
        if (!terminating.get()) {
            sendStop();
        }
    }


    @Override
    public void inEvent() {
        while(true) {
            /*QvpCommand cmd = mailbox.recv(0);
            if(cmd == null) {
                break;
            }*/

        }
    }

    @Override
    public void outEvent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void connectEvent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptEvent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void timerEvent(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void processStop() {
        terminating.set(true);
        if(socketsReaping == 0) {
            finishTerminating();
        }
    }

    @Override
    protected void processReap(QvpSocketBase socket) {
        --socketsReaping;
        if (socketsReaping == 0 && terminating.get()) {
            finishTerminating();
        }
    }

    private void finishTerminating() {
        sendDone();
        poller.removeHandle(mailboxHandle);
        poller.stop();
    }

    @Override
    public void close() throws IOException {
        poller.destroy();
        mailbox.close();
    }

    public QvpMailboxG getMailbox() {
        return mailbox;
    }
}
