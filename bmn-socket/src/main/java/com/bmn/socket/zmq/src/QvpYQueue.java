package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/16.
 */
public class QvpYQueue<T> {
    private static class Chunk<T> {
        final T[] values;
        final int[] pos;
        Chunk<T> prev;
        Chunk<T> next;

        public Chunk(int size, int memoryPtr) {
            values = (T[])new Object[size];
            pos = new int[size];
            for(int i = 0; i != size; i++) {
                pos[i] = memoryPtr;
                memoryPtr++;
            }
        }
    }

    private Chunk<T> beginChunk;
    private int beginPos;
    private Chunk<T> backChunk;
    private int backPos;
    private Chunk<T> endChunk;
    private int endPos;
    private volatile Chunk<T> spareChunk;   //备用
    private final int size;

    private int memoryPtr;

    public QvpYQueue(int size) {
        this.size = size;
        memoryPtr = 0;
        beginChunk = new Chunk<>(size, memoryPtr);
        beginPos = 0;
        backPos = 0;
        backChunk = beginChunk;
        spareChunk = beginChunk;
        endChunk = beginChunk;
        endPos = 1;
    }

    public int frontPos() {return beginChunk.pos[beginPos];}

    public T front() {
        return beginChunk.values[beginPos];
    }

    public int backPos() {return backChunk.pos[backPos];}

    public T back() {return backChunk.values[backPos];}

    public T pop() {
        T val = beginChunk.values[beginPos];
        beginChunk.values[beginPos] = null;
        beginPos++;
        if(beginPos == size) {
            beginChunk = beginChunk.next;
            beginChunk.prev = null;
            beginPos = 0;
        }
        return val;
    }

    public void push(T val) {
        backChunk.values[backPos] = val;
        backChunk = endChunk;
        backPos = endPos;
        endPos++;

        if(endPos != size) {
            return;
        }

        Chunk<T> sc = spareChunk;
        if (sc != beginChunk) {
            spareChunk = spareChunk.next;
            endChunk.next = sc;
            sc.prev = endChunk;
        } else {
            endChunk.next = new Chunk<>(size, memoryPtr);
            memoryPtr += size;
            endChunk.next.prev = endChunk;
        }

        endChunk = endChunk.next;
        endPos = 0;
    }

    public void unpush() {
        if(backPos > 0) {
            backPos--;
        } else {
            backPos = size -1;
            backChunk = backChunk.prev;
        }

        if(endPos > 0) {
            endPos--;
        } else {
            endPos = size -1;
            endChunk = endChunk.prev;
            endChunk.next = null;
        }
    }
}
