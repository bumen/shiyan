package com.bmn.socket.zmq.src;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/6/16.
 */
public class QvpBlob {
    private final byte[] buf;
    private QvpBlob(byte[] data) {
        buf = data;
    }

    public static QvpBlob createBlob(byte[] data, boolean copy) {
        if (copy) {
            byte[] b = new byte[data.length];
            System.arraycopy(data, 0, b, 0, data.length);
            return new QvpBlob(b);
        } else {
            return new QvpBlob(data);
        }
    }

    public int size() {
        return buf.length;
    }

    public byte[] data() {
        return buf;
    }

    @Override
    public boolean equals(Object t)
    {
        if (t instanceof QvpBlob) {
            return Arrays.equals(buf, ((QvpBlob) t).buf);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(buf);
    }


}
