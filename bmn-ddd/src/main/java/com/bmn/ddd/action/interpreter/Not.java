package com.bmn.ddd.action.interpreter;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Not extends Expression {

    private Expression expression;

    public Not(Expression expression) {
        this.expression = expression;
    }

    @Override
    boolean interpret(Context context) {
        return !expression.interpret(context);
    }
}
