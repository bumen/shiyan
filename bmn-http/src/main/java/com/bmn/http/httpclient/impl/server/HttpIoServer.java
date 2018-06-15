package com.bmn.http.httpclient.impl.server;

import com.bmn.http.httpclient.config.SocketConfig;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Administrator on 2017/5/24.
 */
public class HttpIoServer {


    enum Status { READY, ACTIVE, STOPPING }

    private final Logger exceptionLogger = LoggerFactory.getLogger(HttpIoServer.class);
    private final int port;
    private final SocketConfig socketConfig;
    private final HttpService httpService;
    private final HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
    private final ThreadPoolExecutor listenerExecutorService;
    private final ThreadGroup workerThreads;
    private final WorkerPoolExecutor workerPoolExecutor;
    private final AtomicReference<Status> status;
    private final ServerSocketFactory serverSocketFactory;


    private volatile ServerSocket serverSocket;
    private volatile RequestListener listener;

    public HttpIoServer(int port, SocketConfig socketConfig, HttpService httpService,
                        HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory) {
        this.port = port;
        this.socketConfig = socketConfig;
        this.httpService = httpService;
        this.connectionFactory = connectionFactory;
        this.listenerExecutorService = new ThreadPoolExecutor(1, 1, 0l, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactoryImpl("HTTP-listener-" + this.port));
        this.workerThreads = new ThreadGroup("HTTP-workers");
        this.workerPoolExecutor = new WorkerPoolExecutor(0, Integer.MAX_VALUE, 1l, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactoryImpl("HTTP-worker", this.workerThreads));

        serverSocketFactory = ServerSocketFactory.getDefault();
        this.status = new AtomicReference<Status>(Status.READY);
    }


    public void start() throws IOException {
        if(status.compareAndSet(Status.READY, Status.ACTIVE)) {
            this.serverSocket = serverSocketFactory.createServerSocket(port, this.socketConfig.getBacklogSize(), null);
            this.serverSocket.setReuseAddress(socketConfig.isSoReuseAddress());
            if(socketConfig.getRevBufSize() > 0) {
                this.serverSocket.setReceiveBufferSize(socketConfig.getRevBufSize());
            }

            this.listener = new RequestListener(socketConfig, serverSocket, httpService,
                    connectionFactory, exceptionLogger, workerPoolExecutor);
            this.listenerExecutorService.execute(listener);
        }
    }

    public void stop() {
        if (status.compareAndSet(Status.ACTIVE, Status.STOPPING)) {
            this.listenerExecutorService.shutdown();
            this.workerPoolExecutor.shutdown();
            final RequestListener l = this.listener;
            if (l != null) {
                try {
                    l.terminate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.workerThreads.interrupt();

        }
    }

}
