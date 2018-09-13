package com.bmn.socket.serializable;

import java.awt.Button;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class ObjectVO implements Serializable{


    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] codec() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] value = this.name.getBytes();
        byteBuffer.putInt(value.length);
        byteBuffer.put(value);
        byteBuffer.putInt(this.id);
        byteBuffer.flip();
        value = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }

    public byte[] codec(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        byte[] value = this.name.getBytes();
        byteBuffer.putInt(value.length);
        byteBuffer.put(value);
        byteBuffer.putInt(this.id);
        byteBuffer.flip();
        value = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }
}
