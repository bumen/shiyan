package com.bmn.jvm;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;

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

        //t2.start();

       //

    }

}
