
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.util;


/**
 * 
 *
 * @date 2018-05-07
 * @author
 */
public abstract class StringUtils {

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static boolean hasLength( String str) {
        return (str != null && !str.isEmpty());
    }

    public static String replace(String inString, String oldPattern,  String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0; // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString.substring(pos));
        return sb.toString();
    }


    /**
     * 使用时间恒定的比较函数
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        // 判断长度是否相同
        int d = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            // 判断每byte是否相同
            d |= a[i] ^ b[i];
        return d == 0;
    }

}
