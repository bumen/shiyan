package com.bmn.spring.bean;

import org.springframework.beans.*;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/26.
 */
public class QvpBeanWrapper implements BeanWrapper{


    @Override
    public Object getWrappedInstance() {
        return null;
    }

    @Override
    public Class<?> getWrappedClass() {
        return null;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[0];
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
        return null;
    }

    @Override
    public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {

    }

    @Override
    public boolean isAutoGrowNestedPaths() {
        return false;
    }

    @Override
    public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {

    }

    @Override
    public int getAutoGrowCollectionLimit() {
        return 0;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {

    }

    @Override
    public ConversionService getConversionService() {
        return null;
    }

    @Override
    public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {

    }

    @Override
    public boolean isExtractOldValueForEditor() {
        return false;
    }

    @Override
    public boolean isReadableProperty(String propertyName) {
        return false;
    }

    @Override
    public boolean isWritableProperty(String propertyName) {
        return false;
    }

    @Override
    public Class getPropertyType(String propertyName) throws BeansException {
        return null;
    }

    @Override
    public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
        return null;
    }

    @Override
    public Object getPropertyValue(String propertyName) throws BeansException {
        return null;
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) throws BeansException {

    }

    @Override
    public void setPropertyValue(PropertyValue pv) throws BeansException {

    }

    @Override
    public void setPropertyValues(Map<?, ?> map) throws BeansException {

    }

    @Override
    public void setPropertyValues(PropertyValues pvs) throws BeansException {

    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {

    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid) throws BeansException {

    }

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

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        return null;
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam) throws TypeMismatchException {
        return null;
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws TypeMismatchException {
        return null;
    }
}
