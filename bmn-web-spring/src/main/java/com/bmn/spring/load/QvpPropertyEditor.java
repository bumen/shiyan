package com.bmn.spring.load;

import java.beans.PropertyEditorSupport;

/**
 * Created by Administrator on 2017/8/11.
 */
public class QvpPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text == null) {
            return;
        }

        //判断配置参数
        if(text == "zhangsan") {

        }

        super.setAsText(text);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }
}
