## 打jar包


### META-INF
 
 
#### MANNIFEST.MF 清单文件
 * 重要属性
  + Class-Path：指定运行jar时的classpath
  + Main-Class：指定main方法所在类
  > 如果没有指定这两项属性，而在执行jar -jar *.jar时出错。
  > 也可以通过命令行直接指定：java -cp lib/a.jar:lib/b.jar xxx.jar test.MainClass。
  