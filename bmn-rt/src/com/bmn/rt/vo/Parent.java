package com.bmn.rt.vo;

/**
 * Created by Administrator on 2017/8/31.
 */
public class Parent<T> implements IPerson{

    public T clazz;
    private int parentId;
    protected int parentAge;
    public String parentName;


    public void toParentPublic() {

    }

    private void toParentPrivate() {

    }

    protected void toParentProtected() {

    }

    @Override
    public void savePerson(int id) {

    }

    public static class InnerParent{

    }
}
