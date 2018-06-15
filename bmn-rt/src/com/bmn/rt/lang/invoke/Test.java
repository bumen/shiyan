package com.bmn.rt.lang.invoke;

import com.bmn.bean.LeafBean;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created by Administrator on 2017/9/7.
 */
public class Test {
    public static void main(String[] args) throws Throwable {
        MethodHandleInfo methodHandleInfo;

        MethodHandle methodHandle;


        CallSite callSite;

        int i = 0_100_0;

        System.out.println(i);

        System.out.println(Integer.toOctalString(64*8));

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        MethodType type = MethodType.methodType(String.class, int.class, int.class);

        MethodHandle handle = lookup.findVirtual(String.class, "substring", type);

        String str = (String) handle.invokeExact("Hello World", 0, 5);
        System.out.println(str);

        //handle = lookup.findVirtual()

        LeafBean leaf = new LeafBean();

        handle = lookup.findSetter(LeafBean.class, "color", String.class);
        handle.bindTo(leaf);
        handle.invoke("xxx");

        System.out.println(leaf.getColor());
    }
}
