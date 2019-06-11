package com.bmn.bootstrap.exception;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public class BeanInstantiationException extends RuntimeException {

    public BeanInstantiationException(String msg) {
        super(msg);
    }

    public BeanInstantiationException( String msg,  Throwable cause) {
        super(msg, cause);
    }
}
