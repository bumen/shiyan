package com.bmn.rt.vo;

/**
 * Created by Administrator on 2017/8/31.
 */
public interface IPerson {

    String PERSON = "default";

    void savePerson(int id);

    IPerson DEFAULT = new IPerson() {
        @Override
        public void savePerson(int id) {

        }
    };
}
