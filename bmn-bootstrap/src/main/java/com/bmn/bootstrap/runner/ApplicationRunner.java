package com.bmn.bootstrap.runner;

import com.bmn.bootstrap.context.BmnApplicationContext;

/**
 *
 * 服务器启动成功后调用。负责加载外部模块
 *
 * @author 562655151@qq.com
 * @date 2019/5/22
 */
public interface ApplicationRunner {
    void run(BmnApplicationContext context, String[] args) throws Exception;
}
