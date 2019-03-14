package com.bmn.core.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author: zyq
 * @date: 2019/3/5
 */
public class PoolTest {


    public static void main(String[] args) throws Exception {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        GenericObjectPool<Object> pool = new GenericObjectPool<Object>(new ObjectPollFactory());

        pool.borrowObject();

        pool.returnObject("123");

        pool.invalidateObject("123");
    }

}
