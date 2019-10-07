## 打包 jar

### java命令打包
 * 注意
   + 清单文件名, 档案文件名和入口点名称的指定顺序与 'm', 'f' 和 'e' 标记的指定顺序相同。
   + 在运行java命令的时候，如果指定了-jar选项，那么环境变量CLASSPATH和在命令行中指定的所有类路径都被JVM所忽略
     - 解决方案
     1. 不使用-jar参数方式启动  
     `java -cp lib\xxx;shutdown.jar com.bmn.jvm.shutdown.TestMain`
      > 在windows下多个jar之间以分号（;）隔开,最后还需要指定运行jar文件中的完整的主类名。
      > 在linux下多个jar之间以冒号（:）隔开
     2. 使用-jar参数  
     `java -jar shutdown.jar`
      > 需要修改shutdown.jar中的MANIFEST.MF，通过MANIFEST.MF中的Class-Path 来指定运行时需要用到的其他jar
      > 其他jar可以是当前路径也可以是当前路径下的子目录
      > 多个jar文件之间以空格隔开
      
 * MANIFEST.MF
   + 打包会默认自动生成
   + Class-Path 指定需要的jar，多个jar必须要在一行上，多个jar之间以空格隔开，如果引用的jar在当前目录的子目录下，windows下使用\来分割，linux下用/分割
   + 文件的冒号后面必须要空一个空格，否则会出错
   + 文件的最后一行必须是一个回车换行符，否则也会出错
   > 打包时可修改
   
 * java -cvtfm x.jar manifest.mf \[-C dir\]files
   + manifest.mf 文件名可以随意起
   
 * 使用
   + 项目bmn-rt
   + 源码目录src/com/bmn/shutdown/
   + 清单文件：META-INF/shutdown.mf
   + 编译后源码目录：target/classes/com/bmn/jvm/shutdown/
   + 步骤
     1. 编译项目
     2. cd bmn-rt根目录 
     2. 打包：jar cvfm shutdown.jar META-INF/shutdown.mf -C target/classes/ com/bmn/jvm/shutdown/
     3. 查看：jar tf shutdown.jar
     
   > 注意
   >> -C target/classes/ com/bmn/jvm/shutdown/ 不能写成 jar cvfm shutdown.jar META-INF/shutdown.mf target/classes/com/bmn/jvm/shutdown/
   >> -C 参数指定.class输出目录，但这个目录不是类路径目录，只是一个存放目录，所以不能打到jar包中。
   >> com/bmn/jvm/shutdown/ 会将这个目录及子目录+文件都打包。所以不能使用target/classes/com/bmn/jvm/shutdown/，否则将target/classes目录也打到jar包中