package com.bmn.socket.filter;

import java.util.LinkedList;

/**
 *
 * 在开发高并发系统时有三把利器用来保护系统：缓存、降级和限流
 *
 * 限流：计数器、漏桶和令牌桶算法
 *
 *
 * 计数器是最简单粗暴的算法。比如某个服务最多只能每秒钟处理100个请求。
 * 我们可以设置一个1秒钟的滑动窗口，窗口中有10个格子，每个格子100毫秒，每100毫秒移动一次，每次移动都需要记录当前服务请求的次数。
 * 内存中需要保存10次的次数。可以用数据结构LinkedList来实现。
 * 格子每次移动的时候判断一次，当前访问次数和LinkedList中最后一个相差是否超过100，如果超过就需要限流了。
 *
 * @date 2019-12-12
 * @author zhangyuqiang02@playcrab.com
 */
public class Counter {

    Long counter = 0L;

    LinkedList<Long> ll = new LinkedList<>();


    private void doCheck() throws InterruptedException {
        while (true) {
            ll.addLast(counter);

            // 1010 毫秒了， 开始滑动
            if (ll.size() > 10) {
                ll.removeFirst();
            }

            // 1秒内100个请求
            if ((ll.peekLast() - ll.peekFirst()) > 100L) {
                // 开始限流
            }

            Thread.sleep(100);
        }
    }

    private void request() {
        counter++;
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        counter.doCheck();
    }
}
