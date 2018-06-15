package com.bmn.spring.aop;

/**
 * Created by Administrator on 2017/5/12.
 */
public class UserManagerImpl implements UserManager {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String findUser(int userId) {
        System.out.println("-----------UserManagerImpl.findUser()---------");
        return "hello world";
    }
}
