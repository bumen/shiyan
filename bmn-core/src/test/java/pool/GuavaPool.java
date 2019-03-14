package pool;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zyq
 * @date: 2019/3/1
 */
public class GuavaPool {

    private static AtomicInteger count = new AtomicInteger(0);

    private static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .initialCapacity(100)
        .concurrencyLevel(1).weakKeys().weakValues().expireAfterAccess(3, TimeUnit.SECONDS)
        .refreshAfterWrite(1, TimeUnit.SECONDS).removalListener(
            new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    new Throwable().printStackTrace();

                    System.out.println("this is removed_" + removalNotification.getValue());
                }
            }).build(new CacheLoader<String, String>() {
            @Override
            public String load(String dialogId) throws Exception {
                Throwable t = new Throwable();
                t.printStackTrace();

                //throw new IllegalStateException();
                return dialogId+ "_hahah_" + count.getAndIncrement();
            }
        });

    public static void testCache() {
        String v = cache.getUnchecked("333");
        System.out.println(v);


        Thread t = new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String vv = cache.getUnchecked("444");
            System.out.println(vv);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(cache.getUnchecked("555"));

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(cache.getUnchecked("555"));


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(cache.getUnchecked("555"));

        });
        t.start();
    }


    private static final UidInfo empty = new UidInfo();
    /**
     * 30分钟不访问就过期，需要重新获取token
     * 每隔1分钟访问时，就刷新一次
     */
    private static final LoadingCache<String, UidInfo> userIdCache = CacheBuilder.newBuilder()
        .initialCapacity(1000)
        .concurrencyLevel(2).weakKeys().expireAfterAccess(2, TimeUnit.SECONDS)
        .refreshAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, UidInfo>() {
            @Override
            public UidInfo load(String deviceId) throws Exception {
                System.out.println("get null token ");
                return empty;
            }

            @Override
            public ListenableFuture<UidInfo> reload(String key, UidInfo oldValue) throws Exception {
                if(oldValue != null) {
                    int cur = (int) (Instant.now().toEpochMilli() / 1000l);
                    if (cur < oldValue.expireTime) {
                        System.out.println("use same token success");
                        return Futures.immediateFuture(oldValue);
                    }
                }
                return super.reload(key, oldValue);
            }
        });

    public static void testUidCache() throws ExecutionException, InterruptedException {
        String uid = "123";
        UidInfo info = userIdCache.get(uid);

        if(info == empty) {
            info = new UidInfo();
            info.expireTime = (int) (Instant.now().toEpochMilli() / 1000l) + 10000;
            info.token = "abc";

            userIdCache.put(uid, info);
        }


        TimeUnit.MILLISECONDS.sleep(1500);

        UidInfo info1 = userIdCache.get(uid);

        System.out.println(info1.token);

        TimeUnit.MILLISECONDS.sleep(1000);

        UidInfo info2 = userIdCache.get(uid);

        System.out.println(info2.token);


    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testUidCache();
    }

    private static class UidInfo {
        private String token;
        private int expireTime;
    }

}
