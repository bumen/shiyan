package com.bmn.gm.vo;

import java.util.List;

/**
 * 组合模式
 * Created by Administrator on 2017/8/1.
 */
public interface Component extends Cloneable {
    int getId();

    List<Component> getChildren();

    void setChildren(List<Component> component);

    Component clone() throws CloneNotSupportedException;

    void filter(ComponentFilter filter);

    interface ComponentFilter {
        void doFilter(List<Component> components);
    }
}
