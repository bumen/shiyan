## core-env 包

 * 两个主要接口 
   + PropertyResolver
   + PropertySource
> 通过PropertyResolver使用PropertySource
> 实现框架封装

### PropertyResolver
 > 负责使用PropertySource
 
 1. Environment
  * 应用的环境如：java或java web
  * 获取环境下的变量属性如：SystemPropertys, ServletPropertys
  * 通过适配器模式，来解析PropertySource  
  
 2. PropertySourcesPropertyResolver
  * 负责解析PropertySource

 3. MutablePropertySources
  * 是一个PropertySource集合
  * 提供一个多个PropertySource按顺查找
  
### PropertySource<source>
 * 属性来源的封装
 
 * 多属性来源
   + CommandLine
   + ServletConfig
   + Properties
   + Resource
   
### PropertySourcesPropertyResolver
 * 解析PropertySource
 * 可以解析带占位符的属性: ${user.name}
 解析过程通过PropertyPlaceholderHelper解析占位符，解析后(user.name)的属性来源，可以通过配置不同的来源策略实现获取值
 * 可以解析class类型的属性，可以通过conversionService作类型转换
 
### Envrionment
 * 作用
   + 控制选择哪个profile指定的Bean配置，通过Bean配置生成BeanDefinition
 * 关联
   + PropertySources
   + PropertySourcesPropertyResolver
 * 通过PropertySources中获取profile