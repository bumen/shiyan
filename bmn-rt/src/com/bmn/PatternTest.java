package com.bmn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: zyq
 * @date: 2018/12/26
 */
public class PatternTest {

    public static void main(String[] args) {
        String badRegex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+$";
        String bugUrl = "asd 放/N.title as 读/N.title 写/N.title http://www.fapiao.com/dddp-web/pdf/download?request=6e7JGxxxxx4ILd-kExxxxxxxqJ4-CHLmqVnenXC692m74H38sdfdsazxcUmfcOH2fAfY1Vw__%5EDadIfJgiEf asdfasdfasdfasdf";

        Pattern p = Pattern.compile(badRegex);
       /* Matcher m = p.matcher(bugUrl);
        if(m.matches()) {
            int c = m.groupCount();
            for(int i = 0; i < c ; i++) {
                System.out.println(m.group(i));
            }
        }*/

        test(bugUrl);

        backreference();

    }

    public static void test(String v ) {
        String pg = "(.*?([上下听放来播念读]/N.title)\\s*)+(.*/N.title)\\s*(第\\s)?(?<number>[0-9零一二两三四五六七八九十百千万]+)?.*";
         //pg = ".*(([上下听放来播念读]/N.title)\\s*)+(.*/N.title)\\s*(第\\s)?(?<number>[0-9零一二两三四五六七八九十百千万]+)?.*";

        Pattern p = Pattern.compile(pg);

        Matcher m = p.matcher(v);

        if(m.matches()) {
            int c = m.groupCount();
            for(int i = 0; i < c ; i++) {
                System.out.println(m.group(i));
            }
        } else {
            System.out.println("error");
        }


    }

    /**
     * 回溯引用，前后一致匹配
     *
     * 如果回溯，则这个表达试会，指数级回溯
     */
    public static void backreference(){
        String pg = "(.*?[上下听放来播念读]/N.title\\s*)++(.*?/N.title)\\s*(第\\s)?(?<number>[0-9零一二两三四五六七八九十百千万]+)?.*";
        // (.*?[上下听放来播念读]/N.title\s*)+ 少一个加号（表示进行回溯）
         pg = "(.*?[上下听放来播念读]/N.title\\s*)+(.*?/N.title)\\s*(第\\s)?(?<number>[0-9零一二两三四五六七八九十百千万]+)?.*";
        String v = "提 醒 我 明天/N.title 早 上/N.title 八点 去 上/N.title 课";

        Pattern p = Pattern.compile(pg);

        Matcher m = p.matcher(v);

        if(m.matches()) {
            int c = m.groupCount();
            for(int i = 0; i < c ; i++) {
                System.out.println(m.group(i));
            }
        } else {
            System.out.println("error");
        }
    }

}
