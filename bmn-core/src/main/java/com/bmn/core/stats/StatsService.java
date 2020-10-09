package com.bmn.core.stats;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date 2020-07-17
 * @author zhangyuqiang02@playcrab.com
 */
public enum StatsService {
    /**
     *
     */
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger("ROBOT");

    private Thread thread;

    private volatile boolean running = false;

    private RequestStats globalStats = new RequestStats();

    private volatile boolean print = true;

    public void start() {
        thread = new Thread(() -> {
            statsPrinter();
        });
        thread.setName("Robot-StatsEngine");
        thread.start();
    }

    public void offPrint() {
        print = false;
    }

    private static final int taskRate = (int) (2000 * (0.9d));

    public synchronized void statsPrinter() {
        if (running) {
            return;
        }

        int resetTime = 0;

        long start, end, delay = 2000 - taskRate;
        running = true;
        while (running) {
            start = Instant.now().toEpochMilli();

            long l = runAllTasks(start + taskRate);

            if (l < 0L) {
                l = 0L;
            }

            if (print) {
                printStats();
            }

            if (change) {
                resetTime = 0;
                change = false;
            } else {
                if (resetTime >= 0 && ++resetTime >= 5) {
                    resetTime = -1;

                    finishPrint();

                    globalStats.clearAll();
                    internalStats.clearAll();
                }
            }

            try {
                Thread.sleep(delay + l);
            } catch (InterruptedException e) {
                //pass
            }
        }

        finishPrint();
    }

    private long runAllTasks(final long deadline) {
        int runTasks = 0;
        Runnable r;
        while ((r = runnables.poll()) != null) {
            r.run();

            runTasks++;

            if (deadline > 0) {
                if ((runTasks & 0x3F) == 0) {
                    long now = Instant.now().toEpochMilli();
                    if (now >= deadline) {
                        return 0L;
                    }
                }
            }
        }

        return deadline - Instant.now().toEpochMilli();
    }

    public void stop() {
        if (!running) {
            return;
        }

        running = false;

        thread.interrupt();

        logger.info("stats engine stopped");
    }

    public void finishPrint() {
        runAllTasks(0);

        if (print) {
            printStats();
            printPercentileStats();
            printErrorReport();
        }

        printInternalStats();
    }

    private ConcurrentLinkedQueue<Runnable> runnables = new ConcurrentLinkedQueue<>();

    private volatile boolean change;

    public void onRequestSuccess(String name, long now, int responseTime, int responseLength, int blockTime, String blockStr) {
        runnables.offer(() -> {
            globalStats.logRequest(name, now, responseTime, responseLength, blockTime);
            change = true;

            if (blockStr != null) {
                logger.info("[{}]-{}", name, blockStr);
            }
        });
    }

    public void onRequestFailure(String name, long now, int responseTime,
            StatsErrorCode errorCode) {
        runnables.offer(() -> {
            globalStats.logRequest(name, now, responseTime, 0, 0);
            globalStats.logError(name, errorCode);
        });
    }

    public void onInternalSuccess(String name, long now, int costTime) {
        runnables.offer(() -> {
            internalStats.logRequest(name, now, costTime, 0, 0);
        });
    }

    private RequestStats internalStats = new RequestStats();


    public void printStats() {
        TreeMap<String, StatsEntry> stats = globalStats.getEntries();
        if (stats.isEmpty()) {
            return;
        }

        logger.info(
                "|               Name|       reqs|       fails|       Avg|       Min|       Max|     < Median|  req/s (10)|");
        logger.info(
                "+-------------------+-----------+------------+----------+----------+----------+-------------+------------+");
        int totalRps = 0;
        int totalReqs = 0;
        int totalFailures = 0;

        Iterator<Map.Entry<String, StatsEntry>> it = stats.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StatsEntry> entryEntry = it.next();
            StatsEntry r = entryEntry.getValue();
            totalRps += r.currentRps();
            totalReqs += r.getNumRequests();
            totalFailures += r.getNumFailures();

            logger.info("{}", r);
            logger.info("{}", r.toBlockString());
            logger.info(
                    "+-------------------+-----------+------------+----------+----------+----------+-------------+------------+");
        }

        String fail = "0(0.00%)";
        if (totalReqs > 0) {
            try {
                double failPercent = ((totalFailures * 1D) / totalReqs) * 100D;
                fail = String.format("%d(%.2f%%)", totalFailures, failPercent);
            } catch (Exception e) {
                // pass
            }
        }

        logger.info("Total reqs:{} Fail:{} rps:{}", totalReqs, fail, totalRps);
        logger.info("");
    }

    public void printPercentileStats() {
        TreeMap<String, StatsEntry> stats = globalStats.getEntries();
        if (stats.isEmpty()) {
            return;
        }

        logger.info("Percentage of the requests completed within given times");
        logger.info(
                "|          Name|       reqs|      <50%|      <66%|      <75%|      <80%|      <90%|      <95%|      <98%|      <99%|      <100%|");
        logger.info(
                "+--------------+-----------+----------+----------+----------+----------+----------+----------+----------+----------+-----------+");
        Iterator<Map.Entry<String, StatsEntry>> it = stats.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StatsEntry> entryEntry = it.next();
            StatsEntry r = entryEntry.getValue();

            if (!r.getResponseTimes().isEmpty()) {
                logger.info(r.percentile());
            }
        }

        logger.info(
                "+--------------+-----------+----------+----------+----------+----------+----------+----------+----------+----------+-----------+");

        StatsEntry totalStats = globalStats.getTotal();
        if (!totalStats.getResponseTimes().isEmpty()) {
            logger.info(totalStats.percentile());
        }
        logger.info("");
    }

    public void printErrorReport() {
        Map<String, StatsError> errors = globalStats.getErrors();
        if (errors.isEmpty()) {
            return;
        }

        logger.info("Error report");
        logger.info("|      occurrences|                      Error|");
        logger.info("+-----------------+---------------------------+");
        for (StatsError error : errors.values()) {
            logger.info("{}", String.format("|%17d|%27s|", error.getOccurrences(), error.toName()));
        }
        logger.info("+-----------------+---------------------------+");
        logger.info("");
    }


    public void printInternalStats() {
        TreeMap<String, StatsEntry> stats = internalStats.getEntries();
        if (stats.isEmpty()) {
            return;
        }

        logger.info("Internal report");
        logger.info(
                "|          Name|       reqs|       fails|       min|       max|     < Median|");
        logger.info(
                "+--------------+-----------+------------+----------+----------+-------------+");
        int totalReqs = internalStats.numRequests();
        int totalFailures = internalStats.numFailures();

        Iterator<Map.Entry<String, StatsEntry>> it = stats.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StatsEntry> entryEntry = it.next();
            StatsEntry r = entryEntry.getValue();

            logger.info("{}", String.format("|%14s|%11s|%12s|%10s|%10s|%13.1f|", r.getName(),
                    r.getNumRequests(),
                    r.failPercent(), r.getMinResponseTime(), r.getMaxResponseTime(),
                    r.medianResponseTime()));
        }

        logger.info(
                "+--------------+-----------+------------+----------+----------+-------------+");

        int failPercent = 0;
        try {
            failPercent = (totalFailures / totalReqs) * 100;
        } catch (Exception e) {
            // pass
        }

        logger.info("Total reqs:{} Fail:{},{}", totalReqs, totalFailures, failPercent);
        logger.info("");
    }
}
