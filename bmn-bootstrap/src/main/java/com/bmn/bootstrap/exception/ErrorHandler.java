package com.bmn.bootstrap.exception;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
@FunctionalInterface
public interface ErrorHandler {

    void handleError(Throwable t);
}
