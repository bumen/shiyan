## @Configuration
 * 通过java class 装配BeanDefinition
 * 注解了@Configuration class同时也是一个@Component
 
### 发现资源 
 * 当启动ApplicationContext时，会先加载所有@Component类型的bean（注解了@Configuration也是一个@Component）
 * 启动下一步执行BeanFactoryPostProcessor。（通过配置可以处理@configuration注解的processor）
 * 执行BeanFactoryPostProcessor后拿到所有bean拿过滤有@configuration注解的bean
 * 解析这个bean
 * 封装成BeanDefinition注册到BeanFactory