## spring-core-io

### Resource 资源 
 * 抽象：
   + 代表资源的文件
   + 资源是否存在
   + 资源大小
   + 资源流
 * 资源分类
   + 可写资源
      - WritableResource
   + 进程内资源 
      - ByteArrayResource
      - ClassPathResource
   + 进程外文件系统资源 
      - FileSystemResource
   + 远程资源
      - UrlResource
   + 带上下文的资源
      - ContextResrouce
      - ServletContextResource
      
### 通过URL代表资源 protocal类型
 * jar
 * zip
 * wsjar
 * vfszip
 * vfsfile
 * vfs
 * code-source
 * file
   + 如："file:/E:/project/bmn/shiyan/bmn-web-spring/config/a.properties"
 * 路径”..“(表示退回上层目录), ”.“(表示当前目录)
   + "file:/e:/project/bmn/shiyan/../config/a.properties"
   转换后："file:/e:/project/bmn/config/a.properties"
   + "file:/e:/project/bmn/shiyan/./config/a.properties"
   转换后："file:/e:/project/bmn/shiyan/config/a.properties"
   
   
   

### AbstractFileResolvingResource
 * 通过url获取的资源
 * UrlResource
 * ClassPathResource
 * ServletPathResource
 
### ResourceUtils
 * 资源工具类
 
### /META-INFO/spring.factories文件加载 
 * SpringFactoriesLoader

