package com.bmn.core.stats;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @date 2020-07-16
 * @author zhangyuqiang02@playcrab.com
 */
public class RequestStats {

    private TreeMap<String, StatsEntry> entries = new TreeMap<>();
    private Map<String, StatsError> errors = new HashMap<>();

    private StatsEntry total;

    private long startTime;


    public RequestStats() {
        total = new StatsEntry(this, "Total", true);
    }

    public int numRequests() {
        return this.total.getNumRequests();
    }

    public int numFailures() {
        return this.total.getNumFailures();
    }

    public int lastRequestTimestamp() {
        return this.total.getLastRequestTimestamp();
    }

    public void logRequest(String name, long now, int responseTime, int contentLength, int blockTime) {
        this.total.log(now, responseTime, contentLength, blockTime);
        this.get(name).log(now, responseTime, contentLength, blockTime);
    }

    public void logError(String name, StatsErrorCode error) {
        this.total.logError(error);
        this.get(name).logError(error);

        // String key = StatsError.createKey(name, error);

        StatsError entry = getErrorEntry(name, error);

        entry.occurred();
    }

    private StatsError getErrorEntry(String name, StatsErrorCode error) {
        StatsError entry = errors.get(name);
        if (entry == null) {
            entry = new StatsError(name, error);
            errors.put(name, entry);
        }
        return entry;
    }

    private StatsEntry get(String name) {
        StatsEntry entry = this.entries.get(name);
        if (entry == null) {
            entry = new StatsEntry(this, name, false);
            entries.put(name, entry);
        }

        return entry;
    }

    public void resetAll() {
        this.startTime = Instant.now().toEpochMilli();
        this.total.reset();
        this.errors = new HashMap<>();
        for (Map.Entry<String, StatsEntry> entrys : entries.entrySet()) {
            entrys.getValue().reset();
        }
    }

    public void clearAll() {
        this.total = new StatsEntry(this, "Total", true);
        this.entries = new TreeMap<>();
        this.errors = new HashMap<>();
        this.startTime = 0;
    }


    public long getStartTime() {
        return startTime;
    }

    public TreeMap<String, StatsEntry> getEntries() {
        return entries;
    }

    public Map<String, StatsError> getErrors() {
        return errors;
    }

    public StatsEntry getTotal() {
        return total;
    }
}
