package com.bmn.rt.lang;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;
import javax.sound.midi.Soundbank;

public class BmnUnicode {

    public static void main(String[] args) {
        String str = "你好";
        int code = Character.codePointAt(str, 0);
        System.out.println(code);

        System.out.println(Character.isBmpCodePoint(code));

        int smpCodePoint = 0x12367;

        boolean t = Character.isSupplementaryCodePoint(smpCodePoint);
        System.out.println(t);

        int c = Character.charCount(smpCodePoint);
        System.out.println(c);

        ThreadPoolExecutor x= null;

        StringBuilder sb = new StringBuilder("      ");
        sb.append(1);


        char high = Character.highSurrogate(smpCodePoint);
        char low = Character.lowSurrogate(smpCodePoint);

        System.out.println(high + "_" + low);

        System.out.println(Character.getType(smpCodePoint));

        System.out.println(Character.toChars(code));

        System.out.println(Charset.defaultCharset());


        Charset charset = StandardCharsets.UTF_8;
        CharsetEncoder encoder = charset.newEncoder();

        // encoder.encode();

        System.out.println(stringSize(11));
    }

    public   static int stringSize(long x) {
        long p = 10;
        for (int i=1; i<19; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 19;
    }

}
