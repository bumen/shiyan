package com.bmn.rt.util;

import java.util.TimeZone;

/**
 * Created by Administrator on 2016/12/28.
 */
public class T {
    public static void main(String[] args) {
        String s = "  a b c  .d e ";

        s = StringUtils.trimAllWhitespace(s);

        System.out.println(s);

        // 默认时区
        TimeZone tz = TimeZone.getDefault();

        // 获取“id”
        String id = tz.getID();

        // 获取“显示名称”
        String name = tz.getDisplayName();

        // 获取“时间偏移”。相对于“本初子午线”的偏移，单位是ms。
        int offset = tz.getRawOffset();
        // 获取“时间偏移” 对应的小时
        int gmt = offset/(3600*1000);

        System.out.println(tz.getDSTSavings());


        System.out.printf("id=%s, name=%s, offset=%s(ms), gmt=%s\n",
                id, name, offset, gmt);
    }
}
