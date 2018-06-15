package com.bmn.spring.load;

import org.springframework.beans.*;
import org.springframework.core.convert.TypeDescriptor;

import java.beans.PropertyDescriptor;

/**
 * Created by Administrator on 2017/8/8.
 */
public class QvpBeanWrapper extends AbstractPropertyAccessor implements BeanWrapper {

    private Object object;


    private CachedIntrospectionResults cachedIntrospectionResults;


    public void setWrappedInstance(Object object) {
        setWrappedInstance(object, "", null);
    }

    /**
     * Switch the target object, replacing the cached introspection results only
     * if the class of the new object is different to that of the replaced object.
     * @param object the new target object
     * @param nestedPath the nested path of the object
     * @param rootObject the root object at the top of the path
     */
    public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
        this.object = object;
        //this.typeConverterDelegate = null;//new TypeConverterDelegate(this, object);
        setIntrospectionClass(object.getClass());
    }

    protected void setIntrospectionClass(Class<?> clazz) {
//        if (this.cachedIntrospectionResults != null &&
//                !clazz.equals(this.cachedIntrospectionResults.getBeanClass())) {
//            this.cachedIntrospectionResults = null;
//        }
    }

    private CachedIntrospectionResults getCachedIntrospectionResults() {
//        if (this.cachedIntrospectionResults == null) {
//            this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
//        }
//        return this.cachedIntrospectionResults;
        return null;
    }

    @Override
    public Object getPropertyValue(String propertyName) throws BeansException {
//        return getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
        return null;
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) throws BeansException {

    }

    @Override
    public Object getWrappedInstance() {
        return object;
    }

    @Override
    public Class<?> getWrappedClass() {
        return (this.object != null ? this.object.getClass() : null);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
//        return getCachedIntrospectionResults().getPropertyDescriptors();
        return null;
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
//        return getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
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
    public boolean isReadableProperty(String propertyName) {
        return false;
    }

    @Override
    public boolean isWritableProperty(String propertyName) {
        return false;
    }

    @Override
    public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
        return null;
    }

    @Override
    public Class getPropertyType(String propertyName) {
        try {
            PropertyDescriptor pd = getPropertyDescriptor(propertyName);
            if (pd != null) {
                return pd.getPropertyType();
            }
            else {
                // Maybe an indexed/mapped property...
                Object value = getPropertyValue(propertyName);
                if (value != null) {
                    return value.getClass();
                }
                // Check to see if there is a custom editor,
                // which might give an indication on the desired target type.
                Class<?> editorType = guessPropertyTypeFromEditors(propertyName);
                if (editorType != null) {
                    return editorType;
                }
            }
        }
        catch (InvalidPropertyException ex) {
            // Consider as not determinable.
        }
        return null;
    }
}
