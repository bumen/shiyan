package com.bmn.rt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/2.
 */
public class RegexTest {

    /**
     * ? 0-1
     * + 1-&
     * * 0-&
     * @param args
     */
    public static void main(String[] args) {
        //只匹配一次，且全部字符匹配
        boolean t = Pattern.matches("\\d{5,}", "123123");
        System.out.println(t);

        t = Pattern.matches("\\d{3,}", "123aa");
        System.out.println(t);

        Pattern p = Pattern.compile("\\d{3,}");
        Matcher m = p.matcher("123aa");
        t = m.lookingAt();
        System.out.println(t);

        Character character;

        String string="测试<>《》！*(^)$%~!@#$…&%￥—+=、。，；‘’“”：·`  \r\n  文本";
        System.out.println(string.replaceAll("\\pP|\\pS|\\s", ""));
    }
}
