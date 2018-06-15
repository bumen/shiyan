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
  
### PropertySource
 * 属性来源的封装
 * 多属性来源
   + CommandLine
   + ServletConfig
   + Properties
   + Resource
 