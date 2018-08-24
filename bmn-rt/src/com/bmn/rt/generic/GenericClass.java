package com.bmn.rt.generic;

import com.bmn.rt.abs.TestA;
import com.bmn.rt.abs.TestI;
import com.bmn.rt.beans.TestC;
import com.bmn.rt.beans.TestMC;
import com.bmn.rt.generic.bean.ABean;
import com.bmn.rt.generic.bean.BBean;
import com.bmn.rt.generic.pti.APti;
import com.bmn.rt.generic.pti.APtiAbstract;
import com.bmn.rt.generic.pti.CommonPti;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class GenericClass {


    /**
     * 类型变量
     * 1. 每个类型变量都有一个上界，如果没有显示给出，默认是Object
     *   * 上界有两种
     *   1. 是其它类型变量如F
     *   2. 是类或接口类型(TestC, TestA)，后面可能还有接口类型I, &...& n
     *     + 类型变量擦除后取决于边界中的第一个类型；
     *     + 类类型或类型变量只能出现在第一个类型的位置
     * @param t
     * @param <T>
     */
    public static <F, E extends F, T extends TestC & TestA & TestI> void test(T t) {
        t.mI();
        t.mCPublic();
        t.mA();
    }


    public static void main(String[] args) {
        GenericClass.test(new TestMC());
    }


    public static void superT(){
        List<?> all = null;
        List<? extends Object> oAll = null;

        List<? super TestC> sclist = null;
        List<? super TestMC> smcList = null;

        smcList = sclist;
        all = sclist;
        oAll = sclist;


        List<? extends TestC> eclist = null;
        List<? extends TestMC> emcList = null;

        eclist = emcList;
        all = eclist;
        oAll = eclist;

        List<TestMC> mclist = null;
        List<TestC> clist = null;

        mclist = mclist;

        eclist = mclist;
        emcList = mclist;

        //sclist = mclist;, 不成立
        smcList = mclist;
        smcList = clist;

        TestMC mc = new TestMC();


        // all.add(mc);     //不可以，相当于 ? extends Object
        // oAll.add(mc);    //不可以
        // eclist.add(mc);  //不可以

        sclist.add(mc);
        mc = (TestMC) sclist.get(0);
        mc = (TestMC) smcList.get(0);

        mc = (TestMC) eclist.get(0);
        mc = emcList.get(0);

        mc = (TestMC) all.get(0);
        mc = (TestMC) oAll.get(0);

    }

    /**
     * 泛型上界, 只能取，不能放
     * 上界是相对于引用来说的：如list = aBeans, list = bBeans. CommonPti是ABean, BBean的上界
     * 引用传递只能是越传越大，即子类引用向父类引用传递。保证取出的数据不会出错
     * 为什么不能放
     * 1. list = aBeans时，此时引用为List<? extends CommonPti> 类型， 如果放BBean(), 则出错；
     * 为什么可以取, 上界相对于 ? extends T 和来说
     * 1. 因为list = aBeans, list = bBeans取出来都是CommonPti
     *
     * 结果上界就是为了解决多态会产生的类型不正确问题
     */
    public static void genericExtends() {
        List<? extends CommonPti> list = null;
        List<ABean> aBeans = null;
        List<BBean> bBeans = null;

        aBeans.add(new ABean());
        bBeans.add(new BBean());

        list = aBeans;
        list = bBeans;

        ABean aBean = aBeans.get(0);
        BBean bBean = bBeans.get(0);

        CommonPti commonPti = list.get(0);
        commonPti = aBeans.get(0);
        commonPti = bBeans.get(0);

        APti aPti = aBeans.get(0);
        APtiAbstract aPtiAbstract = aBeans.get(0);

        List<ABean> aaBeans = null;

        aPti = aaBeans.get(0);


    }

    /**
     * 泛型下界, 能放，能取
     * 下界是相对于引用来说的：如aBeans = list, bBeans = list. CommonPti, ABean, BBean指定界
     * 引用传递只能是越传越小，即父类引用向子类引用传递。保证放进去的数据不会出错。
     * 下界也相对于 super T， 相对于T来说，放的至少是T
     *
     * 为什么可以放
     * 1. aBeans = list, bBeans = list, list不管由谁引用，放进去的至少是CommonPti
     * 为什么可以取
     * 1. 如果按与上界对应关系，则是不可以取的
     * 2. 因为没有对取做限制，现在是可以取。也是很容易出错。只能确定类型，然后再强制转换
     */
    public static void genericSuper() {
        List<? super CommonPti> list = null;
        List<? super ABean> aBeans = null;
        List<? super BBean> bBeans = null;

        list.add(new ABean());
        list.add(new BBean());

        aBeans = list;
        bBeans = list;

        APtiAbstract aPtiAbstract = null;
        // aBeans.add(aPtiAbstract);

        bBeans.add(new BBean());

        CommonPti a = (CommonPti) aBeans.get(0);
        // System.out.println(m(12d, 2d));
    }

    static double m(float a, double b) {
        return a + b;
    }

    public static void re(List<?> list) {
        rei(list);
    }

    public static void ree(List<? extends CommonPti> list) {
        rei(list);
    }

    public static void res(List<? super CommonPti> list) {
        rei(list);
    }

   // static int x;
    public static <T> void rei(List<T> list) {
        int asdf = asdf = 20000;

        switch(1) {
            case 1:
                Object ix = 1110;
                break;
            case 2:
                ix = 11111;
                break;
            case 3:
                ix = 11112;
                break;
            case 4:
                ix = 111121234;
                break;
        }

        int iddd = 10123123;

    }
}
