package com.bmn.ddd.action.memento;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Caretaker {

    private Memento memento;

    public void save(Memento memento) {
        this.memento = memento;
    }

    public Memento getMemento() {
        return this.memento;
    }
}
