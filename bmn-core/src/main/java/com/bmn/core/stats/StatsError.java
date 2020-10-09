package com.bmn.core.stats;

/**
 * @date 2020-07-16
 * @author zhangyuqiang02@playcrab.com
 */
public class StatsError {

    private String name;

    private StatsErrorCode errorCode;

    private int occurrences;

    public StatsError(String name, StatsErrorCode errorCode) {
        this(name, errorCode, 0);
    }

    public StatsError(String name, StatsErrorCode errorCode, int occurrences) {
        this.name = name;
        this.errorCode = errorCode;
        this.occurrences = occurrences;
    }

    public void occurred() {
        this.occurrences += 1;
    }

    public static String createKey(String name, StatsErrorCode errorCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(".").append(errorCode.code());
        return sb.toString();
    }

    public int getOccurrences() {
        return this.occurrences;
    }

    public String toName() {
        String cName = errorCode.codeName();
        if ("".equals(cName)) {
            cName = String.valueOf(errorCode.code());
        }

        return name + "." + cName;
    }

    public String getName() {
        return name;
    }

    public StatsErrorCode getErrorCode() {
        return errorCode;
    }

    public void extend(int num) {
        this.occurrences += num;
    }
}
