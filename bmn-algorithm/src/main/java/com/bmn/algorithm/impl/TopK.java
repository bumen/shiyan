package com.bmn.algorithm.impl;

/**
 * 给定一组数据，查找出最大的k个元素
 */
public class TopK {

    /**
     * 方案1
     * 排序，然后取出最大K个
     * O(n*lg(n))
     */
    public void top1() {

    }

    /**
     * 方案2
     * 只排序k个元素
     * 使用冒泡排序
     * O(n * k)
     */
    public void top2() {

    }

    /**
     * 方案3
     * k个元素不需要排序
     * 小顶堆
     * 取k个元素组成小顶堆，每次与其它数据比较，替换堆顶元素，并重新生成小顶堆
     * 最后k个元素就是topk
     * O(n*lg(k))
     */
    public void top3() {

    }

    /**
     * 通过二分查询，+ partition
     */
    public void top4() {

    }

    /**
     * 通过bitmap实现
     */
    public void top5() {
        int[] n = {1,2,3,9};
        int[] map = new int[10];

        for(int i : n) {
            map[i] = 1;
        }
        int k = 2;
        for(int i = map.length; i > 0; i--) {
            int v = map[i];

            if(v == 1 && k > 0) {
                System.out.println(v);
                k--;
            }
        }

    }
}
