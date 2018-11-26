## spring-beans-factory

### factory 类型
 * BeanFactory
   + HierarchicalBeanFactory
   + ListableBeanFactory
   + AutowireCapableBeanFactory
 * FactoryBean
 * ObjectFactory
 
>> 依赖dependentBeanMap的key是被依赖对象。可以找到我都被谁依赖了
>> 依赖dependenciesForBeanMap的key是依赖对象。可以找到我依赖了谁。
 
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
 
### 创建一个bean
 * 需要beanName, BeanDefinition, args
 1。首先检查Instantitaion BeanPostProcessor如果可以返回。则直接返回bean对象
   + 这个一个bean的实例化前置处理器。可以返回bean的一个代理
   
 2。如果1步失败，则需要Bean实例化
 3。实例化成功后返回BeanWrapper对象
 4。实例化成功允许通过BeanPostProcessor修改BeanDefinition
 5。执行bean实例化后置处理器。
   + 如果处理成功。可以终止bean属性的注入。即直接返回，不进入6步。
 6。
 
#### Bean 实例化：三种实例化方式
 * 按优先级从上到下
 1。通过FactoryMethod实例化
   + 如果BeanDefinition配置了FactoryMethod
 2。通过类构造器实例化
   + 如果getBean时，指定了参数
   + 如果BeanDefinition解析出了构造器参数
   + 如果BeanDefinition配置了按构造器自动装配
   + 如果通过BeanPostProcessor，找到构造器（这也是一种自动装配类型）
 3。java反射或cglib
   + 通过BeanDefinition没有需要重写的方法，则使用默认构造器，通过反射实例化
   + 否则使用cglib通过实现子类实例化
 
 
#### Bean 实例化过程
 1。根据BeanDefinition解析beanClass
   + 通过BeanFactory配置的BeanClassLoader去加载BeanClass
   + BeanClassLoader, 先使用Thread.ContextClassLoader
   如果没有，则使用当前ClassUtils的ClassLoader
   如果没有，则使用systemClassLoader
   
 * 下面接着是通过FactoryMethod或指定构造器或默认构造器三种实例化方式
   + 优先使用FactoryMethod实例化，其次指定构造器，再是默认构造器
   + 同时构造器实例化时，还分通过自定义参数与BeanDefinition解析的参数实例化
   
 2。判断BeanDefinition是否定义了factoryMethod. 如果有，则使用factoryMethod创建
 3。根据beanClass通过BeanPostProcessor确定bean的构造器
 4。如果3步找到构造器或BeanDefinition解析出是通过构造器注入或BeanDefinition有构造器参数或getBean时传入参数
 5。如果4步成功，则通过构造器创建bean
 6。如果4步不成功，则通过反射实例化bean
 
#### BeanDefinition构造器参数：类型与解析
 * 类型：配置时指定index, 与未指定index
 * 解析
   + 把所有参数解析为对应值
   + 如果Bean对象，数组，list, set, map, string, properties
   
 * 特殊String类型的解析
   + 通过TypeConverter进行类型转换
 
#### Bean 实例化通过FactoryMethod
 * 有两种：优先使用第一种
   + 一种是通过FactoryBean类创建。非static方法
   + 一种是通过在Bean中定义static 工厂方法
 1。确定FactoryBean class对象
   + 通过BeanDefinition配置的FactoryBeanName
   + 如果没有配置FactoryBeanName，则当前类BeanClass为FactoryBeanClass
 2。确定调用方法
   + 通过搜索类中所有方法去匹配
   + FactoryBean中搜索非静态方法且方法名与BeanDefinition配置的FactoryMethodName一样
   + Bean中搜索静态方法且方法名与BeanDefinition配置的FactoryMethodName一样
 3。确定参数
   + 参数有两种：一种是自定义参数，与种是BeanDefinition解析出的构造器使用参数
   + 如果配置自定义参数，则优先使用自定义参数
   + 如果没有配置自定义参数，则才使用BeanDefinition构造器参数
   + [看上面的BeanDefinition构造器参数：类型与解析]：对于使用BeanDefinition构造器参数，第1次配置解析参数
   
 4。从所有方法中根据参数个数匹配一个FactoryMethod
   + 如果有自定义参数，则是方法参数个数必须等于自定义参数个数。直接匹配成功使用方法创建Bean
   + 如果没有自定义参数，则是方法参数个数大于等于BeanDefinition解析的构造器参数个数，则去匹配参数类型
   
 5。找到匹配的FactoryMethod后，根据FactoryMethod参数类型去匹配参数值
   + 参数值来源于：一个是BeanDefinition解析出的构造器参数
   + 看6步：匹配过程：看6步。根据FactoryMethod的参数类型和参数名
   如果使用BeanDefinition解析出的构造器参数去匹配成功，则继续匹配。
   如果匹配失败，则看当前Bean是否是配置了构造器注入模式，如果是则自动装配该参数。否则异常
   + 自动装配过程，通过方法的索引第几个参数就拿参数类型去
 6。匹配过程
   + 如果方法没有参数则直接返回匹配成功
   + 如果方法有参数，则遍历所有参数类型去找对应值
 7。根据类型找值
   + 首先去BeanDefinition解析出的构造器参数找匹配值。如果有则继续下一个参数匹配。
   + 如果没有则判断当前BeanDefinition是否定义为按构造器自动装配，如果不是则抛出异常，继续匹配下一个方法，走第4步。
   如果已经是最后一个方法也没匹配成功。则抛出异常。
   + 如果是自动装配，则走自动装配参数方法。看8步。直到解析完所有参数
   + 成功返回，看9步
   
 8。自动装配参数：resolveAutowiredArgument
   + 肯定是方法的参数。通过MethodParameter封装一下，同时通过参数传递的都是依赖。所以抽象出解析依赖接口。这其实就是依赖注入的实现。
   + 通过BeanFactory调用解析依赖接口resolveDependency
   + 解析对应类型的值
 9。此时已经拿创建bean的类，方法，方法参数。通过反射调用。返回bean实例对象
 
#### Bean 实例化通过Constructor
 * 与通过**Bean 实例化通过FactoryMethod**过程一样
 * 通过找到所有可实例化构造器后。遍历每一个去匹配。匹配过程与FactoryMethod一样
 * 最后找到Bean的实例化构造器，方法参数。通过反射创建实例。
   
#### Bean属性注入
 * 注入方式
   + 自动注入：要有setXxx方法
   + 通过写配置注入
 * 自动注入
   + 设置为byName, byType
 * 自动注入：byName
   + 按setXxx方法中的Xxx名称与Bean的名称去匹配
   + 如果set方法参数类型是基本数据类型，不注入
   + 如果set方法参数类型通过手动配置注入过，不注入
   + 如果set方法参数类型ignoredDependencyTypes，ignoredDependencyIntegerfaces中指定的类型对象，不注入
   + 最后生成PropertyValue，添加到BeanDefinition属性列表中
   
 * 自动注入：byType
   + 先按setXxx方法中的Xxx名称与Bean的名称去匹配
   + 如果set方法参数类型是基本数据类型，不注入
   + 如果set方法参数类型通过手动配置注入过，不注入
   + 如果set方法参数类型ignoredDependencyTypes，ignoredDependencyIntegerfaces中指定的类型对象，不注入
   + 找到所有匹配的Xxx属性名，通过属性名拿到PropertyDescriptor。然后拿到参数类型
   + 如果参数类型是Object则不注入。否则去BeanFactory中拿到所有该类型的Bean，按条件取一个对象
   + 注入
   + 当通过参数类型去匹配所有该类型的bean时，如果这个bean配置了属性autowirecandidate = false时，
   不会匹配
   + 从所有匹配的类型的bean中选择一个时优先规则如下
     - 如果bean配置了isPrimary则优先选择
     - 如果有一个primarybean然后再去找是否有是localbean(即本beanFactory注册的bean,不是beanFactory父beanFactory注册的bean)
     优先使用localBean
     - 如果没有primarybean，则选择通过参数名称与bean的名称匹配的bean
   + 最后生成PropertyValue，添加到BeanDefinition属性列表中
   
 * 通过配置注入
   + 通过解析配置属性property解析为PropertyValue，添加到BeanDefinition属性列表中
  
  > 所以BeanDefinition属性列表可能来自上面三个地方解析得到
  
 * beanPostProcessor过滤属性
   + 拿到所有注入的属性后，
   即：所有已经注入过的属性
   + 再拿到过滤ignoredDependencyTypes，ignoredDependencyIntegerfaces中指定的类型对象后的所有PropertyDescripter
   即：所有可以注入的属性
   + 通过beanPostProcessor还有机会操作bean属性
   + 最后返回BeanDefinition属性列表
 * 注入检查 
   + 如果BeanDefinition配置了dependencycheck属性则会检查
   需要检查的类型的属性是否都注入了
 * 解析BeanDefinition属性列表
   + 先对BeanDefinition读取的匹配属性解析
   + 最后对解析的值与所需要注入的属性进行类型转换
  
 * bean的属性注入完成
 
#### AutowireCandidateResolver
  * 就是在自动注入时，判断一个bean是否是可以被自动注入的
  * 通过BeanDefinition, 与要注入的属性比较看是否可被注入
    + 简单实现是直接判断BeanDefinition的autowirecandidate属性值 
    + 复杂时，可以与要注入的属性DependencyDescriptor比较后判断
   
   
#### Bean初始化
 1。执行Bean的感知
   + BeanName, BeanClassLoader, BeanFactory
 2。执行Bean的初始化前置处理器BeanPostProcessor
 3。执行初始化方法
   + InitializingBean
   + 自定义的InitMethod
 4。执行Bean的初始化后置处理器BeanPostProcessor
 
#### Bean配置注销方法
 1。判断Bean是否需要注销
   + 非prototype scope类型的Bean
   + 实现DisposableBean
   + 实现java.lang.AutoCloseable
   + 配置DestroyMethod
   + 有DestructionAwareBeanPostProcessor对象
 2。如果是singleton
   + 会把bean的一个DisposableBeanAdapter对象注册到BeanFactory中
 3。如果是自定义scope
   + 会把bean的一个DisposableBeanAdapter对象传到scope，自己处理
   
#### Bean注销
 * 只注销singleton的bean
 * 从singleton objects cache中删除
 * 先注销依赖这个bean的所有对象
 * 调用bean的destroy方法
 * 从dependenciesForBeanMap删除
 
### 依赖注入
 * 如果一个Spring管理的Bean不想参数自动装配，则需要配置autowire-candidate = false
 
 * 所未依赖就是通过方法参数传递的对象
 * 通过BeanFactory的resolveDependency接口实现
   + DependencyDescriptor 被依赖对象的抽象 
   + beanName 依赖者
   + autowiredBeanNames 已经
   + TypeConverter 对被依赖对象类型转换器
 * 接口实现
   + 如果被依赖对象是ObjectFactory类型，则返回一个ObjrctFactory代理对象
   + 否则解析依赖
   + 首先通过AutowireCandidateResolver解析，成功返回值则直接进行类型转换后返回。
   + 如果被依赖类型是array, collection, map 则会根据元素类型去找匹配值
   + 否则根据被依赖类型找出所有该类型的bean。再去根据bean的名称与依赖属性名匹配找到一个返回
   + 根据被依赖类型找出所有可以注入的类型
   可以配置resolvableDependencies
 
 
### 预热singleton
 * 如果bean是lazyinit = false
 
### TypeConverter
 * 主要是将原始值转目标类型值
 * 三个接口
   + 值-> targetType（这个如果没有找到默认TypeDescriptor，使用ClassDescriptor(targetType)
   + 值-> 方法对应参数的对应类型
   + 值-> 类filed字段对应的类型
 
 