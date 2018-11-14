## Maven配置

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