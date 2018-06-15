package com.bmn.socket.zmq.src;

import zmq.*;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/6/14.
 */
public class QvpCtx {
    private static final int WAIT_FOREVER = -1;

    static class Endpoint {

    }

    private int tag;

    private final List<SocketBase> sockets;

    private final Deque<Integer> emptySlots;

    private AtomicBoolean starting = new AtomicBoolean(true);

    private boolean terminating;

    private final Lock slotSync;

    private List<Selector> selectors;

    private QvpReaper reaper;

    private final List<QvpIoThread> ioThreads;

    private int slotCount;

    private QvpMailboxG[] slots;

    private final QvpMailboxG termMailbox;


    private final Map<String, Endpoint>  endpoints;

    private final Lock endpointsSync;

    private static AtomicInteger maxSocketId = new AtomicInteger(0);

    private int maxSockets;

    private int ioThreadCount;

    private boolean blocky;

    private final Lock optSync;

    public static final int TERM_TID = 0;
    public static final int REAPER_TID = 1;

    public QvpCtx() {
        tag = 0xabadcaff;
        terminating= false;
        reaper = null;
        slotCount = 0;
        slots = null;

        maxSockets = ZMQ.ZMQ_MAX_SOCKETS;

        ioThreadCount = ZMQ.ZMQ_IO_THREADS;

        blocky = true;

        slotSync = new ReentrantLock();
        endpointsSync = new ReentrantLock();
        optSync = new ReentrantLock();

        termMailbox = new QvpMailboxG("terminator");

        emptySlots = new ArrayDeque<>();
        ioThreads = new ArrayList<>();
        sockets = new ArrayList<>();
        selectors = new ArrayList<>();
        endpoints = new HashMap<>();

    }


    private void destroy() throws IOException
    {
        for (QvpIoThread it : ioThreads) {
            it.stop();
        }
        for (QvpIoThread it : ioThreads) {
            it.close();
        }

        for (Selector selector : selectors) {
            if (selector != null) {
                selector.close();
            }
        }
        selectors.clear();

        if (reaper != null) {
            reaper.close();
        }
        termMailbox.close();

        tag = 0xdeadbeef;
    }

    public boolean checkTag()
    {
        return tag == 0xabadcaff;
    }

    public void terminate()
    {
        tag = 0xdeadbeef;

        if (!starting.get()) {
            slotSync.lock();
            try {
                //  Check whether termination was already underway, but interrupted and now
                //  restarted.
                boolean restarted = terminating;
                terminating = true;

                //  First attempt to terminate the context.
                if (!restarted) {
                    //  First send stop command to sockets so that any blocking calls
                    //  can be interrupted. If there are no sockets we can ask reaper
                    //  thread to stop.
                    for (SocketBase socket : sockets) {
                        socket.stop();
                    }
                    if (sockets.isEmpty()) {
                        reaper.stop();
                    }
                }
            }
            finally {
                slotSync.unlock();
            }
            //  Wait till reaper thread closes all the sockets.
            QvpCommand cmd = termMailbox.recv(WAIT_FOREVER);
            if (cmd == null) {
                throw new IllegalStateException();
            }
            assert (cmd.type() == QvpCommand.Type.DONE);
            slotSync.lock();
            try {
                assert (sockets.isEmpty());
            }
            finally {
                slotSync.unlock();
            }
        }

        //  Deallocate the resources.
        try {
            destroy();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean set(int option, int optval)
    {
        if (option == ZMQ.ZMQ_MAX_SOCKETS && optval >= 1) {
            optSync.lock();
            try {
                maxSockets = optval;
            }
            finally {
                optSync.unlock();
            }
        }
        else
        if (option == ZMQ.ZMQ_IO_THREADS && optval >= 0) {
            optSync.lock();
            try {
                ioThreadCount = optval;
            }
            finally {
                optSync.unlock();
            }
        }
        else
        if (option == ZMQ.ZMQ_BLOCKY && optval >= 0) {
            optSync.lock();
            try {
                blocky = (optval != 0);
            }
            finally {
                optSync.unlock();
            }
        }
        else {
            return false;
        }
        return true;
    }

    public int get(int option)
    {
        int rc = 0;
        if (option == ZMQ.ZMQ_MAX_SOCKETS) {
            rc = maxSockets;
        }
        else if (option == ZMQ.ZMQ_IO_THREADS) {
            rc = ioThreadCount;
        }
        else if (option == ZMQ.ZMQ_BLOCKY) {
            rc = blocky ? 1 : 0;
        }
        else {
            throw new IllegalArgumentException("option = " + option);
        }
        return rc;
    }

    public QvpSocketBase createSocket(int type)
    {
        QvpSocketBase s = null;
        slotSync.lock();
        try {
            if (starting.compareAndSet(true, false)) {
                initSlots();
            }

            //  Once zmq_term() was called, we can't create new sockets.
            if (terminating) {
                throw new ZError.CtxTerminatedException();
            }

            //  If maxSockets limit was reached, return error.
            if (emptySlots.isEmpty()) {
                throw new IllegalStateException("EMFILE");
            }

            //  Choose a slot for the socket.
            int slot = emptySlots.pollLast();

            //  Generate new unique socket ID.
            int sid = maxSocketId.incrementAndGet();

            //  Create the socket and register its mailbox.
            /*s = SocketBase.create(type, this, slot, sid);
            if (s == null) {
                emptySlots.addLast(slot);
                return null;
            }
            sockets.add(s);
            slots[slot] = s.getMailbox();*/
        }
        finally {
            slotSync.unlock();
        }

        return s;
    }

    public void destroySocket(SocketBase socket)
    {
        slotSync.lock();

        //  Free the associated thread slot.
        try {
           // int tid = socket.getTid();
            //emptySlots.add(tid);
            //slots[tid] = null;

            //  Remove the socket from the list of sockets.
            sockets.remove(socket);

            //  If zmq_term() was already called and there are no more socket
            //  we can ask reaper thread to terminate.
            if (terminating && sockets.isEmpty()) {
                reaper.stop();
            }
        }
        finally {
            slotSync.unlock();
        }
    }

    // Creates a Selector that will be closed when the context is destroyed.
    public Selector createSelector()
    {
        try {
            Selector selector = Selector.open();
            assert (selector != null);
            selectors.add(selector);
            return selector;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //  Returns reaper thread object.
    QvpZObject getReaper()
    {
        return reaper;
    }

    void sendCommand(int tid, final QvpCommand command)
    {
        slots[tid].send(command);
    }

    //  Returns the I/O thread that is the least busy at the moment.
    //  Affinity specifies which I/O threads are eligible (0 = all).
    //  Returns NULL if no I/O thread is available.
    QvpIoThread chooseIoThread(long affinity)
    {
        if (ioThreads.isEmpty()) {
            return null;
        }

        //  Find the I/O thread with minimum load.
        int minLoad = -1;
        QvpIoThread selectedIoThread = null;

        for (int i = 0; i != ioThreads.size(); i++) {
            if (affinity == 0 || (affinity & (1L << i)) > 0) {
                int load = ioThreads.get(i).getLoad();
                if (selectedIoThread == null || load < minLoad) {
                    minLoad = load;
                    selectedIoThread = ioThreads.get(i);
                }
            }
        }
        return selectedIoThread;
    }

    //  Management of inproc endpoints.
    boolean registerEndpoint(String addr, Endpoint endpoint)
    {
        endpointsSync.lock();

        Endpoint inserted = null;
        try {
            inserted = endpoints.put(addr, endpoint);
        }
        finally {
            endpointsSync.unlock();
        }
        return inserted == null;
    }

    void unregisterEndpoints(SocketBase socket)
    {
        endpointsSync.lock();

        try {
            Iterator<Map.Entry<String, Endpoint>> it = endpoints.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Endpoint> e = it.next();
               /* if (e.getValue().socket == socket) {
                    it.remove();
                }*/
            }
        }
        finally {
            endpointsSync.unlock();
        }
    }

    Endpoint findEndpoint(String addr)
    {
        Endpoint endpoint = null;
        endpointsSync.lock();

        try {
            endpoint = endpoints.get(addr);
            if (endpoint == null) {
                //return new Endpoint(null, new QvpOptions());
            }

            //  Increment the command sequence number of the peer so that it won't
            //  get deallocated until "bind" command is issued by the caller.
            //  The subsequent 'bind' has to be called with inc_seqnum parameter
            //  set to false, so that the seqnum isn't incremented twice.
            //endpoint.socket.incSeqnum();
        }
        finally {
            endpointsSync.unlock();
        }
        return endpoint;
    }

    private void initSlots()
    {
        slotSync.lock();
        try {
            //  Initialize the array of mailboxes. Additional two slots are for
            //  zmq_term thread and reaper thread.
            int slotCount;
            int ios;
            optSync.lock();
            try {
                ios = ioThreadCount;
                slotCount = maxSockets + ioThreadCount + 2;
            }
            finally {
                optSync.unlock();
            }
            slots = new QvpMailboxG[slotCount];
            //alloc_assert (slots);

            //  Initialize the infrastructure for zmq_term thread.
            slots[TERM_TID] = termMailbox;

            //  Create the reaper thread.
            reaper = new QvpReaper(this, REAPER_TID);
            //alloc_assert (reaper);
            slots[REAPER_TID] = reaper.getMailbox();
            reaper.start();

            //  Create I/O thread objects and launch them.
            for (int i = 2; i != ios + 2; i++) {
                QvpIoThread ioThread = new QvpIoThread(this, i);
                //alloc_assert (io_thread);
                ioThreads.add(ioThread);
                slots[i] = ioThread.getMailbox();
                ioThread.start();
            }

            //  In the unused part of the slot array, create a list of empty slots.
            for (int i = slotCount - 1;
                 i >= ios + 2; i--) {
                emptySlots.add(i);
                slots[i] = null;
            }
        }
        finally {
            slotSync.unlock();
        }
    }
}

