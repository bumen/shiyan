package com.bmn.core.stats;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date 2020-08-24
 * @author zhangyuqiang02@playcrab.com
 */
public class StatsContext {

    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    private static final ThreadLocal<StatsContext> LOCAL = new ThreadLocal<>();

    /**
     * 设置上下文
     *
     * @param context RPC内置上下文
     */
    public static void setContext(StatsContext context) {
        LOCAL.set(context);
    }

    /**
     * 得到上下文，为空则自动创建
     *
     * @return RPC内置上下文
     */
    public static StatsContext getContext() {
        StatsContext context = LOCAL.get();
        if (context == null) {
            context = new StatsContext();
            LOCAL.set(context);
        }
        return context;
    }

    /**
     * 查看上下文，为空不自动创建
     *
     * @return RPC内置上下文
     */
    public static StatsContext peekContext() {
        return LOCAL.get();
    }

    /**
     * 清理上下文
     */
    public static void removeContext() {
        LOCAL.remove();
    }


    private static boolean isOpen;


    static {
        isOpen = Boolean.valueOf(System.getProperty("dev.state"));
    }

    private Map<String, Long> attachments = new ConcurrentHashMap<>();

    private int blockMethodIndex;

    public static void init() {
        if (!isOpen) {
            return;
        }

        StatsService.INSTANCE.start();
    }


    public static void shutdown() {
        if (!isOpen) {
            return;
        }

        StatsService.INSTANCE.stop();
    }

    /**
     * get attachment.
     *
     * @param key the key
     * @return attachment attachment
     */
    public Object getAttachment(String key) {
        return key == null ? null : attachments.get(key);
    }


    /**
     * remove attachment.
     *
     * @param key the key
     * @return Old value
     */
    public Object removeAttachment(String key) {
        return attachments.remove(key);
    }


    /**
     * 构建阻塞方法名称，名称统一为了显示整齐
     * @return 名称
     */
    private String buildBlockMethodName(String name) {
        if (name == null) {
            return String.valueOf(++blockMethodIndex);
        }

        return name.concat(String.valueOf(++blockMethodIndex));
    }

    /**
     * 记录阻塞方法耗时
     * @param start 开始时间
     */
    public void recordBlockElapse(long start) {
        long elapse = Instant.now().toEpochMilli() - start;
        attachments.put(buildBlockMethodName(null), elapse);
    }

    /**
     * 记录阻塞方法耗时
     * @param name 名称
     * @param start 开始时间
     */
    public void recordBlockElapse(String name, long start) {
        long elapse = Instant.now().toEpochMilli() - start;
        attachments.put(buildBlockMethodName(name), elapse);
    }

    /**
     * 记录topic开始
     * @param topic 主题
     */
    public void recordStart(String topic) {
        attachments.put(topic, Instant.now().toEpochMilli());
    }

    /**
     * 记录topic结束
     * @param topic 主题
     */
    public void recordStartedElapse(String topic) {
        Long v = (Long) attachments.remove(topic);
        if (v == null) {
            return;
        }

        long elapse = Instant.now().toEpochMilli() - v;

        attachments.put(topic, elapse);
    }


    /**
     * 记录topic结束，并打印
     * @param topic 主题
     * @param errorCode 状态码
     */
    public void recordFinish(String topic, StatsErrorCode errorCode) {
        if (!isOpen) {
            return;
        }

        // 获取开始时间
        Long v = attachments.remove(topic);
        if (v == null) {
            return;
        }

        long now = Instant.now().toEpochMilli();
        // 执行时间
        int up = (int) (now - v);

        // 请求成功
        if (errorCode.code() == StatsErrorCode.SUCCESS_CODE) {
            String str = null;
            int sum = 0;
            if (!attachments.isEmpty()) {
                int min = 0, max = 0, avg = 0, size = 0;

                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Long> entry : attachments.entrySet()) {
                    int vv = entry.getValue().intValue();
                    sum += vv;

                    if (vv > 0) {
                        size++;
                    }

                    if (vv < min || min == 0) {
                        min = vv;
                    }

                    if (vv > max) {
                        max = vv;
                    }

                    sb.append(entry.getKey()).append("=").append(vv).append(",");
                }

                int c = up - sum, num = 0;
                if (c > 0) {
                    num = CPU_NUM * (up / c);
                }

                str = String
                        .format("[%s] up:%d block:%d, min:%d max:%d, thread:%d, size:%d, str:%s",
                                Thread.currentThread().getName(), up, sum, min, max, num, size,
                                sb.toString());
                blockMethodIndex = 0;
                attachments.clear();
            }

            StatsService.INSTANCE.onRequestSuccess(topic, now, up, 0, sum, str);
        } else {
            StatsService.INSTANCE.onRequestFailure(topic, now, up, errorCode);
        }
    }

}
