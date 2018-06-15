package com.bmn.rt.util;

import java.io.ObjectStreamClass;
import java.util.*;
import java.util.Collection;

/**
 * Created by Administrator on 2017/9/20.
 */
public class Test {
    public static void main(String[] args) {
        int head = 0;
        int v = (head - 1) & (8 - 1);
        System.out.println(v);

        System.out.println(Integer.toBinaryString(head - 1));


        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);

        List<Integer> list2 = (List<Integer>) list.clone();
        list.remove(0);
        list.remove(3);
        list2.removeAll(list);


        System.out.println(Integer.toBinaryString(1 >> 6));

        String d = new String("3");
        System.out.println(System.identityHashCode("3"));
        System.out.println(System.identityHashCode("3"));
        System.out.println(System.identityHashCode(d));
    }



    private void interfaces() {
        Collection collection;
        Comparator comparator;
        Deque deque;
        Enumeration enumeration;

        EventListener eventListener;

        Formattable formattable;
        Iterator iterator;

        List list;

        ListIterator listIterator;

        Map map;

        NavigableMap navigableMap;

        NavigableSet navigableSet;

        Observer observer;

        PrimitiveIterator primitiveIterator;

        Queue queue;

        RandomAccess randomAccess;

        Set set;

        SortedMap sortedMap;

        SortedSet sortedSet;
    }

    public void classes() {
        AbstractCollection abstractCollection;

        AbstractList abstractList;

        AbstractMap abstractMap;

        AbstractQueue abstractQueue;

        AbstractSequentialList abstractSequentialList;

        AbstractSet abstractSet;

        ArrayDeque arrayDeque;

        ArrayList arrayList;

        Arrays arrays;

        Base64 base64;

        BitSet bitSet;

        Calendar calendar;

        Collections collections;


        Currency currency;

        Dictionary dictionary;

        DoubleSummaryStatistics doubleSummaryStatistics;

        EnumMap enumMap;

        EnumSet enumSet;

        EventListenerProxy eventListenerProxy;

        EventObject eventObject;

        FormattableFlags formattableFlags;

        Formatter formatter;

        GregorianCalendar gregorianCalendar;

        HashMap hashMap;

        HashSet hashSet;

        Hashtable hashtable;

        IdentityHashMap identityHashMap;

        LinkedHashMap linkedHashMap;

        LinkedHashSet linkedHashSet;

        LinkedList linkedList;

        ListResourceBundle listResourceBundle;

        Objects objects;

        Observable observable;

        Observer observer;

        PriorityQueue priorityQueue;

        Properties properties;

        PropertyResourceBundle propertyResourceBundle;

        Scanner scanner;

        Random random;

        ServiceLoader serviceLoader;

        SimpleTimeZone simpleTimeZone;

        Stack stack;

        StringTokenizer stringTokenizer;

        Timer timer;

        TimerTask timerTask;

        TreeMap treeMap;

        TreeSet treeSet;

        UUID uuid;

        WeakHashMap weakHashMap;
    }



}
