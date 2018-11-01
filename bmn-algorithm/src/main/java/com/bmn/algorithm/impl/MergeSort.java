package com.bmn.algorithm.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 归并排序
 */
public class MergeSort {

    public static void main(String[] args) {
        MergeSort m = new MergeSort();
        int[] a = {72, 11, 3, 82, 32, 44, 13, 17, 95, 54, 28, 79, 56};
        a = m.mergeSort(a);

        for(int i : a) {
            System.out.println(i);
        }
    }

    private int[] mergeSort(int[] arr) {
        int len = arr.length;
        if(len < 2) {
            return arr;
        }

        int mid = len / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    /**
     * 把两个有序的集合，合并
     * @param left，有序
     * @param right，有序
     * @return
     */
    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int pos = 0;
        int leftIndex = 0, rightIndex = 0;
        //肯定会有一个集合索引达到最大，并退出循环
        while (leftIndex < left.length && rightIndex < right.length) {
            if (left[leftIndex] <= right[rightIndex]) {
                result[pos] = left[leftIndex];
                leftIndex++;
                pos++;
            } else {
                result[pos] = right[rightIndex];
                rightIndex++;
                pos++;
            }
        }

        while(leftIndex < left.length) {
            result[pos] = left[leftIndex];
            leftIndex++;
            pos++;
        }

        while (rightIndex < right.length) {
            result[pos] = right[rightIndex];
            rightIndex++;
            pos++;
        }

        return result;
    }
}
