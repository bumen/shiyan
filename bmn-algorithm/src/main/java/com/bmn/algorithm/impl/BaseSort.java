package com.bmn.algorithm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 数据多，且数的位数少
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
            if(this.next != null) {
                this.next.setNext(next);
            } else {
                this.next = next;
            }
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



    public void sort2() {
        //桶1-9的数字
        Node[] nodes = new Node[10];
        int[] a = {72, 11, 3, 82, 32, 44, 13, 17, 95, 54, 28, 79, 56};
        //循环每个基。默认max个基。当zero的个数== a.length时说明每个基都排完。
        for(int mod = 10, dev = 1, i = 0; i < Integer.MAX_VALUE; i++, dev *= 10, mod *=10) {
            int zero = 0;
            for(int j = 0, l = a.length; j < l; j++) {
                int bucket = (a[j] %mod) / dev; //取出每位的基

                //如果所有位都为0，则表示已经排完序
                if (bucket == 0) {
                    zero++;
                }

                Node e = nodes[bucket]; //把基放到桶中
                if(e == null) {
                    e = new Node(a[j]);
                    nodes[bucket] = e;
                } else {
                    e.setNext(new Node(a[j]));  //如果基相同，则放到队列
                }
            }

            if(zero == a.length) {
                System.out.println("============= "+ i);
                break;
            }

            //按顺序
            int pos = 0;
            for(int m = 0, l = nodes.length; m < l; m++) {
                Node node = nodes[m];
                while(node != null) {
                    a[pos] = node.v;
                    node = node.next;
                    pos++;
                }
                nodes[m] = null;
            }

        }

        for(int i : a) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        int a = 123;
        System.out.println(a % 10);
        BaseSort base = new BaseSort();
        base.sort2();
    }
}
