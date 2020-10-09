package com.bmn.core.stats;

/**
 * @date 2020-07-17
 * @author zhangyuqiang02@playcrab.com
 */
public interface StatsErrorCode {

    /**
     * 成功
     */
    int SUCCESS_CODE = 0;


    /**
     *
     * @return 错误码
     */
    int code();

    /**
     *
     * @return 错误码名称
     */
    String codeName();
}
