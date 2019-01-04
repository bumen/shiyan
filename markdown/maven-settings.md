## Maven配置
 * 组成
   + POM (project object model) 项目对象模型
   + 构建生命周期和阶段(project life cycle and phases)
   + 依赖管理模型 (dependency management model)
   + 插件管理
   + 仓库：本地与远程
   
### POM

### 仓库
 * 本地
 * 私服
 * 远程
   + 中央仓库(central)
   + 镜像
   + 其它远程仓库，则central是最后查找的仓库。（其它仓库顺序应该是按id字母顺序优先级查找）
 > 过程
 >> maven首先会去本地仓库找相关的依赖jar，如果没有，
 >> 假使配置了私服，那么第二步就会去私服上下载依赖jar。
 >> 如果私服这条路走了，还是没有下载到相关jar，
 >> 那么这时候就会去中央仓库下载，在中央仓库下载的时候，会根据镜像的配置对其jar进行备份缓存。
 >> 下一次再次访问中央仓库的时候，就会被镜像拦截，先从镜像地址获取jar（降低中央仓库的压力）
 1. 当依赖的范围是system的时候，Maven直接从本地文件系统解析构件
 
 2. 根据依赖坐标计算仓库路径后，尝试直接从本地仓库寻找构件，如果发现相应构件，则解析成功
 
 3. 在本地仓库不存在相应的构件情况下，如果依赖的版本是显示的发布版本构件，则遍历所有的远程仓库，发现后下载使用
 
 4. 如果依赖的版本是RELEASE或LATEST, 则基于更新策略读取所有远程仓库的元数据，将其于本地仓库的对应元数据合并后，计算出RELEASE或者LATEST的真实值，然后基于这个真实值检查本地仓库
 
 5. 如果依赖的版本是SNAPSHOT， 则基于更新策略读取所有远程仓库的元数据， 将其与本地仓库的对应元数据合并后，得到最新快照版本的值，然后基于该值检查本地仓库或从远程仓库下载
 
 6. 如果最后解析到的构件版本是时间戳格式的快照，则复制其时间戳格式的文件 至 非时间戳格式，并使用该非时间戳格式的构件
 
 
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
   + `mvn dependency:tree -Dverbose`  
   + `mvn dependency:tree -Dverbose -Dincludes=<groupId>:<artifactId>`: 查看某个module的依赖
   + 最后写着compile的就是编译成功的。
   + 最后写着omitted for duplicate的就是有jar包被重复依赖了，但是jar包的版本是一样的。
   + 最后写着omitted for conflict with xxxx的，说明和别的jar包版本冲突了，而该行的jar包不会被引入。
   
 * 依赖具有传递特性
 * 依赖范围<scope>
   + test：范围指的是测试范围有效，在编译和打包时都不会使用这个依赖
   只测试有效
   + compile：（默认值）范围指的是在编码范围有效，在编译和打包时都会将依赖打进去
   编译，测试，运行有效
   + provided：在编译和测试过程中有效，最后生成包时不会加入
   编译，测试有效
   + runtime：在运行时依赖，在编译时不依赖。如-对象api接口为compile, 对于第三方实现接口可以为runtime
   测试，运行有效
   + system：与provided一样，不过被依赖不会去maven仓库取，而是从本地文件系统拿，一定需要配合systemPath属性使用
   编译，测试有效
 

#### 依赖冲突-调解（由于依赖的传递特性）
 * 相同路径深度：第一声明者优先
   + a->b(1.0), c->b(1.1), d->a,c. 这时候b有两个版本会产生冲突。
   在d的pom中，哪个依赖先写就使用先配置的依赖的版本。
   + 对于相同路径深度的模块中的包依赖，哪个模块写在pom依赖前，就依赖哪个版本
 * 不同路径深度：路径最近者优先
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
 * 远程仓库的认证
   ``` 
    <!--配置远程仓库认证信息-->
     <servers>
         <server>
            <id>releases</id>
            <username>admin</username>
            <password>admin123</password>
         </server>
     </servers>
   ```
   + settings.xml中server元素的id必须与pom.xml中需要认证的repository元素的id完全一致。正是这个id将认证信息与仓库配置联系在了一起。
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
   + 元素updatePolicy用来配置Maven从远处仓库检查更新的频率，默认值是daily，
   表示Maven每天检查一次。其他可用的值包括：never-从不检查更新；always-每次构建都检查更新；
   interval：X-每隔X分钟检查一次更新（X为任意整数）
   + 元素checksumPolicy用来配置Maven检查校验和文件的策略。当构建被部署到Maven仓库中时，
   会同时部署对应的检验和文件。在下载构件的时候，Maven会验证校验和文件，如果校验和验证失败，
   当checksumPolicy的值为默认的warn时，Maven会在执行构建时输出警告信息，其他可用的值包括：fail-Maven遇到校验和错误就让构建失败；
   ignore-使Maven完全忽略校验和错误。
   
 * 激活配置
     ``` 
        <activeProfiles>
          <activeProfile>profile-jfrog</activeProfile>
        </activeProfiles>
     ```
  
#### 镜像
 * 关于镜像的一个更为常见的用法是结合私服。由于私服可以代理任何外部的公共仓库(包括中央仓库)，
 因此，对于组织内部的Maven用户来说，使用一个私服地址就等于使用了所有需要的外部仓库，这可以将配置集中到私服，从而简化Maven本身的配置。
 在这种情况下，任何需要的构件都可以从私服获得，私服就是所有仓库的镜像。这时，可以配置这样的一个镜像：
 ``` 
    <!--配置私服镜像-->
    <mirrors> 
        <mirror>  
            <id>nexus</id>  
            <name>internal nexus repository</name>  
            <url>http://xxx/nexus/content/groups/public/</url>  
            <mirrorOf>*</mirrorOf>  
        </mirror>  
    </mirrors>
 ```
* 该例中<mirrorOf>的值为星号，表示该配置是所有Maven仓库的镜像，任何对于远程仓库的请求都会被转至http://xxx/nexus/content/groups/public/。
如果该镜像仓库需要认证，则配置一个id为nexus的认证信息即可。
需要注意的是，由于镜像仓库完全屏蔽了被镜像仓库，当镜像仓库不稳定或者停止服务的时候，Maven仍将无法访问被镜像仓库，因而将无法下载构件。
     
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
   + -e(详细异常)
   + -U(强制更新)
   + -Dmaven.test.skip=true (跳test整个阶段)
   + -X (显示debug)
   
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