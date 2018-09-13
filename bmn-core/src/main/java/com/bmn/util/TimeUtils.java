
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package cn.itimi.tv.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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

    /*****************************************
     * 格式化时间 *
     *****************************************/

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public static String millisToDateTime(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);

        ZonedDateTime time = instant.atZone(ZoneId.systemDefault());

        return time.format(formatter);
    }

    /*****************************************
     * 使用当前时间 *
     *****************************************/

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
     * 当前时间加上多少秒
     * <p>
     * 
     * @param unit 当unit小于TimeUnit.SECOND时，如果转为秒时小于等于0，则返回当前时间
     */
    public static long nowTimeMillisPlus(long time, TimeUnit unit) {
        long seconds = unit.toSeconds(time);
        if (seconds <= 0) {
            return now();
        }

        return nowDateTime().plusSeconds(seconds).toInstant().toEpochMilli();
    }

    /**
     * 当前时间减去多少秒
     * <p>
     * 
     * @param unit 当unit小于TimeUnit.SECOND时，如果转为秒时小于等于0，则返回当前时间
     */
    public static long nowTimeMillisMinus(long time, TimeUnit unit) {
        long seconds = unit.toSeconds(time);
        if (seconds <= 0) {
            return now();
        }

        return nowDateTime().minusSeconds(seconds).toInstant().toEpochMilli();
    }

    /**
     * 当前时间天,00:00:00
     */
    public static long nowDayTime() {
        return localDateToTime(LocalDate.now());
    }

    /**
     * 当前结束时间，23:59:59
     */
    public static long nowDayEndTime() {
        return nowTimePlusDay(1) - 1;
    }

    /**
     * 当前时间之后几天,00:00:00
     */
    public static long nowTimePlusDay(int day) {
        LocalDate localDate = LocalDate.now().plusDays(day);
        return localDateToTime(localDate);
    }

    /**
     * 当前时间之前几天,00:00:00
     */
    public static long nowTimeMinusDay(int day) {
        LocalDate localDate = LocalDate.now().minusDays(day);
        return localDateToTime(localDate);
    }

    /**
     * 当前整点小时
     */
    public static long nowTimeHour() {
        ZonedDateTime date = nowDateTime().withMinute(0)
                .withSecond(0);

        return date.toInstant().toEpochMilli();
    }

    public static ZonedDateTime nowDateTime() {
        long now = now();
        return Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault());
    }

    /*****************************************
     * 使用指定时间 *
     *****************************************/

    /**
     * 指定年月天,00:00:00
     */
    public static long timeOf(int year, int month, int day) {
        return localDateToTime(LocalDate.of(year, month, day));
    }

    /**
     * yyyy-mm-dd 00:00:00
     */
    public static long localDateToTime(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 指定时间的一天的开始: yyyy-mm-dd 00:00:00
     */
    public static long ofDayStartTime(long time) {
        return localDateToTime(ofTimeToLocalDate(time));
    }

    /**
     * 指定时间的一天的结束: yyyy-mm-dd 23:59:59
     */
    public static long ofDayEndTime(long time) {
        return ofTimePlusDay(time, 1) - 1;
    }

    /**
     * 指定时间，几天后的时间yyyy-mm-dd 00:00:00
     */
    public static long ofTimePlusDay(long time, int day) {
        if (day <= 0) {
            return localDateToTime(ofTimeToLocalDate(time));
        }

        return localDateToTime(ofTimeToLocalDate(time).plusDays(day));
    }

    /**
     * 指定时间，几天前的时间yyyy-mm-dd 00:00:00
     */
    public static long ofTimeMinusDay(long time, int day) {
        if (day <= 0) {
            return localDateToTime(ofTimeToLocalDate(time));
        }

        return localDateToTime(ofTimeToLocalDate(time).minusDays(day));
    }

    /**
     * 返回指定时间的日期对象
     */
    public static LocalDate ofTimeToLocalDate(long time) {
        return ofTimeToLocalDateTime(time).toLocalDate();
    }

    /**
     * 返回指定时间的日期与时间对象
     */
    public static ZonedDateTime ofTimeToLocalDateTime(long time) {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime ofLocalDateTime(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneId.systemDefault());
    }

    public static long localDateTimeToMillis(LocalDateTime time) {
        return ofLocalDateTime(time).toInstant().toEpochMilli();
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

    /*****************************************
     * 通过calendar格式化时间统一使用CALENDAR对象 *
     *****************************************/

    /**
     * 
     * 1. 只能做为格式化时间工具使用
     * <p>
     * 2. 使用次使用cALENDAR必须调用setTimeInMillis设置时间
     */
    private static final Calendar CALENDAR = Calendar.getInstance();

    private static final int MONDAY = 2;

    /**
     * 根据提供的年月日获取该月份的第一天
     */
    public static long getSupportBeginDayofMonth(long time) {
        CALENDAR.setTimeInMillis(time);
        CALENDAR.set(Calendar.DAY_OF_MONTH, 1);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        CALENDAR.set(Calendar.SECOND, 0);
        CALENDAR.set(Calendar.MILLISECOND, 0);
        return CALENDAR.getTimeInMillis();
    }

    /**
     * 根据提供的年月日获取该月份的最后一天
     */

    public static long getSupportEndDayofMonth(long time) {
        CALENDAR.setTimeInMillis(time);
        CALENDAR.set(Calendar.DAY_OF_MONTH, CALENDAR.getActualMaximum(Calendar.DAY_OF_MONTH));
        CALENDAR.set(Calendar.HOUR_OF_DAY, 23);
        CALENDAR.set(Calendar.MINUTE, 59);
        CALENDAR.set(Calendar.SECOND, 59);
        CALENDAR.set(Calendar.MILLISECOND, 999);
        return CALENDAR.getTimeInMillis();
    }

    public static int countWeekofMonth(long time) {
        CALENDAR.setFirstDayOfWeek(MONDAY);
        CALENDAR.setTimeInMillis(time);
        return CALENDAR.getActualMaximum(Calendar.WEEK_OF_MONTH);

    }

    /**
     * 周开始时间为周一
     * 
     * @param time
     * @param weekNum
     * @return
     */
    public static long getBeginTimeOfWeek(long time, int weekNum) {
        CALENDAR.setFirstDayOfWeek(MONDAY);
        CALENDAR.setTimeInMillis(time);
        CALENDAR.set(Calendar.WEEK_OF_MONTH, weekNum);
        CALENDAR.set(Calendar.DAY_OF_WEEK, 2);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        CALENDAR.set(Calendar.SECOND, 0);
        CALENDAR.set(Calendar.MILLISECOND, 0);
        return CALENDAR.getTimeInMillis();
    }

    /**
     * 当前月最后一周(包含下个月在当前周内日期) 周结束时间为周日
     * 
     * @param time
     * @param weekNum
     * @return
     */
    public static long getEndTimeOfWeek(long time, int weekNum) {
        CALENDAR.setFirstDayOfWeek(MONDAY);
        CALENDAR.setTimeInMillis(time);
        CALENDAR.set(Calendar.WEEK_OF_MONTH, weekNum);
        CALENDAR.set(Calendar.DAY_OF_WEEK, 1);
        CALENDAR.set(Calendar.HOUR_OF_DAY, 23);
        CALENDAR.set(Calendar.MINUTE, 59);
        CALENDAR.set(Calendar.SECOND, 59);
        CALENDAR.set(Calendar.MILLISECOND, 999);
        return CALENDAR.getTimeInMillis();
    }

    public static void main(String[] args) {
        System.out.println(TimeUtils.nowTimeMinus(5, TimeUnit.MINUTES));

        System.out.println(TimeUtils.now());

        TimeUtils.debugNowTime(1526714616000l);

        System.out.println(TimeUtils.now());

        TimeUtils.debugRestNowTime();

        System.out.println(TimeUtils.now());

        System.out.println(TimeUtils.nowTimeHour());

        long v = TimeUtils.nowTimePlusDay(1);
        System.out.println("add day: " + v);
        v = TimeUtils.nowTimeMinusDay(1);
        System.out.println(v);
        v = TimeUtils.nowDayTime();
        System.out.println(v);

        v = TimeUtils.timeOf(2018, 8, 1);
        System.out.println(v);
        System.out.println(v - 1);

        System.out.println(Month.JANUARY.maxLength());
        int day = LocalDate.now().getDayOfMonth();
        LocalDateTime local = LocalDateTime.now();
        local = local.withMonth(8);
        LocalDateTime local2 = local.minusDays(day).plusDays(1);
        System.out.println(local2);
        long vvv = ZonedDateTime.of(local2, ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(vvv);
        System.out.println(countWeekofMonth(vvv));

        vvv = getSupportBeginDayofMonth(vvv);
        System.out.println("first month " + vvv);

        vvv = getSupportEndDayofMonth(vvv);
        System.out.println("last month " + vvv);

        System.out.println(local.minusDays(day).plusMonths(1));

        long t = nowDayEndTime();
        System.out.println(t);
    }

}
