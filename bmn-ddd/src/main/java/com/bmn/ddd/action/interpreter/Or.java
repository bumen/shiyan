package com.bmn.ddd.action.interpreter;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Or extends Expression {

    private Expression left, right;

    public Or(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    boolean interpret(Context context) {
        return left.interpret(context) || right.interpret(context);
    }
}
