package com.bmn.algorithm.square;

/**
 * @date 2019-12-18
 * @author zhangyuqiang02@playcrab.com
 */
public class SquareRoot {

    private static int time = 0;

    public static double calc(int v, double guess) {
        double second = v / guess;
        double third = second + guess;
        double fourth = third / 2;

        System.out.println(fourth);

        time++;

        if (time > 11) {
            return fourth;
        }

        return calc(v, fourth);
    }

    public static void main(String[] args) {
        int v = 10;
        double guess = 111d;
        double r = SquareRoot.calc(v, guess);

        System.out.println( v + " square root is " + r);
    }
}
