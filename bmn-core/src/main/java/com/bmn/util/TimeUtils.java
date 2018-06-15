
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 与当前时间有关的方法，以now开头。同时通过{@link TimeUtils.now()}获取当前时间
 *
 * @date 2018-05-08
 * @author
 */
public class TimeUtils {
    private static volatile long shiftFakeMilli = 0;

    public static long getShiftFakeMilli() {
        return shiftFakeMilli;
    }

    public static void setShiftFakeMilli(long shiftFakeMilli) {
        TimeUtils.shiftFakeMilli = shiftFakeMilli;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public static String millisToDateTime(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);

        ZonedDateTime time = instant.atZone(ZoneId.systemDefault());

        return time.format(formatter);
    }

    /**
     * 获取当前时间
     */
    public static long now() {
        return shiftFakeMilli + Instant.now().toEpochMilli();
    }

    public static int nowSeconds() {
        return (int) (now() / 1000);
    }

    /**
     * 当前时间，减去多少时间
     */
    public static long nowTimeMinus(long time, TimeUnit unit) {
        long now = now();
        long minusMillis = unit.toMillis(time);
        if (now < minusMillis) {
            return 0;
        }

        return now - minusMillis;
    }

    /**
     * 当前整点小时
     */
    public static long nowTimeHour() {
        long now = now();

        ZonedDateTime date = Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).withMinute(0)
                .withSecond(0);

        return date.toInstant().toEpochMilli();
    }

    /**
     * 当前整点小时，减去多少时间
     */
    public static long nowTimeHourMinus(long time, TimeUnit unit) {
        long nowHour = nowTimeHour();
        long minusMillis = unit.toMillis(time);
        if (nowHour < minusMillis) {
            return 0;
        }

        return nowHour - minusMillis;
    }

    // 调试命令修改虚假当前时间
    public static void debugNowTime(long epochMilli) {
        long shiftMilli = epochMilli - now();

        setShiftFakeMilli(shiftMilli + shiftFakeMilli);
    }

    // 调试命令重置虚假当前时间
    public static void debugRestNowTime() {
        shiftFakeMilli = 0;
    }

    /**
     * 毫秒转为hh:mm:ss
     */
    public static String millisTohhmmss(long millis) {
        millis = millis / 1000;

        StringBuilder sb = new StringBuilder();
        if (millis >= 3600) {
            sb.append(millis / 3600);
            sb.append("小时");
            millis = millis % 3600;
        }

        if (millis >= 60) {
            sb.append(millis / 60);
            sb.append("分钟");
            millis = millis % 60;
        }

        sb.append(millis);
        sb.append("秒");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(TimeUtils.nowTimeMinus(5, TimeUnit.MINUTES));

        System.out.println(TimeUtils.now());

        TimeUtils.debugNowTime(1526714616000l);

        System.out.println(TimeUtils.now());

        TimeUtils.debugRestNowTime();

        System.out.println(TimeUtils.now());

        System.out.println(TimeUtils.nowTimeHour());

    }

}
