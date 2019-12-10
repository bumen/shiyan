package com.bmn.ddd.action.memento;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Originator originator = new Originator();
        originator.setStatue("hello");

        Caretaker caretaker = new Caretaker();

        caretaker.save(originator.createMemento());


    }

}
