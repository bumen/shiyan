package com.bmn.rt.lang;

public class BmnString {

    private String string;

    private StringBuffer sb;

    private StringBuilder StringBuilder;


    public static void main(String[] args) {
        String s = "汉";

        byte[] bytes = s.getBytes();

        for (byte b : bytes) {

            System.out.println(Integer.toHexString(b));
        }

    }
}
