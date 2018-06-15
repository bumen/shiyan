package com.bmn.rt.util;

import java.lang.ref.PhantomReference;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/4/19.
 */
public class Collection {
    public static void main(String[] args) {
        Queue<Integer> concurrentLinkedQueue = new QvpConcurrentLinkedQueue<>();
        concurrentLinkedQueue.offer(3);
        concurrentLinkedQueue.offer(3);

        concurrentLinkedQueue.poll();
        concurrentLinkedQueue.poll();

        Deque<Integer> arrayDeque = new ArrayDeque<>(3);
        arrayDeque.addFirst(3);
        arrayDeque.addFirst(4);
        arrayDeque.addFirst(5);
        arrayDeque.addFirst(6);
        arrayDeque.addFirst(7);
        arrayDeque.addFirst(8);
        arrayDeque.addFirst(9);
        arrayDeque.addFirst(10);

        arrayDeque.addFirst(133);
        arrayDeque.add(11);
        arrayDeque.addLast(7);

        arrayDeque.getFirst();
        arrayDeque.getLast();
    }

    private static void list() {

        /**
         * 1. 看存储对象的数据结构
         * 2. 看插入（存的过程）
         * 3. 看查找（查找算法）
         * 4. 看删除
         * 5. 看修改
         * 6. 初始值
         */
        //就是数组，无注意
        List<Integer> arrayList = new ArrayList<>();

        //是  prev(null) <--first <---> second <---> last ---> next(null) --前后是完的双端链表
        List<Integer> linkList = new LinkedList<>();

        //volatile 数组-与锁的结合。
        //get局部数组，写入成功后，复制数组， 再set
        List<Integer> cowList = new CopyOnWriteArrayList<>();
    }

    private static void queue() {
        //固定大小，一把锁
        Queue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<Integer>(3);

        /**
         * 没有锁， 通过volatile与原子更新实现
         * 1. head  tail 都是volatile
         * 2. 插入时，查找尾结点(特点是next为null， 如果不为null说明其它线程已经修改了尾结点，则重新获取尾结点）。如果找到尾结点，插入next，同时更新尾结点。
         * 3. 取出时，查找头结点(特点烛item为null， 如果已为null说明其它线程已经修改了头结点，则重新获取头结点）。如果找到头结点，更新item，同时更新头结点。
         */
        Queue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

        //两把锁
        Queue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();


        Queue<Integer> SynchronousQeueu = new SynchronousQueue<>();
    }
    private static void deque() {
        //head指针，从后往前走。
        //tail指针，从前往后走。
        //扩容后，head又指向0，tail指向尾。再按顺序走
        //初始化时，head与tail都是0。
        Deque<Integer> arrayDeque = new ArrayDeque<>();

        //一把锁
        //有前后节点
        Deque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
    }

    private static void map() {

        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        Map<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        Map<Integer, Integer> weakHashMap = new WeakHashMap<>();
    }

    private static void set() {
        Set<Integer> concurrentSkipListSet = new ConcurrentSkipListSet<>();
        Set<Integer> cowSet = new CopyOnWriteArraySet<>();
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
    }

    private static void unknow() {
        Map<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        Queue<Integer> SynchronousQeueu = new SynchronousQueue<>();
    }

}
