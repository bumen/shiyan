package com.bmn.socket.netty.util;


import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by Administrator on 2017/2/16.
 */
public class DefaultAttributeMap implements AttributeMap {

    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater;

    static {
        AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> referenceFieldUpdate =
               null;// PlatformDependent.newAtomicReferenceFieldUpdater(DefaultAttributeMap.class, "attributes");
        if(referenceFieldUpdate == null) {
            referenceFieldUpdate = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
        }
        updater = referenceFieldUpdate;
    }

    private static final int BUCKET_SIZE = 4;
    private static final int MASK = BUCKET_SIZE - 1;

    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;

    @Override
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        if(key == null) {
            throw new NullPointerException("key");
        }

        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            attributes = new AtomicReferenceArray<DefaultAttribute<?>>(BUCKET_SIZE);
            if (!updater.compareAndSet(this, null, attributes)) {
                attributes = this.attributes;
            }
        }

        int i = index(key);

        DefaultAttribute<?> head = attributes.get(i);
        if(head == null) {
            head = new DefaultAttribute<>();
            DefaultAttribute<T> attr = new DefaultAttribute<>(head, key);
            head.next = attr;
            attr.prev = head;

            if (attributes.compareAndSet(i, null, head)) {
                return attr;
            } else {
                head = attributes.get(i);
            }
        }

        synchronized (head) {
            DefaultAttribute<?> curr = head;
            for(;;) {
                DefaultAttribute<?> next = curr.next;
                if(next == null) {
                    DefaultAttribute<T> attr = new DefaultAttribute<>(head, key);
                    curr.next = attr;
                    attr.prev = curr;
                    return attr;
                }

                if(next.key == key && !next.removed) {
                    return (Attribute<T>) next;
                }
                curr = next;
            }
        }
    }

    @Override
    public <T> boolean hasAttr(AttributeKey<T> key) {
        if(key == null) {
            throw new NullPointerException("key");
        }

        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            return false;
        }

        int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            return false;
        }

        synchronized (head) {
            DefaultAttribute<?> curr = head.next;
            while (curr != null) {
                if (curr.key == key && !curr.removed) {
                    return true;
                }
                curr = curr.next;
            }
            return false;
        }
    }

    private static int index(AttributeKey<?> key) {
        return key.id() & MASK;
    }

    private static final class DefaultAttribute<T> extends AtomicReference<T> implements Attribute<T> {

        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;

        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;

        private volatile boolean removed;

        DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key) {
            this.head = head;
            this.key = key;
        }

        DefaultAttribute() {
            head = this;
            key = null;
        }

        @Override
        public AttributeKey<T> key() {
            return key;
        }

        @Override
        public T setIfAbsent(T value) {
            while (!compareAndSet(null, value)) {
                T old = get();
                if (old != null) {
                    return old;
                }
            }
            return null;
        }

        @Override
        public T getAndRemove() {
            removed = true;
            T oldValue = getAndSet(null);
            remove0();
            return oldValue;
        }

        @Override
        public void remove() {
            removed = true;
            set(null);
            remove0();
        }

        private void remove0() {
            synchronized (head) {
                if(prev == null) {
                    return;
                }

                prev.next = next;
                if(next != null) {
                    next.prev = prev;
                }

                prev = null;
                next = null;
            }
        }
    }
}
