package com.bmn.algorithm.impl;

/**
 * 计数排序
 *
 * 适合待排序的元素在某一个范围[min, max]
 * 适合数字值域范围小，且数据多，且数据重复多
 *
 * 实现
 * 定义一个max-min大小的数组
 *
 */
public class CountSort {


    public void sort() {
        int[] n = {5, 3, 7, 1, 8, 2, 9, 4, 7, 2, 6, 6, 2, 6, 6};
        int[] c = new int[10]; // min = 0, max = 10

        //计算每个数出现的次数
        for(int i = 0, l = n.length; i < l; i++) {
            int v = n[i];
            c[v] = c[v] + 1;
        }


        for(int i = 0; i < 10; i++  ) {
            while(c[i] > 0) {
                System.out.println(i);
                c[i] = c[i]- 1;
            }
        }
    }
}
