package com.bmn.spring.bean;

import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ARG_TYPE_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.ARG_TYPE_MATCH_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_AUTODETECT_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_BY_NAME_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_BY_TYPE_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_CANDIDATE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_CONSTRUCTOR_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.AUTOWIRE_NO_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.BEAN_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.CLASS_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.CONSTRUCTOR_ARG_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_AUTOWIRE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_DESTROY_METHOD_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_INIT_METHOD_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_LAZY_INIT_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_MERGE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEFAULT_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEPENDENCY_CHECK_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
    .DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
    .DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DEPENDS_ON_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.DESTROY_METHOD_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.FACTORY_BEAN_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.FACTORY_METHOD_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.FALSE_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.INDEX_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.INIT_METHOD_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.KEY_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.LAZY_INIT_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.LOOKUP_METHOD_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.META_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.NAME_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.PARENT_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.PRIMARY_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.PROPERTY_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.QUALIFIER_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REF_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REPLACED_METHOD_ELEMENT;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.REPLACER_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.SCOPE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.SINGLETON_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.TRUE_VALUE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.TYPE_ATTRIBUTE;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.VALUE_ATTRIBUTE;

import com.bmn.spring.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.LookupOverride;
import org.springframework.beans.factory.support.MethodOverrides;
import org.springframework.beans.factory.support.ReplaceOverride;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.springframework.util.ClassUtils;
import org.springframework.util.PatternMatchUtils;

/**
 * Created by Administrator on 2017/10/25.
 *
 * 构建 BeanDefinition
 */
public class QvpBeanDefinitionBuilder {

    public static final String HAS_NOT = "hasn't";

    private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();

    private static Map<String, String> defaultsAttributes = new HashMap<>();
    private static Map<String, String> beanAttributes = new HashMap<>();

    public static void build() throws ClassNotFoundException {
        QvpBeanDefinitionBuilder builder = new QvpBeanDefinitionBuilder();
        BeanElement beanElement = builder.buildBeanElement();
        builder.initDefaults();
        AbstractBeanDefinition beanDefinition = builder
            .parseBeanDefinition(beanElement, "qvpContext");

    }

    /**
     * <bean id="qvpContext" class="com.qvp.app.QvpContext"></bean> 模拟xml配置的bean element
     */
    public static class BeanElement {

        private Map<String, String> attrs = new HashMap<>();  //bean 上的属性，用户配置的

        private List<BeanElement> children = new ArrayList<>();    //bean 下的子元素

        private String text;
        private String elementName;

        public static BeanElement build(String elementName) {
            return build(elementName, null);
        }

        public static BeanElement build(String elementName, String text) {
            return new BeanElement(elementName, text);
        }

        public BeanElement(String elementName, String text) {
            this.elementName = elementName;
            this.text = text;
        }

        public BeanElement(String elementName) {
            this(elementName, null);
        }

        public String getAttribute(String key) {
            if (hasAttribute(key)) {
                return attrs.get(key);
            }
            //默认
            return beanAttributes.get(key);
        }

        private boolean hasAttribute(String key) {
            String v = attrs.get(key);
            return !(v == null || v.equals(HAS_NOT));
        }

        public BeanElement attr(String key, String value) {
            attrs.put(key, value);
            return this;
        }

        public BeanElement addElement(BeanElement eleAttr) {
            this.children.add(eleAttr);
            return this;
        }

        public List<BeanElement> elements() {
            return this.children;
        }

        public String getText() {
            return this.text;
        }

        public boolean match(String elementName) {
            return this.elementName.equals(elementName);
        }
    }

    static {
        //全局bean 属性 <beans>
        defaultsAttributes.put(DEFAULT_LAZY_INIT_ATTRIBUTE, FALSE_VALUE);
        defaultsAttributes.put(DEFAULT_MERGE_ATTRIBUTE, FALSE_VALUE);
        defaultsAttributes.put(DEFAULT_AUTOWIRE_ATTRIBUTE, AUTOWIRE_NO_VALUE);
        defaultsAttributes.put(DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE, "none");
        defaultsAttributes.put(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE, HAS_NOT);
        defaultsAttributes.put(DEFAULT_INIT_METHOD_ATTRIBUTE, HAS_NOT);
        defaultsAttributes.put(DEFAULT_DESTROY_METHOD_ATTRIBUTE, HAS_NOT);

        //bean所有属性的默认值
        beanAttributes.put(CLASS_ATTRIBUTE, "");
        beanAttributes.put(PARENT_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(SCOPE_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(SINGLETON_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(ABSTRACT_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(LAZY_INIT_ATTRIBUTE, DEFAULT_VALUE);
        beanAttributes.put(AUTOWIRE_ATTRIBUTE, DEFAULT_VALUE);
        beanAttributes.put(DEPENDENCY_CHECK_ATTRIBUTE, DEFAULT_VALUE);
        beanAttributes.put(DEPENDS_ON_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(AUTOWIRE_CANDIDATE_ATTRIBUTE, "");
        beanAttributes.put(PRIMARY_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(INIT_METHOD_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(DESTROY_METHOD_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(FACTORY_METHOD_ATTRIBUTE, HAS_NOT);
        beanAttributes.put(FACTORY_BEAN_ATTRIBUTE, HAS_NOT);

    }

    public BeanElement buildBeanElement() {
        BeanElement beanElement = new BeanElement(BEAN_ELEMENT);
        //加入属性
        beanElement.attr(CLASS_ATTRIBUTE, "com.qvp.app.QvpContext");

        //加入子元素
        beanElement.addElement(
            BeanElement.build(META_ELEMENT).attr(KEY_ATTRIBUTE, "a").attr(VALUE_ATTRIBUTE, "b"));
        beanElement.addElement(BeanElement.build(LOOKUP_METHOD_ELEMENT).attr(NAME_ATTRIBUTE, "haha")
            .attr(BEAN_ELEMENT, "beanRef"));
        beanElement.addElement(
            BeanElement.build(REPLACED_METHOD_ELEMENT)
                .attr(NAME_ATTRIBUTE, "haha")
                .attr(REPLACER_ATTRIBUTE, "replaced")
                .addElement(
                    BeanElement.build(ARG_TYPE_ELEMENT, null)
                        .attr(ARG_TYPE_MATCH_ATTRIBUTE, "matchs")
                )
        );
        beanElement.addElement(
            BeanElement.build(CONSTRUCTOR_ARG_ELEMENT)
                .attr(INDEX_ATTRIBUTE, "0")
                .attr(TYPE_ATTRIBUTE, "int")
                .attr(NAME_ATTRIBUTE, HAS_NOT)
                .attr(REF_ATTRIBUTE, HAS_NOT)
                .attr(VALUE_ATTRIBUTE, "1000"));
        beanElement.addElement(BeanElement.build(PROPERTY_ELEMENT)
            .attr(NAME_ATTRIBUTE, "id")
            .attr(REF_ATTRIBUTE, HAS_NOT)
            .attr(VALUE_ATTRIBUTE, "1000-100"));
        beanElement.addElement(
            BeanElement.build(QUALIFIER_ELEMENT).attr(TYPE_ATTRIBUTE, "typeName")
                .attr(VALUE_ATTRIBUTE, HAS_NOT)
                .addElement(
                    BeanElement.build(QUALIFIER_ATTRIBUTE_ELEMENT).attr(KEY_ATTRIBUTE, "key")
                        .attr(VALUE_ATTRIBUTE, "value")
                )
        );

        return beanElement;
    }

    public void initDefaults() {
        //parse root element <beans lazy-init=true>
        String lazyInit = getDefaultAttribute(DEFAULT_LAZY_INIT_ATTRIBUTE);
        if (DEFAULT_VALUE.equals(lazyInit)) {
            lazyInit = "false";
        }
        defaults.setLazyInit(lazyInit);

        String merge = getDefaultAttribute(DEFAULT_MERGE_ATTRIBUTE);
        if (DEFAULT_VALUE.equals(merge)) {
            merge = FALSE_VALUE;
        }
        defaults.setMerge(merge);

        String autowire = getDefaultAttribute(DEFAULT_AUTOWIRE_ATTRIBUTE);
        if (DEFAULT_VALUE.equals(autowire)) {
            autowire = FALSE_VALUE;
        }
        defaults.setAutowire(autowire);

        defaults.setDependencyCheck(getDefaultAttribute(DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE));

        if (hasDefaultAttribute(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE)) {
            defaults
                .setAutowireCandidates(getDefaultAttribute(DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE));
        }

        if (hasDefaultAttribute(DEFAULT_INIT_METHOD_ATTRIBUTE)) {
            defaults.setAutowireCandidates(getDefaultAttribute(DEFAULT_INIT_METHOD_ATTRIBUTE));
        }

        if (hasDefaultAttribute(DEFAULT_DESTROY_METHOD_ATTRIBUTE)) {
            defaults.setAutowireCandidates(getDefaultAttribute(DEFAULT_DESTROY_METHOD_ATTRIBUTE));
        }

        defaults.setSource("qvpBeanDefinitionBuilder");
    }

    public AbstractBeanDefinition parseBeanDefinition(BeanElement ele, String beanName) {

        String className = null;
        if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
            className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
        }

        try {
            String parent = null;
            if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
                parent = ele.getAttribute(PARENT_ATTRIBUTE);
            }

            AbstractBeanDefinition db = createBeanDefinition(parent, className, null);
            parseBeanDefinitionAttributes(ele, beanName, db);

            parseMeta(ele, db);
            parseLookupMethod(ele, db.getMethodOverrides());
            parseReplaceMethod(ele, db.getMethodOverrides());

            parseConstructorArgument(ele, db);
            parseProperties(ele, db);
            parseQualifier(ele, db);

            db.setResource(null);
            db.setSource("bean definition finish");
            return db;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseMeta(BeanElement ele, BeanMetadataAttributeAccessor attributeAccessor) {
        for (BeanElement eleAttr : ele.elements()) {
            if (eleAttr.match(META_ELEMENT)) {
                BeanMetadataAttribute attribute = new BeanMetadataAttribute(
                    eleAttr.getAttribute(KEY_ATTRIBUTE), eleAttr.getAttribute(VALUE_ATTRIBUTE));
                attribute.setSource("meta element");
                attributeAccessor.addMetadataAttribute(attribute);
            }
        }
    }

    private void parseLookupMethod(BeanElement ele, MethodOverrides overrides) {
        for (BeanElement eleAttr : ele.elements()) {
            if (eleAttr.match(LOOKUP_METHOD_ELEMENT)) {
                LookupOverride override = new LookupOverride(eleAttr.getAttribute(NAME_ATTRIBUTE),
                    eleAttr.getAttribute(BEAN_ELEMENT));
                override.setSource("lookup method");
                overrides.addOverride(override);
            }
        }
    }

    private void parseReplaceMethod(BeanElement ele, MethodOverrides overrides) {
        for (BeanElement eleAttr : ele.elements()) {
            if (eleAttr.match(REPLACED_METHOD_ELEMENT)) {
                ReplaceOverride replaceOverride = new ReplaceOverride(
                    eleAttr.getAttribute(NAME_ATTRIBUTE), eleAttr.getAttribute(REPLACER_ATTRIBUTE));

                for (BeanElement attr : eleAttr.elements()) {
                    if (attr.match(ARG_TYPE_ELEMENT)) {
                        String match = attr.getAttribute(ARG_TYPE_MATCH_ATTRIBUTE);
                        match = !StringUtils.isEmpty(match) ? match : attr.getText();
                        if (!StringUtils.isEmpty(match)) {
                            replaceOverride.addTypeIdentifier(match);
                        }
                    }
                }
                replaceOverride.setSource("replace method");

                overrides.addOverride(replaceOverride);
            }
        }
    }

    private void parseConstructorArgument(BeanElement ele, BeanDefinition bd) {
        for (BeanElement eleAttr : ele.elements()) {
            if (eleAttr.match(CONSTRUCTOR_ARG_ELEMENT)) {
                String indexAttr = eleAttr.getAttribute(INDEX_ATTRIBUTE);
                String typeAttr = eleAttr.getAttribute(TYPE_ATTRIBUTE);
                String nameAttr = eleAttr.getAttribute(NAME_ATTRIBUTE);

                if (!StringUtils.isEmpty(indexAttr)) {
                    int index = Integer.parseInt(indexAttr);
                    if (index < 0) {
                        throw new IllegalArgumentException();
                    }

                    Object object = parsePropertyValue(eleAttr, bd, null);
                    ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(
                        object);
                    if (!StringUtils.isEmpty(typeAttr)) {
                        valueHolder.setType(typeAttr);
                    }

                    if (!StringUtils.isEmpty(nameAttr)) {
                        valueHolder.setName(nameAttr);
                    }
                    valueHolder.setSource("constructor argument");
                    if (bd.getConstructorArgumentValues().hasIndexedArgumentValue(index)) {
                        throw new IllegalArgumentException();
                    } else {
                        bd.getConstructorArgumentValues()
                            .addIndexedArgumentValue(index, valueHolder);
                    }
                } else {
                    Object object = parsePropertyValue(eleAttr, bd, null);
                    ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(
                        object);
                    if (!StringUtils.isEmpty(typeAttr)) {
                        valueHolder.setType(typeAttr);
                    }

                    if (!StringUtils.isEmpty(nameAttr)) {
                        valueHolder.setName(nameAttr);
                    }
                    valueHolder.setSource("constructor argument");
                    bd.getConstructorArgumentValues().addGenericArgumentValue(valueHolder);
                }
            }
        }
    }

    private void parseProperties(BeanElement ele, BeanDefinition bd) {
        for (BeanElement eleAttr : ele.elements()) {
            if (eleAttr.match(PROPERTY_ELEMENT)) {
                String propertyName = eleAttr.getAttribute(NAME_ATTRIBUTE);
                if (StringUtils.isEmpty(propertyName)) {
                    throw new IllegalArgumentException();
                }

                if (bd.getPropertyValues().contains(propertyName)) {
                    throw new IllegalArgumentException();
                }
                Object value = parsePropertyValue(eleAttr, bd, propertyName);
                PropertyValue pv = new PropertyValue(propertyName, value);
                parseMeta(eleAttr, pv);
                pv.setSource("properties");
                bd.getPropertyValues().addPropertyValue(pv);
            }
        }
    }

    private void parseQualifier(BeanElement ele, AbstractBeanDefinition bd) {
        for (BeanElement eleAttr : ele.elements()) {
            if (eleAttr.match(QUALIFIER_ELEMENT)) {
                String typeName = eleAttr.getAttribute(TYPE_ATTRIBUTE);
                if (StringUtils.isEmpty(typeName)) {
                    throw new IllegalArgumentException();
                }
                AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(typeName);
                qualifier.setSource("qualifier");
                String value = ele.getAttribute(VALUE_ATTRIBUTE);
                if (!StringUtils.isEmpty(value)) {
                    qualifier.setAttribute(AutowireCandidateQualifier.VALUE_KEY, value);
                }

                for (BeanElement attr : eleAttr.elements()) {
                    if (attr.match(QUALIFIER_ATTRIBUTE_ELEMENT)) {
                        String attributeName = attr.getAttribute(KEY_ATTRIBUTE);
                        String attributeValue = attr.getAttribute(VALUE_ATTRIBUTE);
                        if (!StringUtils.isEmpty(attributeName) && !StringUtils
                            .isEmpty(attributeValue)) {
                            BeanMetadataAttribute attribute = new BeanMetadataAttribute(
                                attributeName, attributeValue);
                            attribute.setSource("qualifier meta");
                            qualifier.addMetadataAttribute(attribute);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                }
                bd.addQualifier(qualifier);
            }
        }
    }

    private Object parsePropertyValue(BeanElement ele, BeanDefinition bd, String propertyName) {

        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);
        /**
         * 1. 不能同时有 ref 与 value
         * 2. 不能同时有 subElement 与 ref 或 value
         */
        if ((hasRefAttribute && hasValueAttribute) ||
            ((hasValueAttribute || hasValueAttribute) && !ele.elements().isEmpty())) {
            throw new IllegalArgumentException();
        }

        if (hasRefAttribute) {
            RuntimeBeanReference bRef = new RuntimeBeanReference(ele.getAttribute(REF_ATTRIBUTE));
            bRef.setSource("constructor argument ref");
            return bRef;
        } else if (hasValueAttribute) {
            TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute(VALUE_ATTRIBUTE));
            valueHolder.setSource("constructor argument value");
            return valueHolder;
        } else {
            //parseSubElement
            throw new IllegalArgumentException();
        }
    }

    private void parseBeanDefinitionAttributes(BeanElement ele, String beanName,
        AbstractBeanDefinition bd) {

        if (ele.hasAttribute(SCOPE_ATTRIBUTE)) {
            // Spring 2.x "scope" attribute
            bd.setScope(ele.getAttribute(SCOPE_ATTRIBUTE));
            if (ele.hasAttribute(SINGLETON_ATTRIBUTE)) {
                //error("Specify either 'scope' or 'singleton', not both", ele);
            }
        } else if (ele.hasAttribute(SINGLETON_ATTRIBUTE)) {
            // Spring 1.x "singleton" attribute
            bd.setScope(TRUE_VALUE.equals(ele.getAttribute(SINGLETON_ATTRIBUTE)) ?
                BeanDefinition.SCOPE_SINGLETON : BeanDefinition.SCOPE_PROTOTYPE);
        }

        if (ele.hasAttribute(ABSTRACT_ATTRIBUTE)) {
            bd.setAbstract(TRUE_VALUE.equals(ele.getAttribute(ABSTRACT_ATTRIBUTE)));
        }

        String lazyInit = ele.getAttribute(LAZY_INIT_ATTRIBUTE);
        if (DEFAULT_VALUE.equals(lazyInit)) {
            lazyInit = this.defaults.getLazyInit();
        }
        bd.setLazyInit(TRUE_VALUE.equals(lazyInit));

        String autowire = ele.getAttribute(AUTOWIRE_ATTRIBUTE);
        bd.setAutowireMode(getAutowireMode(autowire));

        String dependencyCheck = ele.getAttribute(DEPENDENCY_CHECK_ATTRIBUTE);
        bd.setDependencyCheck(getDependencyCheck(dependencyCheck));

        if (ele.hasAttribute(DEPENDS_ON_ATTRIBUTE)) {
            String dependsOn = ele.getAttribute(DEPENDS_ON_ATTRIBUTE);
            bd.setDependsOn(
                StringUtils.tokenizeToStringArray(dependsOn, MULTI_VALUE_ATTRIBUTE_DELIMITERS));
        }

        String autowireCandidate = ele.getAttribute(AUTOWIRE_CANDIDATE_ATTRIBUTE);
        if ("".equals(autowireCandidate) || DEFAULT_VALUE.equals(autowireCandidate)) {
            String candidatePattern = this.defaults.getAutowireCandidates();
            if (candidatePattern != null) {
                String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
                bd.setAutowireCandidate(PatternMatchUtils.simpleMatch(patterns, beanName));
            }
        } else {
            bd.setAutowireCandidate(TRUE_VALUE.equals(autowireCandidate));
        }

        if (ele.hasAttribute(PRIMARY_ATTRIBUTE)) {
            bd.setPrimary(TRUE_VALUE.equals(ele.getAttribute(PRIMARY_ATTRIBUTE)));
        }

        if (ele.hasAttribute(INIT_METHOD_ATTRIBUTE)) {
            String initMethodName = ele.getAttribute(INIT_METHOD_ATTRIBUTE);
            if (!"".equals(initMethodName)) {
                bd.setInitMethodName(initMethodName);
            }
        } else {
            if (this.defaults.getInitMethod() != null) {
                bd.setInitMethodName(this.defaults.getInitMethod());
                bd.setEnforceInitMethod(false);
            }
        }

        if (ele.hasAttribute(DESTROY_METHOD_ATTRIBUTE)) {
            String destroyMethodName = ele.getAttribute(DESTROY_METHOD_ATTRIBUTE);
            if (!"".equals(destroyMethodName)) {
                bd.setDestroyMethodName(destroyMethodName);
            }
        } else {
            if (this.defaults.getDestroyMethod() != null) {
                bd.setDestroyMethodName(this.defaults.getDestroyMethod());
                bd.setEnforceDestroyMethod(false);
            }
        }

        if (ele.hasAttribute(FACTORY_METHOD_ATTRIBUTE)) {
            bd.setFactoryMethodName(ele.getAttribute(FACTORY_METHOD_ATTRIBUTE));
        }
        if (ele.hasAttribute(FACTORY_BEAN_ATTRIBUTE)) {
            bd.setFactoryBeanName(ele.getAttribute(FACTORY_BEAN_ATTRIBUTE));
        }
    }

    public static AbstractBeanDefinition createBeanDefinition(
        String parentName, String className, ClassLoader classLoader)
        throws ClassNotFoundException {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setParentName(parentName);
        if (className != null) {
            if (classLoader != null) {
                bd.setBeanClass(ClassUtils.forName(className, classLoader));
            } else {
                bd.setBeanClassName(className);
            }
        }
        return bd;
    }

    public int getAutowireMode(String attValue) {
        String att = attValue;
        if (DEFAULT_VALUE.equals(att)) {
            att = this.defaults.getAutowire();
        }
        int autowire = AbstractBeanDefinition.AUTOWIRE_NO;
        if (AUTOWIRE_BY_NAME_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_BY_NAME;
        } else if (AUTOWIRE_BY_TYPE_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_BY_TYPE;
        } else if (AUTOWIRE_CONSTRUCTOR_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR;
        } else if (AUTOWIRE_AUTODETECT_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_AUTODETECT;
        }
        // Else leave default value.
        return autowire;
    }

    public int getDependencyCheck(String attValue) {
        String att = attValue;
        if (DEFAULT_VALUE.equals(att)) {
            att = this.defaults.getDependencyCheck();
        }
        if (DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE.equals(att)) {
            return AbstractBeanDefinition.DEPENDENCY_CHECK_ALL;
        } else if (DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE.equals(att)) {
            return AbstractBeanDefinition.DEPENDENCY_CHECK_OBJECTS;
        } else if (DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE.equals(att)) {
            return AbstractBeanDefinition.DEPENDENCY_CHECK_SIMPLE;
        } else {
            return AbstractBeanDefinition.DEPENDENCY_CHECK_NONE;
        }
    }

    private static boolean hasDefaultAttribute(String key) {
        String v = defaultsAttributes.get(key);
        return !(v == null || v.equals(HAS_NOT));
    }

    private static String getDefaultAttribute(String key) {
        return defaultsAttributes.get(key);
    }
}
