## beans-xml包


### Sax解析
 1. InputSource
 2. EntityResolver
 3. ErrorHandler
 4. validationMode
 5. namespaceAware
 6. Document
 7. DocumentLoader
### XmlBeanDefinitionReader
 
 * 解析xml配置文件
 * BeanDefinitionDocumentReader
   + 负责profile 与环境判断是否匹配
 * BeanDefinitionParserDelegate
   + 负责解析<beans></beans>下默认命名空间下的配置
 * NamespaceHandlerResolver 
   + 负责解析自定义名称空间元素
 