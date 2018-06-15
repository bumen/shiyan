package com.bmn.spring.bean;

import org.springframework.beans.PropertyEditorRegistry;

import java.beans.PropertyEditor;

/**
 * Created by Administrator on 2017/11/6.
 */
public class QvpPropertyEditorRegistry implements PropertyEditorRegistry {
    @Override
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {

    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor) {

    }

    @Override
    public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath) {
        return null;
    }
}
