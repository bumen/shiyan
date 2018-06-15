package com.bmn.socket.netty.util;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2017/1/13.
 */
public class ThreadLocalRandom extends Random {
    private static final AtomicLong seedUniquifier = new AtomicLong();
    private static volatile long initialSeedUniquifier = 0;

    static {
        if(initialSeedUniquifier == 0) {
            initialSeedUniquifier = mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
        }
    }

    public static long getInitialSeedUniquifier() {
        long initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
        if(initialSeedUniquifier != 0)
            return initialSeedUniquifier;

        synchronized (ThreadLocalRandom.class) {
            initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
            if (initialSeedUniquifier != 0) {
                return initialSeedUniquifier;
            }

            initialSeedUniquifier ^= 0x3255ecdc33bae119L; // just a meaningless random number
            initialSeedUniquifier ^= Long.reverse(System.nanoTime());
            ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
        }

        return initialSeedUniquifier;
    }

    private static long newSeed() {
        for(;;) {
            final long current = seedUniquifier.get();
            final long actualCurrent = current != 0 ? current : getInitialSeedUniquifier();
            final long next = actualCurrent * 181783497276652981L;
            if(seedUniquifier.compareAndSet(current, next)) {
                return next ^ System.nanoTime();
            }
        }
    }

    boolean initialized;

    private static final long multiplier = 0x5DEECE66DL;
    private long rnd;
    private static final long mask = (1L << 48) - 1;

    ThreadLocalRandom() {
        super(newSeed());
        initialized = true;
    }

    public void setSeed(long seed) {
        if(initialized) {
            throw new UnsupportedOperationException();
        }
        rnd = (seed ^ multiplier) & mask;
    }

    private static long mix64(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }

}
