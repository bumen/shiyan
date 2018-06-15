package com.bmn.http.httpclient.config;

/**
 * Created by Administrator on 2017/5/24.
 */
public class SocketConfig {
    private final int soTimeout;
    private final boolean soReuseAddress;
    private final int soLinger;
    private final boolean soKeepAlive;
    private final boolean tcpNoDelay;
    private final int sendBufSize;
    private final int revBufSize;
    private final int backlogSize;

    public SocketConfig(int soTimeout, boolean soReuseAddress, int soLinger, boolean soKeepAlive, boolean tcpNoDelay, int sendBufSize, int revBufSize, int backlogSize) {
        this.soTimeout = soTimeout;
        this.soReuseAddress = soReuseAddress;
        this.soLinger = soLinger;
        this.soKeepAlive = soKeepAlive;
        this.tcpNoDelay = tcpNoDelay;
        this.sendBufSize = sendBufSize;
        this.revBufSize = revBufSize;
        this.backlogSize = backlogSize;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public boolean isSoReuseAddress() {
        return soReuseAddress;
    }

    public int getSoLinger() {
        return soLinger;
    }

    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public int getSendBufSize() {
        return sendBufSize;
    }

    public int getRevBufSize() {
        return revBufSize;
    }

    public int getBacklogSize() {
        return backlogSize;
    }

    public static Builder custom() {
        return new Builder();
    }

    public static class Builder {

        private int soTimeout;
        private boolean soReuseAddress;
        private int soLinger;
        private boolean soKeepAlive;
        private boolean tcpNoDelay;
        private int sndBufSize;
        private int rcvBufSize;
        private int backlogSize;

        Builder() {
            this.soLinger = -1;
            this.tcpNoDelay = true;
        }

        public Builder setSoTimeout(final int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder setSoReuseAddress(final boolean soReuseAddress) {
            this.soReuseAddress = soReuseAddress;
            return this;
        }

        public Builder setSoLinger(final int soLinger) {
            this.soLinger = soLinger;
            return this;
        }

        public Builder setSoKeepAlive(final boolean soKeepAlive) {
            this.soKeepAlive = soKeepAlive;
            return this;
        }

        public Builder setTcpNoDelay(final boolean tcpNoDelay) {
            this.tcpNoDelay = tcpNoDelay;
            return this;
        }

        /**
         * @since 4.4
         */
        public Builder setSndBufSize(final int sndBufSize) {
            this.sndBufSize = sndBufSize;
            return this;
        }

        /**
         * @since 4.4
         */
        public Builder setRcvBufSize(final int rcvBufSize) {
            this.rcvBufSize = rcvBufSize;
            return this;
        }

        /**
         * @since 4.4
         */
        public Builder setBacklogSize(final int backlogSize) {
            this.backlogSize = backlogSize;
            return this;
        }

        public SocketConfig build() {
            return new SocketConfig(soTimeout, soReuseAddress, soLinger, soKeepAlive, tcpNoDelay,
                    sndBufSize, rcvBufSize, backlogSize);
        }

    }
}
