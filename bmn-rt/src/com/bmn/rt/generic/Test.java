package com.bmn.rt.generic;

import com.bmn.rt.generic.bean.ABean;
import com.bmn.rt.generic.bean.ConcreteConvert;
import com.bmn.rt.generic.bean.Convert;
import com.bmn.rt.generic.bean.Param;

/**
 * @author: zyq
 * @date: 2018/11/29
 */
public class Test {

    public void paramTest(Param<ABean> param) {
        Convert c = new ConcreteConvert();

        c.convert(param.getClass());
    }

    public static void main(String[] args) {
       Test t = new Test();
       t.paramTest(new Param<>());
    }

}
