package com.bmn.objsize;

import java.lang.instrument.Instrumentation;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/7/23
 */
public class ObjectShallowSize {
    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation instP){
        inst = instP;
    }

    public static long sizeOf(Object obj){
        return inst.getObjectSize(obj);
    }

}
