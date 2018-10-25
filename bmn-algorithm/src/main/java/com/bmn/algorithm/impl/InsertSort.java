package com.bmn.algorithm.impl;

/**
 * 插入排序
 */
public class InsertSort {

    /**
     * 直接插入排序（其实是插入到一个有序的队列）
     * O(n^2)
     * 稳定排序
     */
    public void sort1() {
        int[] a = {3,1,5,7,2,4,9,6};
        for(int i = 1, l = a.length; i < l; i++) {
            int j = i - 1; //前一个元素
            //前面一个比后面元素大
            if (a[i] < a[j]) {
                int x = a[i]; //当前元素

                while(x < a[j]) {   //当前元素比他前面的元素都小，则一直后移
                    a[j + 1] = a[j];
                    j--;
                }

                a[j + 1] = x;   //当前元素插入的位置


            }
        }
    }

    /**
     * 希尔排序-也叫增量排序
     * 把数据切分为多块，分别直接插入排序，最后整个串有序
     * 确定增量dk（直接插入排序的增量是1）
     * dk = n / 2;
     * 缩小增量，如果是直接插入排序，dk=1
     * 最后一定是dk为1的增量排序。保证所有
     *
     * 不稳定排序
     */
    public void sort2() {
        int[] a = {3,1,5,7,2,4,9,6};
        int n = a.length;
        int dk = n / 2;
        while (dk >= 1) {
            sort3(a, n, dk);
            dk = dk/2;  //缩小增量，如果是直接插入排序，dk=1
        }
    }

    private void sort3(int[] a, int n, int dk) {
        for(int i = dk, l = n; i < l; i++) {
            int j = i - dk; //前一个元素
            //前面一个比后面元素大
            if (a[i] < a[j]) {
                int x = a[i]; //当前元素

                while(x < a[j]) {   //当前元素比他前面的元素都小，则一直后移
                    a[j + dk] = a[j];
                    j -= dk;
                }
                a[j + dk] = x;   //当前元素插入的位置
            }
        }
    }

}
