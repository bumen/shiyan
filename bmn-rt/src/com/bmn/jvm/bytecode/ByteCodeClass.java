package com.bmn.jvm.bytecode;

import com.bmn.rt.lang.reflect.QvpAnnotation;
import javax.print.PrintException;

public class ByteCodeClass {


    public void useManyNumberic() {
        int i = 100;
        int j = 1000000;
        long l1 = 1;
        long l2 = 0xffffffff;
        double d = 2.2;
        float f = 3.0f;

        Long ll = l1;

        long lll = ll;
    }

    int lessThan100(double d) {
        if(d < 100.0) {
            return 1;
        } else {
            return -1;
        }
    }

    void throwOne(int i) {
        if(i == 0) {
            throw new NullPointerException();
        }
    }

    void catchOne() {
        try {
            throwOne(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    void tryFinally() {
        try {
            throwOne(0);

        } finally {
            System.out.println("");
        }
    }

    void tryCatchFinally() {
        try {
            throwOne(0);
        } catch (NullPointerException e) {
            System.out.println("23");
        } finally {
            System.out.println("");
        }
    }

    void tryCatchFinallyWhile() {
        while(true) {
            try {
                throwOne(0);
            } finally {
                System.out.println("finally");
            }
        }
    }

    boolean tryCatchFinallyFor() {


        for(int i = 0 ; i < 3; i++) {
            try{
                throwOne(0);
            } finally {
                System.out.println("finally");
            }
        }
        return true;
    }

    @QvpAnnotation("asdf")
    void  onlySynchroinzedMe(Object o) {
        synchronized (o) {
            tryCatchFinallyWhile();
        }
    }

    public static void main(String[] args) {
        ByteCodeClass byteCodeClass = new ByteCodeClass();
        byteCodeClass.tryCatchFinallyFor();
    }



    public class In {

    }
}
