package com.bmn.ddd.action.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Subject {

    private List<Observer> observers = new ArrayList<>();

    public void add(Observer observer) {
        this.observers.add(observer);
    }

    public void remove(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObserver(Object args) {
        for (Observer observer : observers) {
            observer.update(this, args);
        }
    }


}
