package com.bmn.rt.vo;

/**
 * Created by Administrator on 2017/8/31.
 */
public class Child<T> extends Parent<T> {

    private int  id;
    protected int age;
    public String name;

    private void toChildPrivate() {

    }


    @Override
    protected void toParentProtected() {

    }

    public class InnerChild {
        noSureClass c = new noSureClass();
    }
}

class noSureClass {
    private int id;
}
