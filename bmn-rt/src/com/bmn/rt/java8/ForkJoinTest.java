package com.bmn.rt.java8;

import com.bmn.rt.java8.domain.CacheDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/7/2
 */
public class ForkJoinTest {

    private Map<Integer, CacheDomain> cacheDomainMap = new ConcurrentHashMap<>();

    public void start() {

        cacheDomainMap.values().parallelStream().filter(cache -> {
            System.out.println(Thread.currentThread().getName() + "-------filter------"+ cache.id);

            return cache.id % 2 != 0;})
            .forEach(cache -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "-------------"+ cache.id);
            });


    }


    public void produce() {
        int i = 100;
        while (i > 0) {
            i--;
            CacheDomain cache = new CacheDomain();
            cache.id = i;
            cacheDomainMap.put(i, cache);
        }
    }

    public static void main(String[] args) {
        ForkJoinTest test = new ForkJoinTest();
        test.produce();
        test.start();
    }
}
