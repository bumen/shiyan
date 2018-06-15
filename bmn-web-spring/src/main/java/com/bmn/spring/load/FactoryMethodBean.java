package com.bmn.spring.load;

/**
 * Created by Administrator on 2017/8/11.
 */
public class FactoryMethodBean {
    private String name;

    private FactoryMethodBean(){}


    public static FactoryMethodBean create() {
        return new FactoryMethodBean();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
