package com.bmn.spring.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/26.
 */
public class QvpBean {

    private static QvpBean qvpBeanFactory() {
        return new QvpBean();
    }

    private int id;
    private String name;
    private List<String> list;
    private Map<Integer, String> map;
    private List<QvpXml> xmls;

    @Autowired
    @Qualifier("xml")
    private QvpXml qvpXml;

    @Autowired
    private QvpDependencyInterface qvpDependencyInterface;

    @Resource
    private QvpInfection infection;

    public void setQvpXml(QvpXml qvpXml) {

        this.qvpXml = qvpXml;
    }

    public void setInfection(QvpInfection infection) {
        this.infection = infection;
    }

    public QvpBean() {

    }

    public QvpBean(int id, List<QvpXml> xmls) {
        this.id = id;
        this.xmls = xmls;
    }


    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
