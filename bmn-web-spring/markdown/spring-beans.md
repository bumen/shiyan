## BeanInfo

### PropertyEditor
 * 可以配置
   + 类型-> PropertyEditor
   + 属性名 -> 类型-> PropertyEditor
   
   
   
### BeanDefinition解析XML
 * 解析Bean的属性
   + scope: 默认singleton
   + isAbstract: false
   + lazy-init : 默认true
   + autowire: no
   + isPrimary: false
   + autowirecandidate: false
   + dependencycheck:no
   + dependsOn: 
   + initMethod
   + destroyMethod
   + factoryMethod
   + factoryBean

 * 解析Constructor参数
   + 解析配置constructor-arg
   + 配置索引index，指定装配第几个参数。
   + 不配置索引index
 * 解析属性property
   + 必须有名称
   + 所有属性最终封闭成PropertyValue(name, value)
   放到BeanDefinition中
   