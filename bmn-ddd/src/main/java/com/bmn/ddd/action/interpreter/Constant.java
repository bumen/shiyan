package com.bmn.ddd.action.interpreter;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Constant extends Expression {

    private boolean v;

    public Constant(boolean v) {
        this.v = v;
    }


    @Override
    boolean interpret(Context context) {
        return this.v;
    }
}
