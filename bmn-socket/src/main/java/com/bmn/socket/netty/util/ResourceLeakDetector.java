package com.bmn.socket.netty.util;

import static com.bmn.socket.netty.util.internal.StringUtil.NEWLINE;
import static com.bmn.socket.netty.util.internal.StringUtil.simpleClassName;

import io.netty.util.internal.MathUtil;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/2/21.
 */
public class ResourceLeakDetector<T> {
    private static final Level DEFAULT_LEVEL = Level.SIMPLE;
    private static final int DEFAULT_MAX_RECORDS = 4;
    private static final int MAX_RECORDS;

    public enum Level {
        DISABLED,
        SIMPLE,
        ADVANCED,
        PARANOID
    }

    private static Level level;

    private static final Logger logger = LoggerFactory.getLogger(ResourceLeakDetector.class);

    static {
        final boolean disabled = false;
        Level defaultLevel = disabled ? Level.DISABLED : DEFAULT_LEVEL;
        MAX_RECORDS = DEFAULT_MAX_RECORDS;
        ResourceLeakDetector.level = defaultLevel;
    }

    static final int DEFAULT_SAMPLING_INTERVAL = 128;


    public boolean isEnabled() {
       return getLevel().ordinal() > Level.DISABLED.ordinal();
    }

    public static void setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        ResourceLeakDetector.level = level;
    }

    public static Level getLevel() {
        return level;
    }

    private final DefaultResourceLeak head = new DefaultResourceLeak(null);
    private final DefaultResourceLeak tail = new DefaultResourceLeak(null);
    private final ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
    private final ConcurrentMap<String, Boolean> reportedLeaks = PlatformDependent.newConcurrentHashMap();

    private final String resourceType;
    private final int samplingInterval;
    private final int mask;
    private final long maxActive;
    private long active;
    private final AtomicBoolean loggedTooManyActive = new AtomicBoolean();
    private long leakCheckCnt;

    public ResourceLeakDetector(Class<?> resourceType, int samplingInterval, long maxActive) {
        this(simpleClassName(resourceType), samplingInterval, maxActive);
    }

    public ResourceLeakDetector(String resourceType, int samplingInterval, long maxActive) {
        if (resourceType == null) {
            throw new NullPointerException("resourceType");
        }
        if (samplingInterval <= 0) {
            throw new IllegalArgumentException("samplingInterval: " + samplingInterval + " (expected: 1+)");
        }
        if (maxActive <= 0) {
            throw new IllegalArgumentException("maxActive: " + maxActive + "(expected: 1+)");
        }
        this.resourceType = resourceType;
        this.samplingInterval = MathUtil.findNextPositivePowerOfTwo(samplingInterval);
        this.mask = samplingInterval - 1;
        this.maxActive = maxActive;

        head.next = tail;
        tail.prev = head;
    }

    public final ResourceLeak open(T obj) {
        Level level = ResourceLeakDetector.level;
        if (level == Level.DISABLED) {
            return null;
        }
        if (level.ordinal() < Level.PARANOID.ordinal()) {
            if ((leakCheckCnt ++ & mask) == 0) {
                reportLeak(level);
                return new DefaultResourceLeak(obj);
            } else {
                return null;
            }
        } else {
            reportLeak(level);
            return new DefaultResourceLeak(obj);
        }
    }

    private void reportLeak(Level level) {
        if (!logger.isErrorEnabled()) {
            for (; ; ) {
                DefaultResourceLeak ref = (DefaultResourceLeak) refQueue.poll();
                if (ref == null) {
                    break;
                }
                ref.close();
            }
            return;
        }

        int samplingInterval = level == Level.PARANOID ? 1 : this.samplingInterval;
        if (active * samplingInterval > maxActive && loggedTooManyActive.compareAndSet(false, true)) {
            reportInstancesLeak(resourceType);
        }

        for(;;) {
            DefaultResourceLeak ref = (DefaultResourceLeak) refQueue.poll();
            if (ref == null) {
                break;
            }
            ref.clear();

            if (!ref.close()) {
                continue;
            }

            String records = ref.toString();
            if(reportedLeaks.putIfAbsent(records, Boolean.TRUE) == null) {
                if (records.isEmpty()) {
                    reportUntrackLeak(resourceType);
                } else {
                    reportTracedLeak(resourceType, records);
                }
            }
        }
    }

    protected void reportTracedLeak(String resourceType, String records) {
        logger.error(
                "LEAK: {}.release() was not called before it's garbage-collected. "
                + "See http://netty.io/wiki/reference-counted-objects.html for more information.{}",
                resourceType, records
        );
    }

    protected void reportUntrackLeak(String resourceType) {
        logger.error("LEAK: {}.release() was not called before it's garbage-collected. " +
                        "Enable advanced leak reporting to find out where the leak occurred. " +
                        "To enable advanced leak reporing, " +
                        "specify the JVM option '-D{}={}' or call {}.setLevel() " +
                        "See xxx information. ",
                        resourceType, "io.netty.leakDetection.level", Level.ADVANCED.name().toLowerCase(), simpleClassName(this)
        );
    }

    protected void reportInstancesLeak(String resourceType) {
        logger.error("LEAK: You are creating too many " + resourceType + " instance . " +
                resourceType + " is a shared resource that must be reused across the JVM, " +
                "so that only a few instances are created");
    }

    private final class DefaultResourceLeak extends PhantomReference<Object> implements ResourceLeak {
        private final String creationRecord;
        private final Deque<String> lastRecords = new ArrayDeque<>();
        private final AtomicBoolean freed;
        private DefaultResourceLeak prev;
        private DefaultResourceLeak next;
        private int removeRecords;

        DefaultResourceLeak(Object referent) {
            super(referent, referent != null ? refQueue : null);

            if (referent != null) {
                Level level = getLevel();
                if (level.ordinal() > Level.ADVANCED.ordinal()) {
                    creationRecord = newRecord(null, 3);
                } else {
                    creationRecord = null;
                }

                synchronized (head) {
                    prev = head;
                    next = head.next;
                    head.next.prev = this;
                    head.next = this;
                }
                freed = new AtomicBoolean();
            } else {
                creationRecord = null;
                freed = new AtomicBoolean(true);
            }
        }

        @Override
        public void record() {
            record0(null, 3);
        }

        @Override
        public void record(Object hint) {
            record0(hint, 3);
        }

        private void record0(Object hint, int recordsToSkip) {
            if (creationRecord != null) {
                String value = newRecord(hint, recordsToSkip);

                synchronized (lastRecords) {
                    int size = lastRecords.size();
                    if (size == 0 || !lastRecords.getLast().equals(value)) {
                        lastRecords.add(value);
                    }
                    if (size > MAX_RECORDS) {
                        lastRecords.removeFirst();
                        ++removeRecords;
                    }
                }
            }
        }

        @Override
        public boolean close() {
            if (freed.compareAndSet(false, true)) {
                synchronized (head) {
                    active--;
                    prev.next = next;
                    next.prev = prev;
                    prev = null;
                    next = null;
                }
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            if (creationRecord == null) {
                return "";
            }
            final Object[] array;
            final int removedRecords;
            synchronized (lastRecords) {
                array = lastRecords.toArray();
                removedRecords = this.removeRecords;
            }

            StringBuilder buf = new StringBuilder(16384).append(NEWLINE);
            if(removedRecords > 0) {
                buf.append("WARNING: ")
                        .append(removedRecords)
                        .append(" leak records were discarded because the leak record count is limited to ")
                        .append(MAX_RECORDS)
                        .append(". Use system property ")
                        .append("io.netty.leakDetection.maxRecords")
                        .append(" to increase the limit.")
                        .append(NEWLINE);

            }
            buf.append("Recent access records: ")
                    .append(array.length)
                    .append(NEWLINE);

            if (array.length > 0) {
                for (int i = array.length - 1; i >= 0; i--) {
                    buf.append('#')
                            .append(i + 1)
                            .append(':')
                            .append(NEWLINE)
                            .append(array[i]);
                }
            }

            buf.append("Created at:")
                    .append(NEWLINE)
                    .append(creationRecord);
            buf.setLength(buf.length() - NEWLINE.length());
            return buf.toString();
        }
    }

    private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS = {
            "com.qvp.util.ReferenceCountUtil.touch(",
            "com.qvp.buffer.AdvancedLeakAwareByteBuf.touch(",
            "com.qvp.buffer.AbstractByteBufAllocator.toLeakAwareBuffer(",
            "com.qvp.buffer.AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation("
    };

    static String newRecord(Object hint, int recordsToSkip) {
        StringBuilder buf = new StringBuilder(4096);
        if (hint != null) {
            buf.append("\tHint: ");
            if (hint instanceof ResourceLeakHint) {
                buf.append(((ResourceLeakHint) hint).toHintString());
            } else {
                buf.append(hint);
            }
            buf.append(NEWLINE);
        }

        StackTraceElement[] array = new Throwable().getStackTrace();
        for (StackTraceElement e : array) {
            if (recordsToSkip > 0) {
                recordsToSkip--;
            } else {
                String estr = e.toString();

                boolean excluded = false;
                for (String exculsion : STACK_TRACE_ELEMENT_EXCLUSIONS) {
                    if (estr.startsWith(exculsion)) {
                        excluded = true;
                        break;
                    }
                }

                if (!excluded) {
                    buf.append("\t");
                    buf.append(estr);
                    buf.append(NEWLINE);
                }
            }
        }
        return buf.toString();
    }
}
