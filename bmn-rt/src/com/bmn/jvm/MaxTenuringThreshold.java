package com.bmn.jvm;

/**
 * 对象在年轻存活最大年龄
 */
public class MaxTenuringThreshold {

    private static final int _1MB = 1024 * 1024;

    public static void allocateThreshold1() {
        byte[] a1, a2, a3, a4;

        a1 = new byte[_1MB / 4];
        a2 = new byte[3 * _1MB];
        //当分配a3时，触发第一次minor gc， a1, a2年龄到1进入年老代
        //第一次结果后，a3被分配到eden
        a3 = new byte[3 * _1MB];
        //a4被分配到eden
        a4 = new byte[3 * _1MB];

        a3 = null;

        //当再次分配a3时，触发第二次minor gc, 上一个a3=null的被清除，  a4年龄到1进入老年代，此时老年代为 a1,a2, a4
        //同时触发Major gc， eden 被清空
        //gc结束后，a3被分配到eden
        a3 = new byte[3 * _1MB];
    }

    public static void allocateThreshold15() {
        byte[] a1, a2, a3, a4;

        a1 = new byte[_1MB / 4];
        a2 = new byte[3 * _1MB];
        //当分配a3时，触发第一次minor gc， a1, 进入s1, a2 进入老年代
        //第一次结果后，a3被分配到eden
        a3 = new byte[3 * _1MB];
        //a4被分配到eden
        a4 = new byte[3 * _1MB];

        a3 = null;

        //当再次分配a3时，触发第二次minor gc, 上一个a3=null的被清除，  a1进入s2, a4进入老年代，此时老年代为 a2, a4
        //gc结束后，a3被分配到eden
        a3 = new byte[3 * _1MB];
    }

    public static void main(String[] args) {
        allocateThreshold15();
    }
}
