
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.rt.lang;

import java.nio.CharBuffer;

/**
 * 
 * lang 包下的接口
 *
 * @date 2018-04-08
 * @author 562655151@qq.com
 */
public interface BmnLangInterface {

    Appendable append(char c);

    AutoCloseable close();

    CharSequence sequence();

    Cloneable clone();

    <T> Comparable<T> compareTo(T t);

    <T> Iterable<T> iterator();

    Readable read(CharBuffer buffer);

    Runnable run();

    Thread.UncaughtExceptionHandler uncaughtException(Thread t, Throwable e);

}
