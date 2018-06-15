package com.bmn.socket.zmq.src;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2017/6/16.
 */
public abstract class QvpOwn extends QvpZObject {

    protected final QvpOptions options;

    private boolean terminating;

    private final AtomicLong sendSeqnum;

    private long processedSeqnum;

    private QvpOwn owner;

    private final Set<QvpOwn> owned;

    private int termAcks;



   protected QvpOwn(QvpCtx ctx, int tid) {
        super(ctx, tid);

        terminating = false;
        sendSeqnum = new AtomicLong(0);
        processedSeqnum = 0;
        owner = null;
        termAcks = 0;
        options = new QvpOptions();
        owned = new HashSet<>();
    }


    protected QvpOwn(QvpIoThread ioThread, QvpOptions options) {
        super(ioThread);

        terminating = false;
        sendSeqnum = new AtomicLong(0);
        processedSeqnum = 0;
        owner = null;
        termAcks = 0;
        this.options = options;
        owned = new HashSet<>();
    }

    public abstract void destroy();

    protected void processDestroy() {
        destroy();
    }

    private void setOwner(QvpOwn owner) {
        this.owner = owner;
    }

    public void incSeqnum() {
        sendSeqnum.incrementAndGet();
    }

    protected void processSeqnum() {
        processedSeqnum++;
        checkTermAcks();
    }

    protected void launchChild(QvpOwn object) {
        object.setOwner(this);      //设置父亲
        sendPlug(object);
        sendOwn(this, object);  //把object加上owned里，由processOwn处理
    }

    protected void processOwn(QvpOwn object) {
        if (terminating) {
            registerTermAcks(1);
            sendTerm(object, 0);
            return;
        }

        owned.add(object);
    }

    protected void termChild(QvpOwn object) {
        processTermReq(object);
    }



    protected void terminate() {
        if(terminating) {
            return;
        }

        if (owner == null) {
            processTerm(options.linger);
            return;
        }

        sendTermReq(owner, this);   //向owner发送，自己要停止命令，写processTermReq相对应
    }

    protected void processTermReq(QvpOwn object) {
        if(terminating) {
            return;
        }

        if(!owned.contains(object)) {
            return;
        }

        owned.remove(object);
        registerTermAcks(1);
        sendTerm(object, options.linger);

    }

    protected void processTerm(int linger) {
        for(QvpOwn own : owned) {
            sendTerm(own, linger);
        }

        registerTermAcks(owned.size());
        owned.clear();
        terminating = true;
        checkTermAcks();
    }

    protected boolean isTerminating() {
        return this.terminating;
    }

    public void registerTermAcks(int count) {
        termAcks+= count;
    }

    public void unregisterTermAck() {
        termAcks--;
        checkTermAcks();
    }

    protected void processTermAck() {
        unregisterTermAck();
    }

    private void checkTermAcks() {
        if(terminating && processedSeqnum == sendSeqnum.get() && termAcks == 0) {
            if (owner != null) {
                sendTermAck(owner);
            }
            processDestroy();
        }
    }


}
