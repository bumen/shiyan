/**
 * Copyright (c) 2019 landon30
 */

package com.bmn.rt.lang.concurrent;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 测试ConcurrentSkipListSet一些问题
 *
 * @date 2019-11-29
 * @author landon30
 */
public class ConcurrentSkipListSetExample {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ConcurrentSkipListSet<Element> set = new ConcurrentSkipListSet<>();

            Element element0 = new Element(1, 60);
            Element element1 = new Element(2, 80);
            Element element2 = new Element(3, 100);

            set.add(element0);
            set.add(element1);
            set.add(element2);

            // 1. 在add之后，改变某个元素内用于排序的值
            // 2. remove元素A，有时候会成功,有时候会失败
            element2.setScore(70);

            set.clear();

            boolean isRemove = set.remove(element2);
            if (!isRemove) {
                System.out.println(isRemove);
                System.out.println(set);
            } else {

                System.out.println("---------------------");
            }
        }
    }

    private static class Element implements Comparable<Element> {
        private int id;
        private volatile int score;

        public Element(int id, int score) {
            this.id = id;
            this.score = score;
        }

        /**
         * @param score the score to set
         */
        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public int compareTo(Element o) {
            if (score > o.score) {
                return -1;
            }

            if (score < o.score) {
                return 1;
            }

            int v = Integer.compare(id, o.id);

            return v;
        }

        @Override
        public String toString() {
            return "Element [id=" + id + ", score=" + score + "]";
        }
    }
}
