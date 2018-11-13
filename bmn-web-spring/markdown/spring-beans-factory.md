## spring-beans-factory

### factory 类型
 * BeanFactory
   + HierarchicalBeanFactory
   + ListableBeanFactory
   + AutowireCapableBeanFactory
 * FactoryBean
 * ObjectFactory
 
### AutowireCapableBeanFactory
 * 用来装配不属于BeanFactory管理的bean
 
### annotation
 * AnnotatedBeanDefinition
 * @Autowried
 * @Configurable
 * @Qualifier
 * @RequiredAutowiredFieldElement
 * @Value

#### AutowiredAnnotationBeanPostProcessor
 * 通过BeanPostProcessor来处理bean的Annotation
 * 默认处理@Autowried, @Value
 * 作用：
   + 解释bean class的 Filed, Method
   + 获取Field上的@Autowired, @Value注解。封装成AutowiredFieldElement
   + 获取Method上的@Autowired,@value注解。封装成AutowiredMethodElement
   + 最终获取bean上所有要注入的内容。封装成InjectionMetadata
   
#### InitDestroyAnnotationBeanPostProcessor
 * 通过自定义注解
   + 初始化注解：在bean初始化前调用
   + 销毁注解：在bean销毁前调用
   
#### RequiredAnnotationBeanPostProcessor
 * 解析@Required注解
 * 只解析注解在set方法上的@Required