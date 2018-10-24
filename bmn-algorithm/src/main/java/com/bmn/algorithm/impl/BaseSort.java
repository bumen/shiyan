package com.bmn.algorithm.impl;

/**
 * 基数排序
 * 基：个位，十位，百位...
 * 桶：基的取值范围 0-9
 * <p>
 * FOR (每一个基) {
 * 循环内，以某一个“基”为依据
 * 第一步：遍历数据集arr，将元素放入对应的桶bucket
 * 第二步：遍历桶bucket，将元素放回数据集arr
 * }
 * <p>
 * 稳定排序
 * <p>
 * 位数不够补0
 */
public class BaseSort {

    private static class Node  {
        private int v;

        private Node next;

        public Node(int v) {
            this.v = v;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public void sort() {
        int[] n = {72, 11, 82, 32, 44, 13, 17, 95, 54, 28, 79, 56};
        Node[] arr = new Node[10];

        boolean first = true;
        int weight = 0;
        int l = n.length;
        int count = 0;
        while (count < l) {
            int v = n[count];



            int b = sortBase(v, 1);

            Node node = arr[b];
            if(node == null) {
                node = new Node(v);
            } else {
                node.setNext(new Node(v));
            }

        }
    }

    private int sortBase(int v, int i) {
        //计算第几位数，就需要除以几
        int wei = 10 * (i - 1);
        int b = wei > 0 ? v / wei : v;
        if (b > 9) {
            b = b % 10;
        }

        return b ;
    }
}
