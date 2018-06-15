package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/16.
 */
public class QvpPipe extends QvpZObject {
    interface IQvpPipeEvents {
        void readActivated(QvpPipe pipe);

        void writeActivated(QvpPipe pipe);

        //短暂的停顿
        void hiccuped(QvpPipe pipe);

        void pipeTerminated(QvpPipe pipe);
    }

    private QvpYPipe<QvpMsg> inpipe;
    private QvpYPipe<QvpMsg> outpipe;

    private boolean inActive;
    private boolean outActive;

    private int hwm;
    private int lwm;

    private long msgsRead;
    private long msgsWritten;

    private long peersMsgsRead;

    private QvpPipe peer;

    private IQvpPipeEvents sink;

    enum State {
        ACTIVE,
        DELIMITED,
        PENDING,
        TERMINATING,
        TERMINATED,
        DOUBLE_TERMINATED
    }

    private State state;

    private boolean delay;
    private QvpBlob identity;
    private QvpZObject parent;


    private QvpPipe(QvpZObject parent, QvpYPipe<QvpMsg> inpipe, QvpYPipe<QvpMsg> outpipe,
                    int inhwm, int outhwm, boolean delay) {
        super(parent);
        this.inpipe = inpipe;
        this.outpipe = outpipe;
        inActive = true;
        outActive = true;
        hwm = outhwm;
        lwm = computeLwm(inhwm);
        msgsRead = 0;
        msgsWritten = 0;
        peersMsgsRead = 0;
        peer = null;
        sink = null;
        state = State.ACTIVE;
        this.delay = delay;
        this.parent = parent;
    }

    private int computeLwm(int hwm) {
        return (hwm > QvpConfig.MAX_WM_DELTA.getValue() * 2) ? hwm - QvpConfig.MAX_WM_DELTA.getValue() : (hwm + 1) /2;
    }

    public static void pipepair(QvpZObject[] parents, QvpPipe[] pipes, int[] hwms, boolean[] delays) {
        QvpYPipe<QvpMsg> upipe1 = new QvpYPipe<>(QvpConfig.MESSAGE_PIPE_GRANULARITY.getValue());
        QvpYPipe<QvpMsg> upipe2 = new QvpYPipe<>(QvpConfig.MESSAGE_PIPE_GRANULARITY.getValue());

        pipes[0] = new QvpPipe(parents[0], upipe1, upipe2, hwms[1], hwms[0], delays[0]);
        pipes[1] = new QvpPipe(parents[1], upipe2, upipe1, hwms[0], hwms[1], delays[1]);

        pipes[0].setPeer(pipes[1]);
        pipes[1].setPeer(pipes[0]);
    }

    private void setPeer(QvpPipe pipe) {
        this.peer = pipe;
    }


    public void setEventSink(IQvpPipeEvents sink) {
        this.sink = sink;
    }

    public void setIdentity(QvpBlob identity) {
        this.identity = identity;
    }

    public QvpBlob getIdentity() {
        return identity;
    }

    public boolean checkRead() {
        if (!inActive || (state != State.ACTIVE && state != State.PENDING)) {
            return false;
        }

        if (!inpipe.checkRead()) {
            inActive = false;
            return false;
        }

        if (isDelimiter(inpipe.probe())) {
            QvpMsg msg = inpipe.read();
            delimit();
            return false;
        }
        return true;
    }

    private void delimit() {
        if (state == State.ACTIVE) {
            state = State.DELIMITED;
            return;
        }

        if (state == State.PENDING) {
            outpipe = null;
            sendPipeTermAck(peer);
            state = State.TERMINATED;
            return;
        }
    }


    private boolean isDelimiter(QvpMsg msg) {
        return msg.isDelimiter();
    }

    public QvpMsg read() {
        if (!inActive || (state != State.ACTIVE && state != State.PENDING)) {
            return null;
        }

        QvpMsg msg = inpipe.read();
        if(msg == null) {
            inActive = false;
            return null;
        }

        if (msg.isDelimiter()) {
            delimit();
            return null;
        }

        if (!msg.hasMore()) {
            msgsRead++;
        }

        if(lwm > 0 && msgsRead % lwm == 0) {
            sendActivateWrite(peer, msgsRead);
        }
        return msg;
    }

    public boolean checkWrite() {
        if (!outActive || state != State.ACTIVE) {
            return false;
        }

        boolean full = hwm > 0 && msgsWritten - peersMsgsRead == (long) (hwm);
        if (full) {
            outActive = false;
            return false;
        }
        return true;
    }

    public boolean write(QvpMsg msg) {
        if (!checkWrite()) {
            return false;
        }
        boolean more = msg.hasMore();
        if (!more) {
            msgsWritten++;
        }

        return true;
    }

}
