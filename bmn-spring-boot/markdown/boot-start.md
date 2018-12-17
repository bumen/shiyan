## start 启动过程
 * logStartupInfo
   + 自定义配置是否开启打印启动日志

### 启动类
 * 可以使用SpringApplication中的main启动
   + 需要创建子类实现，并设置Source, 才能启动
 * 自定义类中的main启动
    + 通过使用SpringApplication的静态run方法启动
    + 通过SpringApplication app = new SpringApplication()对象，并自定义属性后，通过app.run(args)启动

### 构造SpringApplication
 * 初始化ResourceLoader
 * 初始化primarySources
 * 初始化webApplicationType
   + 通过类路径搜索相关class文件，按优先级
   1. NONE
   2. SERVLET
   3. REACTIVE
   
 * 加载META-INFO/spring.factories
   + spring-boot-2.*.jar
   + spring-boot-autoconfigure-2.*.jar
   + spring-beans-5.*.jar
   
 * 初始化ApplicationContextInitializer, 按order排序后
   + org.springframework.boot.context.config.DelegatingApplicationContextInitializer, spring-boot-2.*.jar
   + org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer, spring-boot-autoconfigure-2.*.jar
   + org.springframework.boot.context.ContextIdApplicationContextInitializer, spring-boot-2.*.jar
   + org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer, spring-boot-2.*.jar
   + org.springframework.boot.web.context.ServerPortInfoApplicationContextInitializer, spring-boot-2.*.jar 
   + org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener, spring-boot-autoconfigure-2.*.jar
   + 可以通过app.addInitializers()方法自定义Initializer。并且按顺序排到最后
   > 可自定义，可以通过spring.factories配置或在run方法执行前通过app.addInitializers方法配置
   
 * 初始化ApplicationListener，按order排序后
   + org.springframework.boot.context.config.ConfigFileApplicationListener, spring-boot-2.*.jar
   + org.springframework.boot.context.config.AnsiOutputApplicationListener, spring-boot-2.*.jar 
   + org.springframework.boot.context.logging.LoggingApplicationListener, spring-boot-2.*.jar 
   + org.springframework.boot.context.logging.ClasspathLoggingApplicationListener, spring-boot-2.*.jar 
   + org.springframework.boot.autoconfigure.BackgroundPreinitializer, spring-boot-autoconfigure-2.*.jar 
   + org.springframework.boot.context.config.DelegatingApplicationListener, spring-boot-2.*.jar 
   + org.springframework.boot.builder.ParentContextCloserApplicationListener, spring-boot-2.*.jar  
   + org.springframework.boot.ClearCachesApplicationListener, spring-boot-2.*.jar
   + org.springframework.boot.context.FileEncodingApplicationListener, spring-boot-2.*.jar 
   + org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener, spring-boot-2.*.jar 
   + 可以通过app.addListeners()方法自定义Listener。并且按顺序排到最后

 * 找main方法所在的类
   + 通过new RuntimeException().getStackTrace()
   + 可以使用SpringApplication中的main启动
   + 自定义类中的main启动
   
### run方法执行
 * 创建SpringApplicationRunListeners
   + 是一个SpringApplicationRunListener的聚合管理工具类，管理所有的SpringApplicationRunListener。实现统一调用
   + 通过一个工具类管理多个listener，使SpringApplication代码更加简洁。
   + 只能通过在META-INFO/spring.factories中配置，注册List<SpringApplicationRunListener> 到SpringApplicationRunListeners
   默认提供了org.springframework.boot.context.event.EventPublishingRunListener, spring-boot-2.*.jar
   都构造方法，传SpringApplication对象拿注册的所有ApplicationListener，并注册到EventPublishingRunListener中，实现统一管理
   > 可自定义SpringApplicationRunListener，只能通过spring.factories
   > 可自定义ApplicationListener， 可以通过spring.factories, 或创建SpringApplication对象后，在run方法执行前通过addListeners方法自定义
 
 * 执行listener.starting()方法
   > 可自定义listener实现staring方法
   
 * 解析main方法参数
   + ApplicationArguments，通过适配SimpleCommandLinePropertySource实现
   + 分为可选参数类似：html中的<select><option/><option/></select>标签，通过--name=v1, --name=v2 实现。
   + 单一参数通过非--开头的所有参数
   
 * 配置Environment
   + 创建Environment, 如果app中自定义Env则选择自定义配置，否则根据ApplicationType选择对应的Environment对象
   + 配置ConversionService
   + 如果app中自定义defaultProperties, 会把defaultProperties放到Environemnt中properties链表最后
   + 如果有main方法参数，会把main方法参数配置到properties链表最开头
   + 如果app中自定义additionalProfiles,会把additionalProfiles放到Environment中旧的profiles链表开头
   + 处发listener#environmentPrepared， 可以在listener中配置Environment
   + bindToSpringApplication，通过环境变量配置来绑定SpringApplication中的属性。
   变量开头为spring.main.属性名=value
   + ConfigurationPropertySources
   > 可自定义Environment， 通过app方法设置
   > 可自定义defaultProperties， 通过app方法设置
   > 可自定义additionalProfiles, 通过app方法设置
   > 可自定义Environment, 通过listener自定义
   > 可自定义app中的属性，通过Environemnt配置spring.main
   > 可自定义通过实现SpringApplication子类，重写configureEnvironment， configurePropertySources，configureProfiles, bindToSpringApplication
 
 * 创建GenericApplicationContext
   + 首先判断是否自定义ApplicationContext
   + 根据ApplicationType为不类型创建不同ApplicationContext
   + 都是Annotation类型的ApplicationContext
   > 可自定义ApplicationContext，通过app方法设置
   > 可自定义通过实现SpringApplication子类，重写createApplicationContext方法
   
 
 * 创建List<SpringBootExceptionReporter>
   + 通过spring.factories中获取，并创建
   + 构造方法会注入GenericApplicationContext
 
 * prepareContext：配置GenericApplicationContext
   + postProcessApplicationContext配置BeanNameGenerator, ResourceLoad, ConversionService
   + 执行ApplicationContextInitializer, 可以自定配置ApplicationContext
   + 执行listener#contextPrepared，可以自定义配置ApplicationContext
   + 注册singleton：ApplicationArguments, springBootBanner
   + 创建BeanDefinitionLoader, 并loadBeanDefinitions
   BeanDefinitions配置的指定来源于：primarySource, 自定义Source
   + 执行listener#contextLoaded, 此时已经有了BeanDefinitions, 可以自定义BeanDefinitions
   > 可自定义-子类重写postProcessApplicationContext
   > 可自定义-子类重写applyInitializers
   > 可自定义-通过ApplicationInitializer配置ApplicationContext
   > 可自定义-通过listener配置ApplicationContext
   > 可自定义-primarySource, stringSource, 通过app#addPrimarySources, #setSources
   primarySource默认通过构造器初始化的，使用的main方法所在类，但如果使用SpringApplication中的main启动时，则没有primarySource
   > 可自定义-子类重写load，加载BeanDefitions方式
   > 可自定义-子类重写createBeanDefinitionLoader，创建BeanDefinitionLoader
   > 可自定义-通过listener配置ApplicationContext中的BeanDefinition
   
 * refresh: ApplicationContext
   + 刷新ApplicationContext
   + 配置ApplicationContext-ShutdownHook
   > 可自定义-子类重写refresh方法
 * afterRefresh
   + 未处理
   > 可自定义-子类重写afterRefresh
 * listener: started
   > 可自定义配置ApplicationContext
 
 * callRunners
   + 通过ApplicationContext获取所有ApplicationRunner, CommandLineRunner
   + 分别执行Runner
   > 可自定义Runner，过滤ApplicationArguments
 * listener: running
   > 可自定义ApplicationContext
   
   
 
   
    