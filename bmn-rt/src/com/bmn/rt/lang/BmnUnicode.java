package com.bmn.rt.lang;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
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


        char high = Character.highSurrogate(smpCodePoint);
        char low = Character.lowSurrogate(smpCodePoint);

        System.out.println(high + "_" + low);

        System.out.println(Character.getType(smpCodePoint));

        System.out.println(Character.toChars(code));

        System.out.println(Charset.defaultCharset());


        Charset charset = StandardCharsets.UTF_8;
        CharsetEncoder encoder = charset.newEncoder();

        // encoder.encode();
    }

}
