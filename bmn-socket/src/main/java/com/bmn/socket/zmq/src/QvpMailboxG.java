package com.bmn.socket.zmq.src;

import com.bmn.socket.zmq.QvpSignaler;
import zmq.Config;
import zmq.YPipe;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/6/14.
 */
public class QvpMailboxG implements Closeable {

    private final YPipe<QvpCommand> cpipe;

    private final QvpSignaler signaler;

    private final Lock sync;

    private boolean active;

    private final String name;

    public QvpMailboxG(String name) {
        cpipe = new YPipe<>(Config.COMMAND_PIPE_GRANULARITY.getValue());
        sync = new ReentrantLock();
        signaler = new QvpSignaler();

        QvpCommand cmd = cpipe.read();

        //zhangsan

        active = false;
        this.name = name;
    }

    public SelectableChannel getFd()
    {
        return signaler.getFd();
    }

    public void send(final QvpCommand cmd)
    {
        boolean ok = false;
        sync.lock();
        try {
            cpipe.write(cmd, false);
            ok = cpipe.flush();
        }
        finally {
            sync.unlock();
        }

        if (!ok) {
            signaler.send();
        }
    }

    public QvpCommand recv(long timeout)
    {
        QvpCommand cmd = null;
        //  Try to get the command straight away.
        if (active) {
            cmd = cpipe.read();
            if (cmd != null) {
                return cmd;
            }

            //  If there are no more commands available, switch into passive state.
            active = false;
            signaler.recv();
        }

        //  Wait for signal from the command sender.
        boolean rc = signaler.waitEvent(timeout);
        if (!rc) {
            return null;
        }

        //  We've got the signal. Now we can switch into active state.
        active = true;

        //  Get a command.
        cmd = cpipe.read();
        assert (cmd != null);

        return cmd;
    }

    @Override
    public void close() throws IOException
    {
        //  TODO: Retrieve and deallocate commands inside the cpipe.

        // Work around problem that other threads might still be in our
        // send() method, by waiting on the mutex before disappearing.
        sync.lock();
        sync.unlock();

        signaler.close();
    }

    @Override
    public String toString()
    {
        return super.toString() + "[" + name + "]";
    }
}
