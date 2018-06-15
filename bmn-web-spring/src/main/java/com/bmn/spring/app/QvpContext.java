package com.bmn.spring.app;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Administrator on 2017/5/8.
 */
public class QvpContext implements BeanNameAware, InitializingBean{

    private int id;
    private String name;

    private TestApp testApp;

    public void setTestApp(TestApp testApp) {
        this.testApp = testApp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void pre() {
        System.out.println(QvpContext.class.getName());
    }

    @Override
    public void setBeanName(String name) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

}
