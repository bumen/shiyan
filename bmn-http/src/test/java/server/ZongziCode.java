package server;

import java.util.Random;

/**
 * Created by Administrator on 2017/6/1.
 */
public class ZongziCode {

    public static void main(String[] args) {

        double d = Math.random();
        System.out.println(0.9999 * 9000);

        System.out.println(mix64(System.currentTimeMillis()));
    }

    private static long mix64(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }
}
