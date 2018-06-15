package com.bmn.spring.bean;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 通过BeanDefinitionReader 获取的BeanDefinition最后通过BeanDefinitionRegistry注册接口，保存上BeanDefinition
 *
 * (而DefaultListableBeanFactory实现了BeanDefinitionRegistry,所以它可以保存BeanDefinition, 并使用BeanDefinition)
 * Created qvp
 */
public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);
    private final List<String> beanDefinitionNames = new ArrayList<String>(64);

    private final Map<String, String> aliasMap = new ConcurrentHashMap<String, String>(16);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        beanDefinitionMap.put(beanName, beanDefinition);
        beanDefinitionNames.add(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        beanDefinitionMap.remove(beanName);
        beanDefinitionNames.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[beanDefinitionNames.size()]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return false;
    }

    @Override
    public void registerAlias(String name, String alias) {
        aliasMap.put(alias, name);
    }

    @Override
    public void removeAlias(String alias) {
        aliasMap.remove(alias);
    }

    @Override
    public boolean isAlias(String alias) {
        return aliasMap.containsKey(alias);
    }

    @Override
    public String[] getAliases(String name) {
        List<String> result = new ArrayList<String>();
        synchronized (this.aliasMap) {
            retrieveAliases(name, result);
        }
        return result.toArray(new String[result.size()]);
    }

    private void retrieveAliases(String beanName, List<String> result) {
        for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
            String registeredName = entry.getValue();
            if (registeredName.equals(beanName)) {
                String alias = entry.getKey();
                result.add(alias);
                retrieveAliases(alias, result);
            }
        }
    }
}
