package com.bmn.ddd.action.memento;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Originator {

    private String statue;

    public Memento createMemento() {
        return new Memento(statue);
    }

    public void restor(Memento memento) {
        this.statue = memento.getStatue();
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }
}
