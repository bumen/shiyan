## BeanFactory

### getBean
 * bean的创建分为
   + 实例化，加载new Bean
   + 初始化, 装配bean
   + 注册销毁器
   
 * 实例化过程中
   + 先执行实例化前置处理器，如果成功返回bean，则直接执行实例化后置处理器
   bean创建结束
   + 如果执行实例化前置处理器，没有返回bean，则需要创建BeanWrapper
   + 实例化Bean
   + 如果实例化完成，执行实例化后置处理器，如果后置处理器执行失败
   直接返回bean
   + 如果实例化后置处理器执行成功，则装配置BeanProperty
   + 先确定要装配置的Property, 通过byName, byType, 配置文件Property
   执行实例化处理，postProcessPropertyValues
   + 装配Property
   
 * 初始化
   + 实例化完成后
   + 执行初始化前置处理器postProcessBeforeInitialization
   + 执行InitializingBean
   + 执行自定义初始化方法
   + 执行初始化后置处理器postProcessAfterInitialization
   
 * 注册销毁器
   + 不是Prototype类型
   + Singleton需要配置销毁方法或处理器
   + 自定义Scope

 * 通过Scope创建beanObject
 * Scope
   + Singleton
   + Prototype
   + 自定义Scope
 * 根据BeanDefinition解析BeanClass
 * BeanObject初始化之前，可以通过InstantiationAwareBeanPostProcessor
 创建BeanObject, 通过程序自定义改变bean的创建
 且这个创建只创建一次
   + 如果创建成功，则再执行BeanPost后置处理器
 * 创建BeanWrapper
   + 如果是构造函数注入或有参数时使用构造函数创建
   + 没有则使用BeanClass创建
   + 配置PropertyEditor
 * postProcessMergedBeanDefinition
   + 可以修改一个BeanDefinition
 * 