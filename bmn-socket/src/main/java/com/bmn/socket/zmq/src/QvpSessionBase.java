package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/16.
 */
public class QvpSessionBase extends QvpOwn implements  QvpPipe.IQvpPipeEvents, IQvpPollEvents, IQvpMsgSink, IQvpMsgSource{
    private boolean connect;
    private QvpPipe pipe;

    protected QvpSessionBase(QvpCtx ctx, int tid) {
        super(ctx, tid);
    }

    @Override
    public int pushMsg(QvpMsg msg) {
        return 0;
    }

    @Override
    public QvpMsg pullMsg() {
        return null;
    }

    @Override
    public void readActivated(QvpPipe pipe) {

    }

    @Override
    public void writeActivated(QvpPipe pipe) {

    }

    @Override
    public void hiccuped(QvpPipe pipe) {

    }

    @Override
    public void pipeTerminated(QvpPipe pipe) {

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

    @Override
    public void destroy() {

    }
}
