package com.bmn.socket.zmq.src;

import zmq.Msg;
import zmq.MultiMap;
import zmq.ValueReference;
import zmq.ZError;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/19.
 */
public class QvpSocketBase extends QvpOwn implements QvpPipe.IQvpPipeEvents, IQvpPollEvents
{

    private final Map<String, QvpOwn> endpoints;

    private final Map<String, QvpPipe> inprocs;

    private int tag;

    private boolean ctxTerminated;

    private boolean destroyed;

    private final QvpMailboxG mailbox;

    private final List<QvpPipe> pipes;

    private QvpPoller poller;

    private SelectableChannel handle;

    private long lastTsc;

    private int ticks;

    private boolean rcvmore;

    private QvpSocketBase monitorSocket;

    private int monitorEvents;

    protected ValueReference<Integer> errno;

    protected QvpSocketBase(QvpCtx ctx, int tid, int sid) {
        super(ctx, tid);

        tag = 0xbaddecaf;
        ctxTerminated = false;
        destroyed = false;
        lastTsc = 0;
        ticks = 0;
        rcvmore = false;
        monitorSocket = null;
        monitorEvents = 0;

        options.socketId = sid;
        options.linger = ctx.get(QvpZmq.ZMQ_BLOCKY) != 0 ? -1 : 0;
        endpoints = new MultiMap<>();
        inprocs = new MultiMap<>();
        pipes = new ArrayList<>();

        mailbox = new QvpMailboxG("socket-" + sid);
        errno = new ValueReference<>(0);
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

    public boolean checkTag() {
        return tag == 0xbaddecaf;
    }

    public void close() {
        tag = 0xdeadbeef;

        //  Transfer the ownership of the socket from this application thread
        //  to the reaper thread which will take care of the rest of shutdown
        //  process.
        sendReap(this);
    }

    public void setSocketOpt(int option, Object optval) {
        if (ctxTerminated && option != zmq.ZMQ.ZMQ_LINGER) {
            throw new NullPointerException();
        }

        //  First, check whether specific socket type overloads the option.
        if (xsetsockopt(option, optval)) {
            return;
        }

        //  If the socket type doesn't support the option, pass it to
        //  the generic option parser.
        options.setSocketOpt(option, optval);
    }

    private boolean xsetsockopt(int option, Object optval) {
        return false;
    }

    public Object getsockoptx(int option) {
        if (ctxTerminated) {
            throw new ZError.CtxTerminatedException();
        }

        if (option == QvpZmq.ZMQ_RCVMORE) {
            return rcvmore ? 1 : 0;
        }

        if (option == QvpZmq.ZMQ_FD) {
            return mailbox.getFd();
        }

        if (option == QvpZmq.ZMQ_EVENTS) {
            boolean rc = processCommands(0, false);
            if (!rc && errno.get() == ZError.ETERM) {
                return -1;
            }
            assert (rc);
            int val = 0;
            if (hasOut()) {
                val |= QvpZmq.ZMQ_POLLOUT;
            }
            if (hasIn()) {
                val |= QvpZmq.ZMQ_POLLIN;
            }
            return val;
        }
        //  If the socket type doesn't support the option, pass it to
        //  the generic option parser.
        return options.getsockopt(option);
    }

    boolean hasIn()
    {
        return xhasIn();
    }

    boolean hasOut()
    {
        return xhasOut();
    }

    protected boolean xhasIn()
    {
        return false;
    }
    protected boolean xhasOut()
    {
        return false;
    }

    private boolean processCommands(int timeout, boolean throttle) {
        QvpCommand cmd;
        if (timeout != 0) {
            //  If we are asked to wait, simply ask mailbox to wait.
            cmd = mailbox.recv(timeout);
        }
        else {
            long tsc = 0; // save cpu Clock.rdtsc ();
            if (tsc != 0 && throttle) {
                if (tsc >= lastTsc && tsc - lastTsc <= QvpConfig.MAX_COMMAND_DELAY.getValue()) {
                    return true;
                }
                lastTsc = tsc;
            }

            cmd = mailbox.recv(0);
        }

        while (true) {
            if (cmd == null) {
                break;
            }

            cmd.destination().processCommand(cmd);
            cmd = mailbox.recv(0);
        }
        if (ctxTerminated) {
            errno.set(ZError.ETERM); // Do not raise exception at the blocked operation
            return false;
        }

        return true;
    }

    public int getSocketOpt(int opt) {
        return 0;
    }

    public boolean bind(String addr) {
        if (ctxTerminated) {
            return false;
        }

        boolean brc = processCommands(0, false);
        if (!brc) {
            return false;
        }

        QvpIoThread ioThread = chooseIoThread(options.affinity);
        if (ioThread == null) {
            throw new IllegalArgumentException("");
        }

        SimpleURI uri = SimpleURI.create(addr);
        String protocal = uri.getProtocol();

        if (protocal.equals("tcp")) {

        }


        return false;
    }

    public boolean connect(String addr) {
        return false;
    }

    public boolean termEndpoint(String addr) {
        return false;
    }

    public boolean send(QvpMsg msg, int flags) {
        return false;
    }

    public Msg recv(int flags) {
        return null;
    }

    private static class SimpleURI
    {
        private final String protocol;
        private final String address;

        private SimpleURI(String protocol, String address)
        {
            this.protocol = protocol;
            this.address = address;
        }

        public static SimpleURI create(String value)
        {
            int pos = value.indexOf("://");
            if (pos < 0) {
                throw new IllegalArgumentException("Invalid URI: " + value);
            }
            String protocol = value.substring(0, pos);
            String address = value.substring(pos + 3);

            if (protocol.isEmpty() || address.isEmpty()) {
                throw new IllegalArgumentException("Invalid URI: " + value);
            }
            return new SimpleURI(protocol, address);
        }

        public String getProtocol()
        {
            return protocol;
        }

        public String getAddress()
        {
            return address;
        }
    }
}
