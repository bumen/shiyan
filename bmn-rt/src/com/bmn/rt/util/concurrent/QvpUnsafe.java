package com.bmn.rt.util.concurrent;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import sun.reflect.Reflection;

/**
 * Created by Administrator on 2017/11/28.
 */
public class QvpUnsafe {

        static
        {
            Reflection.registerMethodsToFilter(QvpUnsafe.class, "getUnsafe");
        }

        private static final QvpUnsafe theUnsafe = new QvpUnsafe();
        public static final int INVALID_FIELD_OFFSET = -1;

        @CallerSensitive
        public static QvpUnsafe getUnsafe()
        {
            return theUnsafe;
        }

        public void setMemory(long paramLong1, long paramLong2, byte paramByte)
        {
            setMemory(null, paramLong1, paramLong2, paramByte);
        }

        public void copyMemory(long paramLong1, long paramLong2, long paramLong3)
        {
            copyMemory(null, paramLong1, null, paramLong2, paramLong3);
        }


        public static final int ARRAY_BOOLEAN_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(boolean[].class);
        public static final int ARRAY_BYTE_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(byte[].class);
        public static final int ARRAY_SHORT_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(short[].class);
        public static final int ARRAY_CHAR_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(char[].class);
        public static final int ARRAY_INT_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(int[].class);
        public static final int ARRAY_LONG_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(long[].class);
        public static final int ARRAY_FLOAT_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(float[].class);
        public static final int ARRAY_DOUBLE_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(double[].class);
        public static final int ARRAY_OBJECT_BASE_OFFSET = theUnsafe
                .arrayBaseOffset(Object[].class);
        public static final int ARRAY_BOOLEAN_INDEX_SCALE = theUnsafe
                .arrayIndexScale(boolean[].class);
        public static final int ARRAY_BYTE_INDEX_SCALE = theUnsafe
                .arrayIndexScale(byte[].class);
        public static final int ARRAY_SHORT_INDEX_SCALE = theUnsafe
                .arrayIndexScale(short[].class);
        public static final int ARRAY_CHAR_INDEX_SCALE = theUnsafe
                .arrayIndexScale(char[].class);
        public static final int ARRAY_INT_INDEX_SCALE = theUnsafe
                .arrayIndexScale(int[].class);
        public static final int ARRAY_LONG_INDEX_SCALE = theUnsafe
                .arrayIndexScale(long[].class);
        public static final int ARRAY_FLOAT_INDEX_SCALE = theUnsafe
                .arrayIndexScale(float[].class);
        public static final int ARRAY_DOUBLE_INDEX_SCALE = theUnsafe
                .arrayIndexScale(double[].class);
        public static final int ARRAY_OBJECT_INDEX_SCALE = theUnsafe
                .arrayIndexScale(Object[].class);
        public static final int ADDRESS_SIZE = theUnsafe.addressSize();

        public final int getAndAddInt(Object paramObject, long paramLong, int paramInt)
        {
            int i;
            do
            {
                i = getIntVolatile(paramObject, paramLong);
            } while (!compareAndSwapInt(paramObject, paramLong, i, i + paramInt));
            return i;
        }

        public final long getAndAddLong(Object paramObject, long paramLong1, long paramLong2)
        {
            long l;
            do
            {
                l = getLongVolatile(paramObject, paramLong1);
            } while (!compareAndSwapLong(paramObject, paramLong1, l, l + paramLong2));
            return l;
        }

        public final int getAndSetInt(Object paramObject, long paramLong, int paramInt)
        {
            int i;
            do
            {
                i = getIntVolatile(paramObject, paramLong);
            } while (!compareAndSwapInt(paramObject, paramLong, i, paramInt));
            return i;
        }

        public final long getAndSetLong(Object paramObject, long paramLong1, long paramLong2)
        {
            long l;
            do
            {
                l = getLongVolatile(paramObject, paramLong1);
            } while (!compareAndSwapLong(paramObject, paramLong1, l, paramLong2));
            return l;
        }

        public final Object getAndSetObject(Object paramObject1, long paramLong, Object paramObject2)
        {
            Object localObject;
            do
            {
                localObject = getObjectVolatile(paramObject1, paramLong);
            } while (!compareAndSwapObject(paramObject1, paramLong, localObject, paramObject2));
            return localObject;
        }

        private static void throwIllegalAccessError()
        {
            throw new IllegalAccessError();
        }

        private static native void registerNatives();

        public native int getInt(Object paramObject, long paramLong);

        public native void putInt(Object paramObject, long paramLong, int paramInt);

        public native Object getObject(Object paramObject, long paramLong);

        public native void putObject(Object paramObject1, long paramLong, Object paramObject2);

        public native boolean getBoolean(Object paramObject, long paramLong);

        public native void putBoolean(Object paramObject, long paramLong, boolean paramBoolean);

        public native byte getByte(Object paramObject, long paramLong);

        public native void putByte(Object paramObject, long paramLong, byte paramByte);

        public native short getShort(Object paramObject, long paramLong);

        public native void putShort(Object paramObject, long paramLong, short paramShort);

        public native char getChar(Object paramObject, long paramLong);

        public native void putChar(Object paramObject, long paramLong, char paramChar);

        public native long getLong(Object paramObject, long paramLong);

        public native void putLong(Object paramObject, long paramLong1, long paramLong2);

        public native float getFloat(Object paramObject, long paramLong);

        public native void putFloat(Object paramObject, long paramLong, float paramFloat);

        public native double getDouble(Object paramObject, long paramLong);

        public native void putDouble(Object paramObject, long paramLong, double paramDouble);

        public native byte getByte(long paramLong);

        public native void putByte(long paramLong, byte paramByte);

        public native short getShort(long paramLong);

        public native void putShort(long paramLong, short paramShort);

        public native char getChar(long paramLong);

        public native void putChar(long paramLong, char paramChar);

        public native int getInt(long paramLong);

        public native void putInt(long paramLong, int paramInt);

        public native long getLong(long paramLong);

        public native void putLong(long paramLong1, long paramLong2);

        public native float getFloat(long paramLong);

        public native void putFloat(long paramLong, float paramFloat);

        public native double getDouble(long paramLong);

        public native void putDouble(long paramLong, double paramDouble);

        public native long getAddress(long paramLong);

        public native void putAddress(long paramLong1, long paramLong2);

        public native long allocateMemory(long paramLong);

        public native long reallocateMemory(long paramLong1, long paramLong2);

        public native void setMemory(Object paramObject, long paramLong1, long paramLong2, byte paramByte);

        public native void copyMemory(Object paramObject1, long paramLong1, Object paramObject2, long paramLong2, long paramLong3);

        public native void freeMemory(long paramLong);

        public native long staticFieldOffset(Field paramField);

        public native long objectFieldOffset(Field paramField);

        public native Object staticFieldBase(Field paramField);

        public native boolean shouldBeInitialized(Class<?> paramClass);

        public native void ensureClassInitialized(Class<?> paramClass);

        public native int arrayBaseOffset(Class<?> paramClass);

        public native int arrayIndexScale(Class<?> paramClass);

        public native int addressSize();

        public native int pageSize();

        public native Class<?> defineClass(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ClassLoader paramClassLoader, ProtectionDomain paramProtectionDomain);

        public native Class<?> defineAnonymousClass(Class<?> paramClass, byte[] paramArrayOfByte, Object[] paramArrayOfObject);

        public native Object allocateInstance(Class<?> paramClass)
                throws InstantiationException;

        @Deprecated
        public native void monitorEnter(Object paramObject);

        @Deprecated
        public native void monitorExit(Object paramObject);

        @Deprecated
        public native boolean tryMonitorEnter(Object paramObject);

        public native void throwException(Throwable paramThrowable);

        public final native boolean compareAndSwapObject(Object paramObject1, long paramLong, Object paramObject2, Object paramObject3);

        public final native boolean compareAndSwapInt(Object paramObject, long paramLong, int paramInt1, int paramInt2);

        public final native boolean compareAndSwapLong(Object paramObject, long paramLong1, long paramLong2, long paramLong3);

        public native Object getObjectVolatile(Object paramObject, long paramLong);

        public native void putObjectVolatile(Object paramObject1, long paramLong, Object paramObject2);

        public native int getIntVolatile(Object paramObject, long paramLong);

        public native void putIntVolatile(Object paramObject, long paramLong, int paramInt);

        public native boolean getBooleanVolatile(Object paramObject, long paramLong);

        public native void putBooleanVolatile(Object paramObject, long paramLong, boolean paramBoolean);

        public native byte getByteVolatile(Object paramObject, long paramLong);

        public native void putByteVolatile(Object paramObject, long paramLong, byte paramByte);

        public native short getShortVolatile(Object paramObject, long paramLong);

        public native void putShortVolatile(Object paramObject, long paramLong, short paramShort);

        public native char getCharVolatile(Object paramObject, long paramLong);

        public native void putCharVolatile(Object paramObject, long paramLong, char paramChar);

        public native long getLongVolatile(Object paramObject, long paramLong);

        public native void putLongVolatile(Object paramObject, long paramLong1, long paramLong2);

        public native float getFloatVolatile(Object paramObject, long paramLong);

        public native void putFloatVolatile(Object paramObject, long paramLong, float paramFloat);

        public native double getDoubleVolatile(Object paramObject, long paramLong);

        public native void putDoubleVolatile(Object paramObject, long paramLong, double paramDouble);

        public native void putOrderedObject(Object paramObject1, long paramLong, Object paramObject2);

        public native void putOrderedInt(Object paramObject, long paramLong, int paramInt);

        public native void putOrderedLong(Object paramObject, long paramLong1, long paramLong2);

        public native void unpark(Object paramObject);

        public native void park(boolean paramBoolean, long paramLong);

        public native int getLoadAverage(double[] paramArrayOfDouble, int paramInt);

        public native void loadFence();

        public native void storeFence();

        public native void fullFence();

}
