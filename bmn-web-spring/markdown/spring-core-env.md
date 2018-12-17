## core-env 包

 * 两个主要接口 
   + PropertyResolver
   + PropertySource
> 通过PropertyResolver使用PropertySource
> 实现框架封装

### PropertyResolver
 > 负责使用PropertySource
 * 提供，获取，查询property属性的接口
 
 1. Environment
  * 应用的环境如：java或java web
  * 获取环境下的变量属性如：SystemPropertys, ServletPropertys
  * 通过适配器模式，来解析PropertySource  
  
 2. PropertySourcesPropertyResolver
  * 负责解析PropertySource

 3. MutablePropertySources
  * 是一个PropertySource集合
  * 提供一个多个PropertySource按顺查找
 
### ConfigurablePropertyResolver
 * 提供配置解析property接口。
   + 可以配置类型转换器
   + 可以配置可以解析的placeholder 
 
### PropertySource<source>
 * 属性来源的封装
 
 * 多属性来源
   + CommandLine
   + ServletConfig
   + Properties
   + Resource
   + system property
   + system env
   + map
   
### PropertySourcesPropertyResolver
 * 解析PropertySource
 * 可以解析带占位符的属性: ${user.name}
 解析过程通过PropertyPlaceholderHelper解析占位符，解析后(user.name)的属性来源，可以通过配置不同的来源策略实现获取值
 * 可以解析class类型的属性，可以通过conversionService作类型转换

#### PropertyPlaceholderHelper
 * 字符串占位符解析器：分严格模式，非严格模式
 * 可以解析的字符串 
   + ${xxx}: 解析xxx, 如果xxx对应property也是一个${bbb}. 则再去解析bbb.
   + ${xxx${bbb}xxxx}：先解析xxx${}xxx，然后递归解析bbb。
   + ${key:value}: 先解析key:value去查找property，如果有则返回。没有则拿key去查找，如果有则返回，没有则返回value.
 * 解析过程
   (a)先解析字符串，通过递归调用，从外向内查找，先解析最内层占位符
   (b)获取占位符后，去属性来源中查找属性值。
   (c)如果没找值，则看看占位符是否是key:value形式，如果是则通过key查找属性值，如果有值则返回。没有则把value做为值返回
   (d)如果找到值，则再根据值解析值字符串占位符
   (e)最后用值从内层一值替换到最外层。直到解析完所有占位符
   (f)当解析到最外层时，没有找到值，则之前内层的解析也不会返回。直接返回原始字符串
   (g)返回最终值
 * 严格模式
   + 解析不到value则throw exception
 * 非严格模式
   + 解析不到value则原样返回

### Environment
 * 有两个方面的作用
   1. profiles 
     + 获取active profiles; 如果没有active的，则获取default profiles; 判断某个profile是否被active
   2. properties
     + 由于Environment 继承了PropertyResolver接口，所以它同时具备获取property变量功能
     + 最终实现功能是通过组合关系实现
     + 相当于通过适配器模式实现
   
 * 关联
   + PropertySources
   + PropertySourcesPropertyResolver
   
 * 通过PropertySources中获取active profile
 
#### ConfigurableEnvironment
 * 可配置的。增加了两面功能：配置profile，由于实现了ConfigurablePropertyResolver所以可以同时配置解析property
 
 
#### CommandLineArgs
 * main方法args参数抽象
 * 有两类参数，多选参数，单一参数
 * 如args
   + --foo=bar --foo=tow  single one two 
 * 作为PropertySource使用时
   + 获取单一参数，会使用确定名称，返回所有单一参数逗号分隔如：single,one,two
   + 获取多选参数，通过名称获取所有值逗号分隔如：foo=bar,tow
   
#### 标准Environment
 * 只有系统参数，环境变量。两类属性