package com.bmn.socket.netty.channel;

/**
 * Created by Administrator on 2017/1/13.
 */
public final class WriteBufferWaterMark {
    private static final int DEFAULT_LOW_WATER_MARK = 32 * 1024;
    private static final int DEFAULT_HIGH_WATER_MARK = 64 * 1024;

    public static final WriteBufferWaterMark DEFAULT = new WriteBufferWaterMark(DEFAULT_LOW_WATER_MARK, DEFAULT_HIGH_WATER_MARK);

    private final int low;
    private final int high;

    public WriteBufferWaterMark(int low, int high) {
        this.low = low;
        this.high = high;
    }

    WriteBufferWaterMark(int low, int high, boolean validate) {
        if(validate) {
            if(low < 0) {
                throw new IllegalArgumentException("");
            }

            if(high < low) {
                throw new IllegalArgumentException();
            }
        }

        this.low = low;
        this.high = high;
    }

    public int low() {
        return this.low;
    }

    public int high(){
        return this.high;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(55);
        builder.append("WriteBufferWaterMark(low: ")
                .append(low)
                .append(", high: ")
                .append(high)
                .append(")");
        return builder.toString();
    }
}
