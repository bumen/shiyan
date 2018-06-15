package com.bmn.bean;

public class TreeBean implements TreeInterface{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void say() {

    }


    private void parentOwn(){}
}
