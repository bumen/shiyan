package com.bmn.jvm;

public class RunMain {

    public static void main(String[] args) {
        Runnable script = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "start");
                CInitDeadLoopClass loop = new CInitDeadLoopClass();
                System.out.println(Thread.currentThread() + "run over");
            }
        };

        Thread t1 = new Thread(script);

        Thread t2 = new Thread(script);

        t1.start();
        t2.start();
    }

}
