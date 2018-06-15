
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.rt.lang.primitive;

/**
 * 
 *
 * @date 2018-04-08
 * @author 562655151@qq.com
 */
public class BmnBoolean {

    /**
     * 实现接口 serializable, Comparable
     * <p>
     * 
     * 不可以变值：<code>final boolean value</code>
     * <p>
     * 
     * 原始数据类型：<code>Boolean.Type = Class.getPrimitiveClass("boolean") == "boolean"</code>
     * 
     */
    private Boolean valid = false;

    public static void main(String[] args) {
        System.out.println(Boolean.TYPE);
    }
}
