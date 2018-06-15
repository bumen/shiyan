package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/14.
 */
public class QvpCommand {

    private final QvpZObject destination;
    private Type type;

    public enum Type {
        STOP,
        PLUG,
        OWN,
        ATTACH,
        BIND,
        ACTIVATE_READ,
        ACTIVATE_WRITE,
        HICCUP,
        PIPE_TERM,
        PIPE_TERM_ACK,
        TERM_REQ,
        TERM,
        TERM_ACK,
        REAP,
        REAPED,
        DONE
    }

    Object arg;

    public QvpCommand(QvpZObject destination, Type type) {
        this(destination, type, null);
    }

    public QvpCommand(QvpZObject destination, Type type, Object arg) {
        this.destination = destination;
        this.type = type;
        this.arg = arg;
    }

    public QvpZObject destination()
    {
        return destination;
    }

    public Type type()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return "Cmd" + "[" + destination + ", " + destination.getTid() + ", " + type + (arg == null ? "" : ", " + arg) + "]";
    }
}
