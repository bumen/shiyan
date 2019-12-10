package com.bmn.ddd.action.interpreter;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Variable extends Expression {

    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    boolean interpret(Context context) {
        return context.lookup(this);
    }
}
