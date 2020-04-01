package com.bmn.socket.filter;

/**
 *
 * 漏桶算法即leaky bucket是一种非常常用的限流算法，可以用来实现流量整形（Traffic Shaping）和流量控制（Traffic Policing）
 *
 * 漏桶算法的主要概念如下：
 *
 *
 *
 * 一个固定容量的漏桶，按照常量固定速率流出水滴；
 *
 * 如果桶是空的，则不需流出水滴；
 *
 * 可以以任意速率流入水滴到漏桶；
 *
 * 如果流入水滴超出了桶的容量，则流入的水滴溢出了（被丢弃），而漏桶容量是不变的。
 * @date 2019-12-12
 * @author zhangyuqiang02@playcrab.com
 */
public class LeakyBucket {


}
