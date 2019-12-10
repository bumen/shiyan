package com.bmn.ddd.action.iterator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcreteAggregate extends Aggregate {

    @Override
    public Iterator iterator() {
        return new ConcreteIterator(this);
    }
}
