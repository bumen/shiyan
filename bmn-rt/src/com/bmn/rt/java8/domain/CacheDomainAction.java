package com.bmn.rt.java8.domain;

import java.util.concurrent.RecursiveAction;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/7/2
 */
public class CacheDomainAction extends RecursiveAction {

    private final int count;

    public CacheDomainAction(int count) {
        this.count = count;
    }

    @Override
    protected void compute() {


    }
}
