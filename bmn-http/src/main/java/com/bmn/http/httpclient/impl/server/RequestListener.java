package com.bmn.http.httpclient.impl.server;

import com.bmn.http.httpclient.config.SocketConfig;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/5/24.
 */
public class RequestListener implements Runnable {
    private final SocketConfig socketConfig;
    private final ServerSocket serverSocket;
    private final HttpService httpService;
    private final HttpConnectionFactory<? extends HttpServerConnection> connectionFactory;
    private final Logger exceptionLogger;
    private final ExecutorService executorService;
    private final AtomicBoolean terminated;

    public RequestListener(SocketConfig socketConfig, ServerSocket serverSocket, HttpService httpService,
                           HttpConnectionFactory<? extends HttpServerConnection> connectionFactory,
                           Logger exceptionLogger, ExecutorService executorService) {
        this.socketConfig = socketConfig;
        this.serverSocket = serverSocket;
        this.httpService = httpService;
        this.connectionFactory = connectionFactory;
        this.exceptionLogger = exceptionLogger;
        this.executorService = executorService;
        this.terminated = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        try {
            while (!isTerminated() && !Thread.interrupted()) {
                final Socket socket = serverSocket.accept();
                socket.setSoTimeout(socketConfig.getSoTimeout());
                socket.setKeepAlive(socketConfig.isSoKeepAlive());
                socket.setTcpNoDelay(socketConfig.isTcpNoDelay());
                if (socketConfig.getRevBufSize() > 0) {
                    socket.setReceiveBufferSize(socketConfig.getRevBufSize());
                }

                if (socketConfig.getSendBufSize() > 0) {
                    socket.setSendBufferSize(socketConfig.getSendBufSize());
                }

                if (socketConfig.getSoLinger() > 0) {
                    socket.setSoLinger(true, socketConfig.getSoLinger());
                }

                final HttpServerConnection conn = connectionFactory.createConnection(socket);
                final Worker worker = new Worker(httpService, conn, this.exceptionLogger);
                this.executorService.execute(worker);
            }

        }catch (Exception e) {
            exceptionLogger.error(e.getMessage());
        }
    }

    public boolean isTerminated() {
        return this.terminated.get();
    }

    public void terminate() throws IOException {
        if (this.terminated.compareAndSet(false, true)) {
            this.serverSocket.close();
        }
    }
}
