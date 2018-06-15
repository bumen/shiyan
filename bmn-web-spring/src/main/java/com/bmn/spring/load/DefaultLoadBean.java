package com.bmn.spring.load;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
public abstract class DefaultLoadBean {
    private String name;
    private List<Integer> list;
    private RelationBean relationBean;

    private DefaultLoadBean(String name) {
        this.name = name;
    }

    public DefaultLoadBean(String name, List<Integer> list) {
        this.list = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public void setRelationBean(RelationBean relationBean) {
        this.relationBean = relationBean;
    }

    public void init() {

    }

    public void destroy() {

    }

    public void beReplaced(String name, RelationBean relationBean) {
        this.name = name;
        this.relationBean = relationBean;
        System.out.println("this is default load bean be replaced");
    }

    public void lookup() {
        getLookup().lookup();
    }

    public abstract LookupMethodSuper getLookup();
}
