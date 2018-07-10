package com.bmn.jvm;

/**
 * 栈异常 stackoverflowerror, 达到最大栈深度
 *
 * -verbose:gc -Xss128k
 *
 */
public class StackSOF {

    private int stacklength = 1;

    public void stackLeak() {
        stacklength++;
        stackLeak();
    }

    public static void main(String[] args) {
        StackSOF sof = new StackSOF();
        try {

        sof.stackLeak();
        }catch(Throwable t) {
            System.out.println("stack length: " + sof.stacklength);
            throw t;
        }

    }
}
