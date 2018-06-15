package com.bmn.rt.util;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/26.
 */
public class QvpBitSet {
    private long[] words;
    private transient int wordsInUse = 0;

    public QvpBitSet() {
        initWords(1 << 6);
    }

    public int size() {
        return words.length * (1 << 6);
    }

    public int count() {
        return words.length;
    }

    private void initWords(int nbits) {
        words = new long[wordIndex(nbits-1) + 1];
    }

    private static int wordIndex(int bitIndex) {
        return bitIndex >> 6;
    }

    public void set(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

        int wordIndex = wordIndex(bitIndex);
        expandTo(wordIndex);


        words[wordIndex] |= (1L << bitIndex); // Restores invariants
    }

    private void expandTo(int wordIndex) {
        int wordsRequired = wordIndex+1;
        if (wordsInUse < wordsRequired) {
            ensureCapacity(wordsRequired);
            wordsInUse = wordsRequired;
        }

    }
    private void ensureCapacity(int wordsRequired) {
        if (words.length < wordsRequired) {
            // Allocate larger of doubled size or required size
            int request = Math.max(2 * words.length, wordsRequired);
            words = Arrays.copyOf(words, request);
        }
    }

    public boolean get(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

        int wordIndex = wordIndex(bitIndex);
        return (wordIndex < wordsInUse)
                && ((words[wordIndex] & (1L << bitIndex)) != 0);
    }
}
