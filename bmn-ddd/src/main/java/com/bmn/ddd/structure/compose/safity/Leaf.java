package com.bmn.ddd.structure.compose.safity;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Leaf implements Component {

    @Override
    public void opr() {
        System.out.println("this is leaf");
    }
}
