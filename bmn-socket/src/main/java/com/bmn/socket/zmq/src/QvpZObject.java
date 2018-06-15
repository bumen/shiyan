package com.bmn.socket.zmq.src;

import zmq.SocketBase;

/**
 * Created by Administrator on 2017/6/14.
 */
public abstract class QvpZObject {
    private final QvpCtx ctx;
    private final int tid;

    protected QvpZObject(QvpCtx ctx, int tid) {
        this.ctx = ctx;
        this.tid = tid;
    }

    protected QvpZObject(QvpZObject parent) {
        this(parent.getCtx(), parent.getTid());
    }

    protected int getTid() {return this.tid;}

    protected QvpCtx getCtx() {return this.ctx;}

    protected void processCommand(QvpCommand command) {
        switch (command.type()) {
            case ACTIVATE_READ:
                processActivateRead();
                break;
            case ACTIVATE_WRITE:
                processActivateWrite();
                    break;
            case STOP:
                processStop();
                break;
            case PLUG:
                processPlug();
                processSeqnum();
                break;
            case OWN:
                processOwn((QvpOwn) command.arg);
                processSeqnum();
                break;
            case ATTACH:
                processAttach((IQvpEngine) command.arg);
                processSeqnum();
                break;
            case BIND:
                processBind((QvpPipe) command.arg);
                processSeqnum();
                break;
            case HICCUP:
                processHiccup(command.arg);
                break;
            case PIPE_TERM:
                processPipeTermAck();
                break;
            case TERM_REQ:
                processTermReq((QvpOwn) command.arg);
                break;
            case TERM:
                processTerm((Integer) command.arg);
                break;
            case TERM_ACK:
                processTermAck();
                break;
            case REAP:
                processReap((QvpSocketBase) command.arg);
                break;
            case REAPED:
                processReaped();
                break;
            default:
                throw new IllegalArgumentException();
                
        }
    }

    protected boolean registerEndpoint(String addr, QvpCtx.Endpoint endpoint) {
        return ctx.registerEndpoint(addr, endpoint);
    }

    protected void unregisterEndpoints(SocketBase socket)
    {
        ctx.unregisterEndpoints(socket);
    }

    protected QvpCtx.Endpoint findEndpoint(String addr)
    {
        return ctx.findEndpoint(addr);
    }

    protected void destroySocket(SocketBase socket)
    {
        ctx.destroySocket(socket);
    }

    //  Chooses least loaded I/O thread.
    protected QvpIoThread chooseIoThread(long affinity)
    {
        return ctx.chooseIoThread(affinity);
    }

    protected void sendStop() {
        QvpCommand cmd = new QvpCommand(this, QvpCommand.Type.STOP);
        ctx.sendCommand(tid, cmd);
    }

    protected void sendPlug(QvpOwn destination) {
        sendPlug(destination, true);
    }

    protected void sendPlug(QvpOwn destination, boolean incSeqnum) {
        if(incSeqnum) {
            destination.incSeqnum();
        }

        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.PLUG);
        sendCommand(cmd);
    }

    protected void sendOwn(QvpOwn destination, QvpOwn object) {
        destination.incSeqnum();
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.OWN, object);
        sendCommand(cmd);
    }

    protected void sendAttach(QvpSessionBase destination, IQvpEngine engine) {
        sendAttach(destination, engine, true);
    }

    protected void sendAttach(QvpSessionBase destination, IQvpEngine engine, boolean incSeqnum) {
        if (incSeqnum) {
            destination.incSeqnum();
        }

        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.ATTACH, engine);
        sendCommand(cmd);
    }

    protected void sendBind(QvpOwn destination, QvpPipe pipe) {
        sendBind(destination, pipe, true);
    }

    protected void sendBind(QvpOwn destination, QvpPipe pipe, boolean incSeqnum) {
        if (incSeqnum) {
            destination.incSeqnum();
        }
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.BIND, pipe);
        sendCommand(cmd);
    }

    protected void sendActivateRead(QvpPipe destination) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.ACTIVATE_READ);
        sendCommand(cmd);
    }

    protected void sendActivateWrite(QvpPipe destination, long msgsRead) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.ACTIVATE_WRITE, msgsRead);
        sendCommand(cmd);
    }

    protected void sendHiccup(QvpPipe destination, Object pipe) {

        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.HICCUP, pipe);
        sendCommand(cmd);
    }

    protected void sendPipeTerm(QvpPipe destination) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.PIPE_TERM);
        sendCommand(cmd);
    }

    protected void sendPipeTermAck(QvpPipe destination) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.PIPE_TERM_ACK);
        sendCommand(cmd);
    }

    protected void sendTermReq(QvpOwn destination, QvpOwn object) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.TERM_REQ, object);
        sendCommand(cmd);
    }

    protected void sendTerm(QvpOwn destination, int linger) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.TERM, linger);
        sendCommand(cmd);
    }

    protected void sendTermAck(QvpOwn destination) {
        QvpCommand cmd = new QvpCommand(destination, QvpCommand.Type.TERM_ACK);
        sendCommand(cmd);
    }

    protected void sendReap(QvpSocketBase socket) {
        QvpCommand cmd = new QvpCommand(ctx.getReaper(), QvpCommand.Type.REAP, socket);
        sendCommand(cmd);
    }

    protected void sendReaped() {
        QvpCommand cmd = new QvpCommand(ctx.getReaper(), QvpCommand.Type.REAPED);
        sendCommand(cmd);
    }

    protected void sendDone() {
        QvpCommand cmd = new QvpCommand(null, QvpCommand.Type.DONE);
        ctx.sendCommand(QvpCtx.TERM_TID, cmd);
    }





    protected void processStop()
    {
        throw new UnsupportedOperationException();
    }

    protected void processPlug()
    {
        throw new UnsupportedOperationException();
    }


    protected void processOwn(QvpOwn object)
    {
        throw new UnsupportedOperationException();
    }

    protected void processAttach(IQvpEngine engine)
    {
        throw new UnsupportedOperationException();
    }

    protected void processBind(QvpPipe pipe)
    {
        throw new UnsupportedOperationException();
    }

    private void processActivateRead() {
        throw new UnsupportedOperationException();
    }


    private void processActivateWrite() {
        throw new UnsupportedOperationException();
    }

    protected void processHiccup(Object hiccupPipe)
    {
        throw new UnsupportedOperationException();
    }

    protected void processPipeTerm()
    {
        throw new UnsupportedOperationException();
    }
    protected void processPipeTermAck()
    {
        throw new UnsupportedOperationException();
    }

    protected void processTermReq(QvpOwn object)
    {
        throw new UnsupportedOperationException();
    }

    protected void processTerm(int linger)
    {
        throw new UnsupportedOperationException();
    }
    protected void processTermAck()
    {
        throw new UnsupportedOperationException();
    }
    protected void processReap(QvpSocketBase socket)
    {
        throw new UnsupportedOperationException();
    }



    protected void processReaped()
    {
        throw new UnsupportedOperationException();
    }
    protected void processSeqnum() {  throw new UnsupportedOperationException(); }

    private void sendCommand(QvpCommand cmd) {ctx.sendCommand(cmd.destination().getTid(), cmd);}
}
