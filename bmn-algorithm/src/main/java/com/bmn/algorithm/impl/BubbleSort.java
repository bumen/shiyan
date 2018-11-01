package com.bmn.algorithm.impl;

import java.util.Queue;

/**
 * 交换排序
 */
public class BubbleSort {


    /**
     * 冒泡排序
     * O(n^2), 稳定
     */
    public void sort1() {
        int[] a = {3,1,5,7,2,4,9,6};
        for(int i = 0, l = a.length; i < l-1; i++) {
            for(int j = 0; j < l -i -1; j++) {
                if (a[j] > a[j + 1]) {
                    int t = a[j];
                    a[j] = a[j + 1];
                    a[j+1] = t;
                }
            }
        }
    }

    public void sortt() {
        int[] a = {};
        for(int i = 0, l = a.length; i < l - 1; i++) {
            for(int j = 0; j < l - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int t = a[j + 1];
                    a[j+1] = a[j];
                    a[j] = t;
                }
            }
        }
    }


    /**
     * 快速排序
     * 选择一个基准元素,通常选择第一个元素或者最后一个元素
     * 通过一趟排序讲待排序的记录分割成独立的两部分，其中一部分记录的元素值均比基准元素值小。另一部分记录的 元素值比基准值大
     * 此时基准元素在其排好序后的正确位置
     * 然后分别对这两部分记录用同样的方法继续进行排序，直到整个序列有序
     *
     * 不稳定
     *
     * O(n*lg(n))
     */
    public void sort2() {
        int[] a = {3,1,5,7,2,4,9,6};
        quickSort(a, 0, a.length -1);
    }

    public void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int p = partition(a, low, high);
            quickSort(a, low, p - 1);
            quickSort(a, p + 1, high);
        }
    }

    /**
     * 排好一个数
     * @param a
     * @param low
     * @param high
     * @return
     */
    private int partition(int[] a, int low, int high) {
        //取最小值做为哨兵
        int base = a[low];
        while (low < high) {
            //先从后向前查找，最后停止在小于base的索引上
            while (low < high && a[high] >= base) {
                high--;
            }
            //做高低交换
            int t = a[low];
            a[low] = a[high];
            //此时high后边的元素都比t大
            a[high] = t;

            while(low < high && a[low] <=   base) {
                low++;
            }

             t = a[low];
            a[low] = a[high];
            a[high] = t;

            //每找完一次，小于low的元素都比base小，大于high元素都比base大
            //下次要比较的范围是low <--->high之间的元素，同时base要么在high, 要么在low的位置
            //所有都比较完后low == high
        }

        return low;
    }

    private void p(int[] a, int low, int high) {
        int b = a[low];
        while(low < high) {

            while(low < high && a[high] >= b) {
                high--;
            }

            int t = a[high];
            a[high] = a[low];
            a[low] = t;

            while(low < high && a[low] <= b) {
                low++;
            }



        }

    }
}
