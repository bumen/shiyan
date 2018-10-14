## ApplicationContext

### 启动
 * 初始化PropertySource
 * 创建BeanFactory加载bean配置文件，生成BeanDefinition
 * 配置BeanFactory
   + BeanClassLoader
   + BeanExpressionResolver
   + PropertyEditorRegistrar
   + BeanPostProcessor
   + ignoreDependencyInterface
   + registerResolvableDependency
 * post BeanFactory
   + BeanDefinitionRegistryPostProcessor
     - 用于自定义向BeanFactory注册BeanDefinition
   + BeanFactoryPostProcessor
     - 用于配置BeanFactory
     - 获取BeanDefinition
 * 根据BeanFactory中BeanDefinition，找到所有BeanPostProcessor
 按优先级，顺序注册到BeanFactory中
 * 如果BeanFactory中有MessageSource的BeanDefinition，
 则生成MessageSource对象，并注册到BeanFactory
 
 * initApplicationEventMulticaster
   + 如果BeanFactory配置有，则使用已配置的
   + 如果没有则新创建
 * 向ApplicationEventMulticaster注册ApplicationListener
 * 完成BeanFactory初始
   + 生成单例bean对象，如果Bean配置了LazyInit = false
 * 