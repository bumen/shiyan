package com.bmn.core.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.PooledSoftReference;

import java.lang.ref.SoftReference;

/**
 * @author: zyq
 * @date: 2019/2/28
 */
public class ObjectPollFactory implements PooledObjectFactory<Object> {

    @Override
    public PooledObject<Object> makeObject() throws Exception {
        Throwable t = new Throwable();
        t.printStackTrace();

        return new PooledSoftReference<Object>(new SoftReference<>("123"));


    }

    @Override
    public void destroyObject(PooledObject<Object> pooledObject) throws Exception {
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    @Override
    public boolean validateObject(PooledObject<Object> pooledObject) {

        Throwable t = new Throwable();
        t.printStackTrace();
        return false;
    }

    @Override
    public void activateObject(PooledObject<Object> pooledObject) throws Exception {
        Throwable t = new Throwable();
        t.printStackTrace();
    }

    @Override
    public void passivateObject(PooledObject<Object> pooledObject) throws Exception {
        Throwable t = new Throwable();
        t.printStackTrace();
    }
}
