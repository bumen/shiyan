package com.bmn.ddd.action.template;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Template template = new MinTemplate();

        System.out.println(template.calc());

        template = new MaxTemplate();

        System.out.println(template.calc());
    }
}
