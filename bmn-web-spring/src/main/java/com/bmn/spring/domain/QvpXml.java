package com.bmn.spring.domain;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 */
public class QvpXml {
    private List<String> list;

    @Value("${com.type}")
    private String type;

    public void setList(List<String> list) {
        this.list = list;
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
