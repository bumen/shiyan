package com.bmn.jvm;

/**
 * object: finalize 方法
 *  1. 只会执行一次
 *  2. 由虚拟机负责执行，执行时间不确定。
 *  3. 可以在GC时实现自我自救
 *
 */
public class FinalizeEscapeGC {

    public static FinalizeEscapeGC INSTNACE;

    public void isAlive() {
        System.out.println("yes, i'm still alive.");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapeGC.INSTNACE = this;
    }

    public static void main(String[] args) throws InterruptedException {
        INSTNACE = new FinalizeEscapeGC();
        INSTNACE = null;

        System.gc();

        Thread.sleep(500);

        if(INSTNACE != null) {
            INSTNACE.isAlive();
        } else {
            System.out.println("no, I'm dead :(");
        }

        INSTNACE = null;

        System.gc();

        // Finalizer 方法优先级很低，暂停0.5秒，以等待它执行
        Thread.sleep(500);

        if (INSTNACE != null) {
            INSTNACE.isAlive();
        } else {
            System.out.println("no, I'm dead :(");
        }
    }
}
