package com.bmn.spring.boot.context.properties;

import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName.Form;

/**
 * @author: zyq
 * @date: 2018/12/22
 */
public class ConfigurationPropertyNameTest {
    private static ConfigurationPropertyName testOf(String s) {
        ConfigurationPropertyName name = ConfigurationPropertyName.of(s);
        return testOf(name);
    }

    private static ConfigurationPropertyName testOf(ConfigurationPropertyName name) {

        int c = name.getNumberOfElements();
        for(int i = 0; i < c; i++) {
            System.out.print(name.getElement(i, Form.ORIGINAL));
            System.out.print("[ORIGINAL], ");
        }
        System.out.println();
        for(int i = 0; i < c; i++) {
            System.out.print(name.getElement(i, Form.DASHED));
            System.out.print("[DASHED], ");
        }
        System.out.println();
        for(int i = 0; i < c; i++) {
            System.out.print(name.getElement(i, Form.UNIFORM));
            System.out.print("[UNIFORM], ");
        }
        System.out.println();
        System.out.println();

        return name;
    }

    public static void main(String[] args) {

        String s = "";
        s = "log.tip";
        testOf(s);
        s = "log.tip-zyq";
        testOf(s);
        s = "log.[1]";
        testOf(s);
        s = "log.[name]";
        testOf(s);
        s = "log[1]";
        testOf(s);
        s = "log[name]";
        testOf(s);
        s = "log[name.age]";
        testOf(s);
        s = "log.[name_age]";
        testOf(s);
        s = "log.[name+age]";
        testOf(s);
        s = "log.[name[age]]";
        testOf(s);

        testOf(testAppend(testOf(s), "sex"));

        testOf(testAppend(testOf(s), "[1]"));

        testOf(testAppend(testOf(s), "[name]"));

        testOf(testAppend(testOf(s), "sex-zh"));

        testOf(testAppend(testOf(s), "[sex-zh]"));

        testOf(testChop(testOf(s), 1));

        // 非法
        s = "log.tip_zyq";
        testOf(s);
        s = "-log.name";
        testOf(s);
        s = "log[";
        testOf(s);

        testOf(testAppend(testOf(s), "sex.zh[3]"));



        // log.name
        // log.name-zyq
        // 切分 log, name-zyq  --DASHED
        // name---zyq 好像也可以

        // log.name-zyq.age
        // log.[1]
        // 切分后为 log -- UNIFORM, [1] -- NUMERICALLY_INDEXED

        // log.[name]
        // 切分后为log, [name] -- INDEXED

        // log[1] 只会过滤到[1]
        // log[name]只会过滤到[name]
        // ----log.name 如果开头是非法字符，则会过滤掉非法字符。取出log --NON_UNIFORM , name -- UNIFORM
        // [1] --
        // [name]
        // 其它都是：NON_UNIFORM

        //只有括号分隔，与点分隔
        //分隔后，字符串组成：数字，小写字母，-，且-不能开头

    }

    public static ConfigurationPropertyName testAppend(ConfigurationPropertyName name, String v) {
        return name.append(v);
    }

    public static ConfigurationPropertyName testChop(ConfigurationPropertyName name, int v) {
        return name.chop(v);
    }
}
