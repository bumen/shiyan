package com.bmn.gm.vo;

/**
 * Created by Administrator on 2017/8/4.
 */
public class ComplexProperty {
    private int id;
    private String name;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.text = name;
    }
}
