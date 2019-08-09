package com.bmn.rt.lang.concurrent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/7/23
 */
public class ConcurrentSkipListSetRankTest {

    private static ConcurrentSkipListSet<UserInfo> sortSet = new ConcurrentSkipListSet<>();

    public static void main(String[] args) {

        for (int i = 0; i < 1; i++) {
            sortSet.clear();
            test();
        }
    }

    public static void test() {
        UserInfo info1 = new UserInfo();
        info1.setCreateTime(1001);
        info1.setExp(100);
        info1.setLevel(8);
        info1.setUserId("123");

        UserInfo info2 = new UserInfo();
        info2.setCreateTime(1002);
        info2.setExp(100);
        info2.setLevel(9);
        info2.setUserId("456");

        UserInfo info3 = new UserInfo();
        info3.setCreateTime(1003);
        info3.setExp(100);
        info3.setLevel(10);
        info3.setUserId("789");

        System.out.println("add 123");
        sortSet.add(info1);
        System.out.println("add 456");
        sortSet.add(info2);
        System.out.println("add 789");
        sortSet.add(info3);

        sortSet.remove(info1);

        Iterator<UserInfo> it = sortSet.iterator();
        while (it.hasNext()) {
            UserInfo user = it.next();

            System.out.println(user.getUserId() + "-rank:" + user.getRank());
        }

    }
}
