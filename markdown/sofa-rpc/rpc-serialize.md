## 序列化

### Protobuf
 * 调用方指定接口协议。服务方跟据调用方使用的协议进行返序列化
 * 使用Protobuf特点：
   + 接口只能是一个参数。且为proto生成的类
   因为在服务方返序列化时，会跟据client传过来的接口类型+方法名，找到对应的Method对象
   判断client传过来的参数类型是否与Method一致。否则出错
   同时方法必须有返回值，且也只能是proto生成的类
   > 如果有多个方法名相同的方法。则只会查找第1个名字相同的。所以不要使用同名方法
   
  