package com.bmn.algorithm.impl;

import java.util.Queue;

/**
 * 交换排序
 */
public class BubbleSort {


    /**
     * 冒泡排序
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

    private int partition(int[] a, int low, int high) {
        int base = a[low];
        while (low < high) {
            while (low < high && a[high] >= base) {
                high--;
            }
            int t = a[low];
            a[low] = a[high];
            a[high] = t;

            while(low < high && a[low] <=   base) {
                low++;
            }

             t = a[low];
            a[low] = a[high];
            a[high] = t;
        }

        return low;
    }
}
