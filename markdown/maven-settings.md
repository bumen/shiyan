## Maven配置
 * 组成
   + POM (project object model) 项目对象模型
   + 构建生命周期和阶段(project life cycle and phases)
   + 依赖管理模型 (dependency management model)
   + 插件管理
   + 仓库：本地与远程
   
### POM
 * 默认打包为jar
 * 默认依赖的scope为compile
 
 * dependency 元素
   + groupId, artifactId, version
   + type: 对应packing, 默认jar
   + scope
   + optional 是否可选
   + exclusions
      - 用来排除传递性依赖
      
 * dependency中的scope
   + 首先有三种classpath
      1. compile classpath: 编译项目主代码时候需要使用一套classpath
        - 如：项目中用到了spring.jar，该文件通过依赖方式加到这个classpath中
      2. test classpath: 测试的使用一套classpath
      3. run classpath: 运行时使用一套classpath
   + compile 默认
     - 对三个classpath都有效
     - 如：spring.jar 编译，测试，运行都需要
   + test:
     - 只有test classpath有效，对于编译与运行无效
     - 如：junit
   + provided
     - 编译，测试有效
     - 如：servlet-api, 编译时需要，运行时候容器已经带了
   + runtime
     - 运行，测试有效
     - 如：jdbc驱动，编译时使用jdk的jdbc接口就可以
   + system
     - 与provided类似
     - 显示指定依赖文件路径，此依赖不走maven库解析，往往是与本机系统绑定
     一般是不可移值的
   + import
   
   
 * 传递依赖
   + 自己项目依赖了B, B又依赖了C，则项目对C产生了传递依赖
   + 传递依赖通过scope控制它对三种classpath是否有效
   
 * 传递依赖与scope
   + 主要是以第一级依赖为准，主要看使用时需要不需要
   + 第一级是compile, 则maven只引入第二级是compile和runtime的
   + 
   -----------------------------------------------+
   |一级/二级| compile | test | provided | runtime |
   -----------------------------------------------+
   |compile | compile | -    |  -       | runtime |
   |test    | test    | -    |  -       | test    |
   |provided| provided| -    | provided | provided|
   |runtime | runtime | -    |  -       | runtime |
   -----------------------------------------------+
   + 解释，通过A->B->C来模拟
       > B首先自己没有编译与测试过程了，
       >> 如果B是compile说明，B在编译+测试+运行时都需要C
     1. A是compile 我编译，测试，运行都要用你，
        - B是compile其实B没有编译，测试过程了，因为A运行时需要，所以这里只使用的运行过程
        - B是test, provided，没用，因为B不需要测试与编译
        - B是runtime, 这个是有用的。因为A运行时需要
     2. A是test, 我只测试
        - B是compile，对应也降为test, 因为A的test过程需要C
        - B是test, provided没用
        - B是runtime, 对应也降为test, 因为A的test过程需要C
     3. A是provided，编译+测试
        - B是compile, 对应降为provided。因为A的编译测试也需要C
        - B是test, 不需要
        - B是provided，runtime, 因为A的编译需要C
     4. A是runtime
        - B是compile, 因为A需要
        - B是test, provided, 因为A不需要C
        - B是runtime, 因为A需要C
      
   
 * 依赖调解
   + 第一原则
     - A->B->C->X(1.0), A->D->X(2.0)
     - 采用路径最近者优先被使用，则使用X(2.0)
   + 第二原则
     - A->B-X(1.0), A->D-X(2.0)
     - 步长一样时，按pom是依赖声明的顺序决定谁会被优先使用。则使用X(1.0)
     
 * dependency的 optional可以依赖
   + optional为ture是，这个依赖不会被传递
   + 一般不会使用
   
 * mvn dependency:list 
   + 列出解释的依赖
 * mvn dependency:tree
   + 列出依赖树
 * mvn dependency:analyze
   + 列出项目中使用的未声明的依赖和已声明但未使用的依赖
   + 使用的未声明的依赖指的是：项目代码里直接使用的传递依赖包中的接口
      - 如果直接使用的接口，最后声明为直接依赖
   + 已声明但未使用指的是：项目在编译+测试阶段声明了某个直接依赖但没有用到
      - 注意，但可能在运行时会用到。因此可以不用参考这个输出
      

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
 
 * 配置
   + releases, snapshots
     - enabled
     - updatePolicy：从远程仓库检查更新的频率
        - daily: 每天 默认
        - never: 从不
        - always: 每次构建都检查
        - interval: 每隔多少分钟
     - checksumPolicy：在下载依赖时，会校验依赖。校验失败的策略
        - warn: 默认 警告
        - fail: 构建失败
        - ignore: 忽略
        
 * 仓库的认证信息
   + setttings配置
      - <servers><servers>中配置
      - 其中<server><id></id></server> id需要与仓库id对应上
      
 * 将包部署到远程仓库
   + 上传和下载都可能都需要认证，都是server配置
   ``` 
    <distributionManagent>
      <repository>
        <id>远程仓库id</id>
        <name>随意</>
        <url>仓库地址</>
      </>
      <snapshortRepository>
        <id>远程仓库id</>
        <name/>
        <url/>
    </>
   ```
 * 快照版本
   + 打的快照版本都会带一个时间戳，这样maven就能知道有更新了，然后跟据updatePolicy去更新 
   
 * 仓库镜像
   + 也是一个仓库，所有也需要一个仓库配置
   ``` 
        <mirrors>
            <mirror>
                <id> 镜像仓库配置</>
                <name>镜像仓库配置</>
                <url>镜像仓库配置</>
                <mirrorOf>被镜像的仓库id</>
   ```
   + 所有到mirrorOf仓库的请求都转发到镜像仓库
   + mirrorOf配置
     - * 匹配所有仓库
     - external: * 匹配所有远程仓库，不包括localhost与file://协议的，也就是匹配所有非本机仓库
     - rep1,rep2: 匹配 rep1和rep2仓库
     - *, ! rep1: 匹配所有，除了rep1
   + 注意
     - 当镜像仓库挂了，maven也不能访问被镜像的仓库。所以下载会失败
     
 
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
 * 聚合与继承的pom.xml的<packaging>必须为pom
 * 聚合是关联子module, 继承是子pom.xml定位父pom.xml
 * 聚合与继承可以合并为一个pom.xml
   + 方便使用
 * 聚合
   + 主要是方便打包，不用不同module分别打
 * 继承注意配置正确：relativePath
   + 与子父同级时，需要../
   + 父是子上级时可以省略
 * 可继承pom元素
   + groupId
   + version
   + dependencies
   + properties
   + dependencyManagement
   + repositories
   + build
   + 等
 * dependencyManagement
   + 父类声明，子类中被继承，但都不会引入
   + 子类还需要通过dependencies引入，只是简化
      - 省略了version与scope
   + scope中的import只能在这使用，方便其它pom引用
 * build->pluginManagement
   + 同dependencyManagement类似
   
 * 构建过程
   + 先构建父模块在构建子模块
   
### 构建生命周期
 * [参考地址](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
 * 有三套三命周期，不同生命周期都是由多个阶段组成
   1. clean
   2. default
   3. site 
 * 生命周期都是声明的抽象，具体执行是通过插件完成
   + 需要进行插件与生命周期绑定
   + 默认插件与自定义插件
   + default生命周期时，跟据<packaging>是jar, war不同使用的默认插件也不同
  
 * 插件
   + 插件中是有目标的（类型不同的功能接口，通过goal来引用）
   + 一个插件中有多个目标，不同目标与生命周期阶段绑定来执行
   + 当没有为插件绑定阶段时，也可能会执行，因为绑定了默认的阶段
     - mvn help:describe -Dplugin = groupId:artifactId:version -Ddetail
     - 查看默认绑定
   + jar打包类型的默认绑定
   ``` 
    process-resources : maven-resources-plugin:resources
    compile: maven-compiler-plugin:compile
    process-test-resources: maven-resources-plugin:testResousrces
    test-compile: maven-compiler-plugin:testCompile
    test : maven-surefire-plugin:test
    package: maven-jar-plugin: jar
    install: maven-install-plugin: install
    deploy: maven-deploy-plugin:deploy
   ```
 * 插件配置
   + 命令行配置：如
      - mvn clean package -Dmaven.test.skip=true
   + pom中插件全局配置
      - 不用在命令行每次都配置
      ``` 
        
        <configuration>
            <source>1.5</source>
        </configuration>
      ```
   + pom中插件任务配置
      - 
 * 插件说明
   + 可以看官方文档
   + 可以使用mvn help:describe -Dplugin=[gid:aid:version]
     - 可以省去version, maven会找最新的version
     - -Dplugin=surefire 即可以直接使用Goal Prefix
     - 如：
     - 其中：Goal Prefix，表示默认前缀，
     
     ``` 
        mvn help:describe -Dplugin=org.apache.maven.plugins:maven-surefire-plugin
        
        
        Name: Maven Surefire Plugin
        Description: Maven Surefire MOJO in maven-surefire-plugin.
        Group Id: org.apache.maven.plugins
        Artifact Id: maven-surefire-plugin
        Version: 2.22.2
        Goal Prefix: surefire
        
        This plugin has 2 goals:
        
        surefire:help
          Description: Display help information on maven-surefire-plugin.
            Call mvn surefire:help -Ddetail=true -Dgoal=<goal-name> to display
            parameter details.
        
        surefire:test
          Description: Run tests using Surefire.
     ```
   
   
 1. clean 清除（clean）
   + pre-clean
   + clean
   + post-clean
 2. default 
    1. 验证（validate）
    2. 初始化 initialize
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
    14. process-test-classes
    15. 测试（test）
    16. prepare-package
    17. 打包（package）
    18. pre-integration-test
    19. integration-test
    20. post-integration-test
    21. 检验（verify）
    22. 安装（install） 
    23. 部属（deploy）
    
 3. 生成文档（site）
   + pre-site 执行一些需要在生成站点文档之前完成的工作
   + site 生成项目的站点文档
   + post-site 执行一些需要在生成站点文档之后完成的工作，并且为部署做准备
   + site-deploy 将生成的站点文档部署到特定的服务器上
   
 20. 部署（deploy） 
 
### mvn 命令调用插件
 * 一种是通过命令激活生命周期阶段来调用插件
 * 一种是直接从命令行调用插件目标
   + mvn groupId:artifactId:version:goal
   + mvn prefix:goal
      - 自动查找groupId和artifactId和version
      - 设计到groupId查找，artifactId查找，version查找过程
   
### 插件仓库
 * 与依赖仓库不同
 * 如果插件在本地仓库找不到，他不会去远程仓库去找。所以需要配置一个插件仓库
   + 配置与依赖仓库一样
   + 外层使用标签<pluginRepositories>   
 * 插件默认groupId
   + 如果pom中配置的插件是org.apache.maven.plugins下的插件时，可以省略groupId
     - maven解析时会自动补全groupId
 * 插件省略版本
   + 如果没有指定配置时，会使用默认版本。因为maven有一个超级父pom中会配置常用插件默认版本
   + 如果超级pom中没有，则会去本地找，同时会跟据updatePolicy去远程找去发现最新版本
      - 这样有这问题就是如果版本变更，可能引起错误所有不建议省略版本
      
 * 解析插件前缀
   + 插件前缀可以简化插件调用
   + 插件前缀与groupId:artifactId对应，这种匹配关系存储在仓库元数据中
     - groupId/artifactId/maven-metadata.xml是artifactId元数据，用于比对版本更新
     - groupId/maven-metadata.xml是插件元数据
     - 默认存在org.apache.maven.plugins和org.codehaus.mojo下
   + 通过配置指定
     - settings.xml
     ``` 
        <pluginGroups>
            <pluginGroup>com.your.plugins</pluginGroup>
          </pluginGroups>
     ```

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
   
### mvn
 * 查看当前生效的settings
   + mvn help:effective-settings
   + mvn help:effective-pom 
 * 下载源码
   + mvn dependency:source
   + 需要进入pom.xml文件夹mvn dependency:sources -X  --settings settings.xml
   
 * mvn install --settings settings.xml 指定配置文件

### profile
 * pom是可以使用的属性
   + 内置属性
   + pom属性
   + 自定义属性
   + settings属性
      - 引入settings.xml中的变量
   + java系统属性
   + 环境变量属性
 
 * 资源过滤
   + maven可以将属性设置到项目资源文件中
   ``` 
    <resources>
        <resource>
            <directory>/</directory>
            <filtering>true</filtering>
        </resource>
    </resources>
   ```
   
 * 激活profile
   + 命令行激活
     - mvn clean install -Px1,x2
     - 多个用逗号分隔
   + settings 显式激活
   ``` 
    <activeProfiles>
        <activeProfile>alwaysActiveProfile</activeProfile>
        <activeProfile>anotherAlwaysActiveProfile</activeProfile>
      </activeProfiles>
      
   ```
   + 系统属性激活
      - mvn clean install -Dtarget-env=dev
     ``` 
        <profile>
              <id>env-dev</id>
        
              <activation>
                <property>
                  <name>target-env</name>
                  <value>dev</value>
                </property>
              </activation>
        <profile>
     ```
     
   + 操作系统环境激活
     ``` 
        <profile>
              <id>env-dev</id>
        
              <activation>
                <os></os>
              </activation>
        <profile>
     ```   
   + 文件存在与否激活
     ``` 
        <profile>
              <id>env-dev</id>
        
              <activation>
                <file><missing></><exists></></file>
              </activation>
        <profile>
     
     ```
   + jdk version激活
     ``` 
     <profile>
           <id>env-dev</id>
     
           <activation>
             <jdk>1.8</>
           </activation>
     <profile>
     ```
   + 默认激活
      - 其中任何一个profile被激活了，所有默认激活都失效
     ``` 
        <profile>
              <id>env-dev</id>
        
              <activation>
                <activeByDefault>ture</>
              </activation>
        <profile>
     ```
     
   + help命令
      - mvn help:active-profiles: 了解当前激活的profile
      - mvn help:all-profiles: 列出所有profile
      
 * profile种类
   + pom.xml中声明：只对当前项目有效
   + 用户settings.xml中声明
   + 全局settings.xml中声明
   
   + pom.xml中的profile元素比其它的可以设置更多
      ``` 
      <profile>
          <id>release-sign-artifacts</id>
          <repositories></repositories>
          <pluginRepositories></pluginRepositories>
          <distributionManagement></distributionManagement>
          <dependencies></dependencies>
          <dependencyManagement></dependencyManagement>
          <modules></modules>
          <properties></properties>

          <build>
              <defaultGoal></defaultGoal>
              <resources></resources>
              <testResources></testResources>
              <finalName></finalName>
              <plugins>
          </build>
      <profile>
      ```
   + settings文件中
      - <repositories></repositories>
      - <pluginRepositories></pluginRepositories>
      - <properties></properties>
    
