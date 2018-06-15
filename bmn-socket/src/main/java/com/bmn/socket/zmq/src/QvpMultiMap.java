package com.bmn.socket.zmq.src;

import java.util.*;

/**
 * Created by Administrator on 2017/6/14.
 */
public class QvpMultiMap<K extends Comparable<? super K>, V>implements Map<K, V> {
    private long id;
    private final HashMap<Long, V> values;
    private final TreeMap<K, ArrayList<Long>> keys;

    public QvpMultiMap() {
        id = 0;
        values = new HashMap<>();
        keys = new TreeMap<>();
    }

    public class QvpMultiMapEntry implements Entry<K, V> {
        private K key;
        private V value;
        public QvpMultiMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }


    public class QvpMultiMapEntrySet implements Set<Entry<K, V>>, Iterator<Entry<K, V>> {
        private QvpMultiMap<K, V> map;
        private Iterator<Entry<K, ArrayList<Long>>> it;
        private Iterator<Long> iit;
        private K key;
        private long id;

        public QvpMultiMapEntrySet(QvpMultiMap<K, V> map)
        {
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            if(iit == null || !iit.hasNext()) {
                if(!it.hasNext()) {
                    return false;
                }

                Entry<K, ArrayList<Long>> item = it.next();
                key = item.getKey();
                iit = item.getValue().iterator();
            }
            return true;
        }

        @Override
        public Entry<K, V> next() {
            id = iit.next();
            return new QvpMultiMapEntry(key, map.values.get(id));
        }

        @Override
        public void remove() {
            iit.remove();
            map.values.remove(id);
            if (map.keys.get(key).isEmpty()) {
                it.remove();
            }
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            it = map.keys.entrySet().iterator();
            return this;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Entry<K, V> kvEntry) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Entry<K, V>> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }


    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return keys.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keys.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values.containsKey(value);
    }

    @Override
    public V get(Object key) {
        ArrayList<Long> l = keys.get(key);
        if(l == null) {
            return null;
        }

        return values.get(l.get(0));
    }

    @Override
    public V put(K key, V value) {
        ArrayList<Long> ids = keys.get(key);
        if (ids == null) {
            ids = new ArrayList<>();
            ids.add(id);
            keys.put(key, ids);
        } else {
            ids.add(id);
        }
        values.put(id, value);
        id++;
        return null;
    }

    @Override
    public V remove(Object key) {
        ArrayList<Long> l = keys.get(key);
        if(l == null) {
            return null;
        }
        V old = values.remove(l.remove(0));
        if (l.isEmpty()) {
            keys.remove(key);
        }
        return old;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Entry<? extends K, ? extends V> o : m.entrySet()) {
            put(o.getKey(), o.getValue());
        }
    }

    @Override
    public void clear() {
        keys.clear();
        values().clear();
    }

    @Override
    public Set<K> keySet() {
        return keys.keySet();
    }

    @Override
    public Collection<V> values() {
        return values.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new QvpMultiMapEntrySet(this);
    }
}
