package com.bmn.spring;

import com.bmn.spring.app.QvpContext;

public class AppServices {

    private static QvpContext qvpContext;

    public static QvpContext getQvpContext() {
        return qvpContext;
    }

    public  void setQvpContext(QvpContext qvpContext) {
        AppServices.qvpContext = qvpContext;
    }
}
