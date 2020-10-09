package com.bmn.core.stats;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @date 2020-07-16
 * @author zhangyuqiang02@playcrab.com
 */
public class StatsEntry {

    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 名称
     */
    private String name;

    /**
     * 总请求数。成功+失败
     */
    private int numRequests;

    /**
     * 失败数
     */
    private int numFailures;

    /**
     * 总时长
     */
    private int totalResponseTime;

    /**
     * 最小
     */
    private int minResponseTime;

    /**
     * 最大
     */
    private int maxResponseTime;

    /**
     * 每秒请求数
     */
    private Map<Integer, Integer> numReqsPerSec;

    /**
     * 相同响应时间请求数
     */
    private TreeMap<Integer, Integer> responseTimes;
    /**
     * 相同阻塞时间请求数
     */
    private TreeMap<Integer, Integer> blockTimes;

    /**
     *
     */
    private boolean useResponseTimesCache;

    private boolean responseTimesCache;

    /**
     * 总数据量
     */
    private int totalContentLength;

    /**
     * 统计开始时间
     */
    private long startTime;

    /**
     * 上一次请求时间
     */
    private int lastRequestTimestamp;

    /**
     * parent
     */
    private RequestStats stats;

    public StatsEntry(RequestStats stats, String name, boolean useResponseTimesCache) {
        this.stats = stats;
        this.name = name;
        this.useResponseTimesCache = useResponseTimesCache;
        this.reset();
    }

    public void reset() {
        this.startTime = Instant.now().toEpochMilli();
        this.numRequests = 0;
        this.numFailures = 0;
        this.totalResponseTime = 0;
        this.responseTimes = new TreeMap<>();
        this.blockTimes = new TreeMap<>();
        this.minResponseTime = 0;
        this.maxResponseTime = 0;
        this.lastRequestTimestamp = (int) (this.startTime / 1000L);
        this.numReqsPerSec = new HashMap<>();
        this.totalContentLength = 0;
    }

    public void log(long now, int responseTime, int contentLength, int blockTime) {
        // int t = (int) (Instant.now().toEpochMilli() / 1000L);
        int t = (int) (now / 1000L);

        numRequests += 1;
        logTimeOfRequest(t);
        logResponseTime(responseTime);
        logAttrs(blockTime);

        totalContentLength += contentLength;
    }

    /**
     * 阻塞耗时最小值
     */
    private int blockMin;
    /**
     * 阻塞请求耗时最大值
     */
    private int blockMax;

    /**
     * 阻塞总耗时
     */
    private int blockTotal;

    private void logAttrs(int blockTime) {
        if (blockTime == 0) {
            return;
        }

        if (blockTime < blockMin || blockMin == 0) {
            blockMin = blockTime;
        }

        if (blockTime > blockMax) {
            blockMax = blockTime;
        }

        this.blockTotal += blockTime;

        int roundedResponseTime = roundedTime(blockTime);

        Integer v = this.blockTimes.get(roundedResponseTime);
        if (v == null) {
            v = 0;

        }
        this.blockTimes.put(roundedResponseTime, v + 1);
    }

    private void logTimeOfRequest(int t) {
        Integer v = this.numReqsPerSec.get(t);
        if (v == null) {
            v = 0;
        }

        this.numReqsPerSec.put(t, v + 1);
        this.lastRequestTimestamp = t;
    }

    private void logResponseTime(int responseTime) {
        this.totalResponseTime += responseTime;

        if (this.minResponseTime == 0) {
            this.minResponseTime = responseTime;
        }

        this.minResponseTime = Math.min(this.minResponseTime, responseTime);
        this.maxResponseTime = Math.max(this.maxResponseTime, responseTime);

        int roundedResponseTime = roundedTime(responseTime);

        Integer v = this.responseTimes.get(roundedResponseTime);
        if (v == null) {
            v = 0;

        }
        this.responseTimes.put(roundedResponseTime, v + 1);
    }

    private static int roundedTime(int responseTime) {
        int roundedResponseTime;
        if (responseTime < 100) {
            roundedResponseTime = responseTime;
        } else if (responseTime < 1000) {
            roundedResponseTime = responseTime / 10 * 10;
        } else if (responseTime < 10000) {
            roundedResponseTime = responseTime / 100 * 100;
        } else {
            roundedResponseTime = responseTime / 1000 * 1000;
        }

        return roundedResponseTime;
    }

    public void logError(StatsErrorCode throwable) {
        this.numFailures += 1;
    }

    public double failRatio() {
        try {
            return numFailures / (numRequests * 1D);
        } catch (Exception e) {
            if (numFailures > 0) {
                return 1d;
            }

            return 0d;
        }
    }

    public double avgResponseTime() {
        if (numRequests == 0) {
            return 0D;
        }

        try {
            return totalResponseTime / (numRequests * 1D);
        } catch (Exception e) {
            return 0D;
        }
    }

    public double avgBlockTime() {
        if (numRequests == 0) {
            return 0D;
        }
        try {
            return blockTotal / (numRequests * 1D);
        } catch (Exception e) {
            return 0D;
        }
    }

    public double medianResponseTime() {
        if (responseTimes == null || responseTimes.isEmpty()) {
            return 0D;
        }

        int pos = ((numRequests - 1) / 2);

        Iterator<Entry<Integer, Integer>> it = responseTimes.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, Integer> entry = it.next();

            if (pos < entry.getValue()) {
                return entry.getKey();
            }

            pos -= entry.getValue();
        }

        return 0D;
    }

    public double medianBlockTime() {
        if (blockTimes == null || blockTimes.isEmpty()) {
            return 0D;
        }

        int pos = ((numRequests - 1) / 2);

        Iterator<Entry<Integer, Integer>> it = blockTimes.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, Integer> entry = it.next();

            if (pos < entry.getValue()) {
                return entry.getKey();
            }

            pos -= entry.getValue();
        }

        return 0D;
    }

    public int currentThread() {
        double avgResponseTime = avgResponseTime();
        double avgCpu = avgResponseTime - avgBlockTime();
        return (int) (CPU_NUM * (avgResponseTime / avgCpu));
    }


    public int currentRps() {
        int last = stats.lastRequestTimestamp();
        if (last == 0) {
            return 0;
        }

        int sliceStartTime = Math.max(last - 12, (int) (stats.getStartTime() / 1000L));

        int total = 0, c = 0;
        for (int i = sliceStartTime, l = last - 2; i <= l; i++) {
            Integer v = numReqsPerSec.get(i);
            if (v == null) {
                v = 0;
            }

            if (v > 0) {
                total += v;
                c++;
            }
        }

        if (total == 0) {
            return 0;
        }

        return total / c;
    }

    public int totalRps() {
        if (stats.lastRequestTimestamp() == 0 || stats.getStartTime() == 0) {
            return 0;
        }

        return (int) (numRequests / Math
                .max(stats.lastRequestTimestamp() - stats.getStartTime(), 1));
    }

    public int getResponseTimePercentile(double percent) {
        return calculateResponseTimePercentile(responseTimes, numRequests, percent);
    }

    public int getBlockTimePercentile(double percent) {
        return calculateResponseTimePercentile(blockTimes, numRequests, percent);
    }

    public static int calculateResponseTimePercentile(TreeMap<Integer, Integer> responseTimes,
            int numRequests, double percent) {

        int numOfRequest = (int) (numRequests * percent);

        int processedCount = 0;

        Iterator<Entry<Integer, Integer>> it = responseTimes.descendingMap().entrySet()
                .iterator();
        while (it.hasNext()) {
            Entry<Integer, Integer> entry = it.next();
            processedCount += entry.getValue();

            if (numRequests - processedCount <= numOfRequest) {
                return entry.getKey();
            }
        }

        return 0;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public int getNumFailures() {
        return numFailures;
    }

    public int getTotalResponseTime() {
        return totalResponseTime;
    }

    public int getMinResponseTime() {
        return minResponseTime;
    }

    public int getMaxResponseTime() {
        return maxResponseTime;
    }

    public Map<Integer, Integer> getNumReqsPerSec() {
        return numReqsPerSec;
    }

    public Map<Integer, Integer> getResponseTimes() {
        return responseTimes;
    }

    public boolean isUseResponseTimesCache() {
        return useResponseTimesCache;
    }

    public boolean isResponseTimesCache() {
        return responseTimesCache;
    }

    public int getTotalContentLength() {
        return totalContentLength;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getLastRequestTimestamp() {
        return lastRequestTimestamp;
    }

    public String getName() {
        return name;
    }

    public String percentile() {

        String fmt = "|%14s|%11d|%10d|%10d|%10d|%10d|%10d|%10d|%10d|%10d|%11d|";

        int five = getResponseTimePercentile(0.5);
        int six = getResponseTimePercentile(0.66);
        int seven = getResponseTimePercentile(0.75);
        int eight = getResponseTimePercentile(0.80);
        int nine = getResponseTimePercentile(0.90);
        int ninetyFive = getResponseTimePercentile(0.95);
        int ninetyEight = getResponseTimePercentile(0.98);
        int ninetyNine = getResponseTimePercentile(0.99);
        int all = getResponseTimePercentile(1.00);
        return String.format(fmt, name, numRequests, five, six, seven, eight, nine, ninetyFive,
                ninetyEight, ninetyNine, all);
    }


    public String blockPercentile() {

        String fmt = "|%14s|%11d|%10d|%10d|%10d|%10d|%10d|%10d|%10d|%10d|%11d|";

        int five = getBlockTimePercentile(0.5);
        int six = getBlockTimePercentile(0.66);
        int seven = getBlockTimePercentile(0.75);
        int eight = getBlockTimePercentile(0.80);
        int nine = getBlockTimePercentile(0.90);
        int ninetyFive = getBlockTimePercentile(0.95);
        int ninetyEight = getBlockTimePercentile(0.98);
        int ninetyNine = getBlockTimePercentile(0.99);
        int all = getBlockTimePercentile(1.00);
        return String.format(fmt, name, numRequests, five, six, seven, eight, nine, ninetyFive,
                ninetyEight, ninetyNine, all);
    }

    @Override
    public String toString() {
        String fmt = "|%-5s%14s|%11s|%12s|%10.1f|%10s|%10s|%13.1f|%12s|";

        String fail = failPercent();

        return String.format(fmt, "", name, numRequests, fail, avgResponseTime(), minResponseTime,
                maxResponseTime, medianResponseTime(), currentRps());
    }

    public String toBlockString() {
        String fmt = "|%-5s%14s|%11s|%12s|%10.1f|%10s|%10s|%13.1f|%12s|";

        return String.format(fmt, "(B)", name, numRequests, "0(0.00%)", avgBlockTime(), blockMin,
                blockMax, medianBlockTime(), currentThread());
    }

    public String failPercent() {
        String fail = "0(0.00%)";
        if (numRequests == 0) {
            return fail;
        }

        try {
            double failPercent = ((numFailures * 1D) / numRequests) * 100D;

            fail = String.format("%d(%.2f%%)", numFailures, failPercent);
        } catch (Exception e) {
            //
        }

        return fail;
    }


}
