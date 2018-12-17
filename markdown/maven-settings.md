## Maven配置
 * 组成
   + POM (project object model) 项目对象模型
   + 构建生命周期和阶段(project life cycle and phases)
   + 依赖管理模型 (dependency management model)
   + 插件管理
   + 仓库：本地与远程
   
### POM
 
 
#### 聚合和继承
  * 项目关系
    ``` 
    user-project/pom.xml
    user-project/user-core
    user-project/user-log
    user-project/user-service
    ```
    
 * 聚合：通过配置module实现
   + 配置
   ``` 
    <project xmlns="http://maven.apache.org/POM/4.0.0" 
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <groupId>org.sai</groupId>
        <artifactId>user-project</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <!-- 类型为pom 管理 -->
        <packaging>pom</packaging>
        
        <!-- module 聚合里面存放是项目模块的绝对路径  -->
        <modules>
            <module>user-dao</module>
            <module>user-log</module>
            <module>user-service</module>
        </modules>
    </project>
    
   ```
   
 * 继承 
   + 在 Maven 的每个模块项目中，一些包依赖需要重复的去添加，
   所以我们可以采用在一个顶级的 pom.xml 来统一管理这些模块和包之间的依赖
   + 通常我们会把 聚合 和 继承统一在一个 pom.xml 文件中
   + 通过dependencyManagement管理子pom依赖
   ``` 
       <properties>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <junit.version>4.12</junit.version>
       </properties>
       
       <dependencyManagement>
         <dependencies>
             <dependency>
                 <groupId>junit</groupId>
                 <artifactId>junit</artifactId>
                 <version>${junit.version}</version>
             </dependency>
         </dependencies>
       </dependencyManagement>
   ```
   
 * 配置继承中的子pom  
   ``` 
       <!--  继承user-project 的pom.xml -->
       <parent>
           <groupId>org.sai</groupId>
           <artifactId>user-project</artifactId>
           <version>0.0.1-SNAPSHOT</version>
           <!-- 继承的绝对路径是pom的文件, 如果是在外层可以不添加这个配置 -->
           <!-- 或默认继承在该模块的最外层根目录下 -->
           <relativePath>../pom.xml</relativePath>
       </parent>
       
       <dependencies>
               <!-- 对于继承过来的，可以不用写上版本号继承 user-maven -->
               <dependency>
                   <groupId>junit</groupId>
                   <artifactId>junit</artifactId>
               </dependency>
       </dependencies>
   ```
   
   
### 构建生命周期
 0. 清除（clean）
   + pre-clean
   + clean
   + post-clean
 1. 验证（validate）
 2. 初始化
 3. 生成源码（generate-sources）
 4. 处理源码（process-sources）
 5. 生成资源（generate-resources）
 6. 处理资源（process-resources）
 7. 编译（compile）
 8. 处理类（process-classes）
 9. 生成测试源码（generate-test-sources）
 10. 处理测试源码（process-test-sources）
 11. 生成测试资源（generate-test-resources）
 12. 处理测试资源（process-test-resources）
 13. 测试编译（test-compile）
 14. 测试（test）
 15. 打包（package）
   +  prepare-package
   
 16. 集成测试（integration-test）
   + pre-integration-test
   + integration-test
   + post-integration-test

 17. 检验（verify）
 18. 安装（install） 
 19. 生成文档（site）
   + pre-site 执行一些需要在生成站点文档之前完成的工作
   + site 生成项目的站点文档
   + post-site 执行一些需要在生成站点文档之后完成的工作，并且为部署做准备
   + site-deploy 将生成的站点文档部署到特定的服务器上
   
 20. 部署（deploy） 
   
   
### 依赖管理模型
 * 查询命令  
 `mvn dependency:tree -Dverbose`
 * 依赖具有传递特性
 * 依赖范围<scope>
   + test：范围指的是测试范围有效，在编译和打包时都不会使用这个依赖
   + compile：（默认值）范围指的是在编码范围有效，在编译和打包时都会将依赖打进去
   + provided：在编译和测试过程中有效，最后生成包时不会加入
   + runtime：在运行时依赖，在编译时不依赖。如-对象api接口为compile, 对于第三方实现接口可以为runtime
   + system：与provided一样，不过被依赖不会去maven仓库取，而是从本地文件系统拿，一定需要配合systemPath属性使用
 

#### 依赖冲突（由于依赖的传递特性）
 * 相同路径深度
   + a->b(1.0), c->b(1.1), d->a,c. 这时候b有两个版本会产生冲突。
   在d的pom中，哪个依赖先写就使用先配置的依赖的版本。
   + 对于相同路径深度的模块中的包依赖，哪个模块写在pom依赖前，就依赖哪个版本
 * 不同路径深度
   + a->b(1.0), c->b(1.1), d->a, e->d,c. 这时候b有两个版本会产生冲突。
   在e的pom中，e->c->b, e->d->a->b 两条依赖关系。会选择最小路径e->b(1.1)
  
 * 解决冲突
   + 可以使用<exclusions>排除功能进行控制
   
   
#### 插件
 * 基础
   + phase: 可以在插件上指定声明周期的哪一步后面执行这个插件
   + executions - execution: 执行什么操作
   + goals - goal: 在绑定phase之后，需要指定插件执行的目标，可以执行多个
   + configuration: 配置插件中类的注解参数，这样可以给插件注明一些参数的值
  

### settings.xml
 * 全局配置：${MEAVEN_HOME}/conf/settings.xml
   + 对操作系统的所有使用者生效
 * 用户配置：${user.home}/.m2/settings.xml
   + 只对当前操作系统的使用者生效
   + 如果两个都存在则用户范围配置会覆盖全局的配置
 
#### 配置公司内部仓库
 * 在profiles标签中添加profile
    ``` 
        <profile>
          <id>profile-jfrog</id>
    
          <repositories>
            <repository>
              <id>jfrog</id>
              <name>xx lan repository</name>
              <url>http://xxx/artifactory/libs-release</url>
              <layout>default</layout>
              <releases>
                  <updatePolicy>daily</updatePolicy><!-- never,always,interval n -->
                  <enabled>true</enabled>
                  <checksumPolicy>warn</checksumPolicy><!-- fail,ignore -->
              </releases>
              <snapshots>
                  <enabled>false</enabled>
              </snapshots>
    
            </repository>
          </repositories>
        </profile>
    ```
 * 激活配置
     ``` 
        <activeProfiles>
          <activeProfile>profile-jfrog</activeProfile>
        </activeProfiles>
     ```
     
### 配置项目指定JDK版本
 * 修改pom.xml 
   ``` 
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
            <maven.compiler.source>1.8</maven.compiler.source>
            <maven.compiler.target>1.8</maven.compiler.target>
        </properties>
   ```
 * 如果添加到parent项目的pom.xml中，则所有子项目相样被指定
   + 建议创建项目时，添加parent项目
 * 引用parent项目
   ``` 
      <parent>
          <groupId>com.bmn</groupId>
          <artifactId>bmn-parent</artifactId>
          <version>1.0-SNAPSHOT</version>
      </parent>
   ```
   
### pom 关系


### 打包
 * mvn clean [package | install] -DskipTests=true -f pom-staging.xml -P staging
 * 在聚合关系中为Child Module 指定自定义名称的pom文件（如果不指定默认使用pom.xml）
   + configuration模块使用pom-staging.xml构建项目
   + stream模块使用默认pom.xml构建项目
   + 了解Tycho
   ```
      <modules>
              <module>stream</module>
              <module>configuration/pom-staging.xml</module>
      </modules> 
   ```