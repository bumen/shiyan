package com.bmn.algorithm.impl;

/**
 * 数1的个数实现方法
 */
public class CountOne {


    /**
     * 方案1
     * 有多少个1就计算多少次
     * @param n
     */
    public void count1(int n) {
        int count = 0;
        while (n != 0) {
            count += n & 1;
            n = n >>> 1;
        }
    }

    /**
     * 方案2
     * 有多少个1就计算多少次
     * n   = 10110000
     * n-1 = 10100000
     * n   = 10100000
     * @param n
     */
    public void count2(int n) {
        int count = 0;
        while(n != 0) {
            count++;

            n = n & (n - 1);
        }
    }

    /**
     * 方案3
     * 通过空间换时间
     * int 最多32个1， 一个有2的32方个数
     * 通过bitmap
     * b[1] = 1
     * b[2] = 1;
     * b[3] = 2;
     * b[n] = 32;
     * 1-32， 用5b表示就够了
     * 当找查3时，直接返回2个1
     * 时间复杂度O(1)
     * 空间复杂度2的32次方* 5b = 2.5G, 2的5次方=32
     * 上面空间占用太大改用分组计算
     * int n1 = n & 0xFFFF;
     * int n2 = (n>>16) & 0xFFFF;
     * return n1 + n2;
     *
     * 空间复杂度2的16次方 * 4b = 32k
     *
     * @param n
     */
    public void count3(int n) {

    }


    public void calc(int n) {

    }
}
