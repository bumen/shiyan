package com.bmn.ddd.action.strategy;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Context {

    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void call(int a, int b) {
        this.strategy.calc(a, b);
    }

    public static void main(String[] args) {
        Context context = new Context();

        context.setStrategy(new PlusStrategy());

        context.call(1, 2);

        context.setStrategy(new MinusStrategy());

        context.call(1, 2);
    }
}
