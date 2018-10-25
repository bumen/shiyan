package com.bmn.algorithm.impl;

/**
 * 选择排序
 */
public class SelectSort {


    /**
     * 简单选择排序
     */
    public void sort1() {
        int[] a = {3,1,5,7,2,4,9,6};

        for(int i = 0, l = a.length; i < l; i++) {
            int s = a[i];
            int k = i;

            //选择最大的数交换
            for(int j = i + 1; j < l; j++) {
                if(a[k] < a[j]) {
                    k = j;
                }
            }

            if(k != i) {
                int t = a[k];
                a[k] = s;
                a[i] = t;
            }
        }
    }

    /**
     * 二元选择排序，每次比较出一个最大与最小
     * 只需要n/2次比较
     */
    public void sort2() {
        int[] a = {3,1,5,7,2,4,9,6};
        int n = a.length /2;
        int min = 0, max = 0;
        for(int i = 1; i < n / 2; i++) {
            min = i; max = i;
            for(int j = i + 1; j < n - i ; j++) {
                if (a[j] > a[max]) {
                    max = j; continue;
                }

                if (a[j] < a[min]) {
                    min = j;
                }
            }

            if (min != i) {
                int t = a[i - 1];
                a[i - 1] = a[min];
                a[min] = t;
            }

            if (max != i) {
                int t = a[n-i ];
                a[n-i] = a[max];
                a[max] = t;
            }
        }
    }

    /**
     * 堆排序，
     * 堆是一个完全二叉树
     * 堆顶元素要么都大于其子女（大顶堆），要么都小于其子女（小顶堆）
     *
     * 1、构建堆,需要确定最后一个非叶子结点位置 (n-1)/2
     * 2、调整堆时，需要确定左孩子位置 2*i + 1;
     *
     * 最后一个非叶子结点为（n-1）/2
     * 开始向前构建，一个到根结点
     *
     */
    public void buildHeap(int[] a) {
        int n = a.length;
        for(int i = (n-1)/2; i >= 0; i--) {
            headAdjust(a, i, n);
        }
    }

    /**
     * 堆排序
     */
    public void sortHeap() {
        int[] a = {3,1,5,7,2,4,9,6};
        buildHeap(a);
        //构建完后堆顶元素是最大的，或最小的

        //从最后一个元素开始对序列进行调整
        for(int i = a.length -1; i >= 0; i--) {
            int t = a[i]; a[i]= a[0]; a[0] = t;
            //每次交换堆顶元素和堆中最后一个元素之后，都要对堆进行调整
            headAdjust(a, 0, i);
        }
    }

    /**
     * 只调整非叶子节点
     * 第i个非叶子节点的左孩子：2*i + 1;
     *
     * 从某一个非叶子节点，一直向下调整。直到所有结点都满足
     * @param a
     * @param i
     * @param len
     */
    private void headAdjust(int[] a, int i, int len) {
        int tmp = a[i];
        int child = 2 * i + 1; //记录左孩子, child++为右孩子
        while(child < len) {
            if(child + 1 < len && a[child] < a[child+1]) { //从两个孩子节点中，找出最大的节点
                ++child;
            }

            //如果孩子节点比父节点大，则需要替换父结点
            if(a[i] < a[child]) {
                a[i] = a[child];
                i = child;  //因为这两个结点交换了，所以需要再去向下调整，以此来满足堆性质
                child = 2* i + 1;
            } else {
                break;
            }

            a[i] = tmp;
        }
    }
}
