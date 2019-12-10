package com.bmn.ddd.action.iterator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcreteIterator implements Iterator {

    private ConcreteAggregate aggregate;

    public ConcreteIterator(ConcreteAggregate aggregate) {
        this.aggregate = aggregate;
    }
}
