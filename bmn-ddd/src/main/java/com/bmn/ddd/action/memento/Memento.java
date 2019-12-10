package com.bmn.ddd.action.memento;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Memento {

    public Memento(String s) {
        this.statue = s;
    }

    private String statue;

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }
}
