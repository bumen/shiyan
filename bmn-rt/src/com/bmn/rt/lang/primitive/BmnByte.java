
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.rt.lang.primitive;

import com.bmn.rt.lang.BmnNumber;

/**
 * 
 *
 * @date 2018-04-08
 * @author 562655151@qq.com
 */
public class BmnByte extends BmnNumber {

    /**
     * 继承Number, 实现接口 Comparable
     * <p>
     * 
     * 最大，最小值
     * <p>
     * 
     * ByteCache 缓存 valueof时使用缓存
     * <p>
     */
    private Byte b;

    public static void main(String[] args) {
        String hex = "0x30";
        byte bb = Byte.decode(hex);
        System.out.println(bb);
    }

}
