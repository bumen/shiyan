package com.bmn.ddd.structure.compose.transparent;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public interface Component {

    void add(Component component);

    void remove(Component component);

    void operation();
}
