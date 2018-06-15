package com.bmn.spring.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/23.
 */
public class QvpBeanDefinitionRegistry implements BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beans = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        beans.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        beans.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beans.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beans.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return StringUtils.toStringArray(this.beans.keySet());
    }

    @Override
    public int getBeanDefinitionCount() {
        return beans.size();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return isAlias(beanName) || containsBeanDefinition(beanName);
    }

    @Override
    public void registerAlias(String name, String alias) {

    }

    @Override
    public void removeAlias(String alias) {

    }

    @Override
    public boolean isAlias(String beanName) {
        return false;
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }
}
