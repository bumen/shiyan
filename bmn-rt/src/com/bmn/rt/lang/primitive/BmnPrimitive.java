
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
public class BmnPrimitive {

    public void exchange() {
        byte b = 0x31;

        short s = 10;

        long l1 = 1123123l;

        float f = l1;

        char ch = (char) b;
        s = (short) ch;
        ch = (char) s;

        // 转为int计算，需要结果需要再转回Byte
        byte b1 = 3, b2 = 3;
        byte b3 = (byte) (b1 + b2);

        // 默认提高为double类型， 结果再转强转回float
        int i1 = 3, i2 = 3;
        float f1 = (float) ((i1 + i2) * 0.2);

    }

    public void stringObj() {
        String a = "atrue";
        String b = "a" + "true";
        System.out.println((a == b)); // result = true

        String a1 = "ab";
        String bb = "b";
        String b1 = "a" + bb; // 因为bb是变量所以在编译期间不能确定
        System.out.println((a1 == b1)); // result = false
    }

    /**
     * 运算符优先级
     * <p>
     * 1. <code> ++ -- </code>
     * <p>
     * 2. 算术
     * <p>
     * 3. 位移
     * <p>
     * 4. 关系
     * 
     * <pre>
     *   它的操作返回值是boolean 类型，所以一般与逻辑运算符一起使用
     * </pre>
     * <p>
     * 5. 位
     * 
     * <pre>
     *   如果使用位移操作符，则一般使用括号来提高运算优先级
     * </pre>
     * <p>
     * 6. 逻辑
     * 
     * <pre>
     *   它的操作参数是boolean 类型， 所以一般与关系运算符一起使用
     * </pre>
     * <p>
     * 7. 三目
     * <p>
     * 8. 赋值
     */
    public void priority() {
        int i = (1 * 3 >> 2 & 0xF) > 0 && 3 >> 2 > 0 ? 1 : 0;

        int b = (3 & 3) > 0 && 3 > 0 ? 1 : 0;

        float f = 1f;
        float f2 = 2f;

        double d = Double.NaN;

        // float f3 = f | f2;

        int c = ~i | b;
    }

    public static void eq() {
        Integer i1 = Integer.valueOf(2222);
        Integer i2 = Integer.valueOf(2222);

        System.out.println(i1 == i2);
    }

    public static void main(String[] args) {
        eq();

    }

}
