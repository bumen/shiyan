package com.bmn.ddd.action.interpreter;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Context context = new Context();

        Variable a = new Variable("a");

        context.assign(a, false);

        Variable b = new Variable("b");

        context.assign(b, true);

        Constant c = new Constant(true);

        Expression exp = new Or(new And(c, a), new And(b, new Not(a)));

        System.out.println(exp.interpret(context));
    }
}
