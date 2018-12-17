## io包

### stream与channel区别
 * stream: 是单向的（inputstream或outputstream）
   + 主要有两种：字节流，字符流
 * channel:是双向的支持同时读与写操作
   + 主要有两种：FileChannel, SocketChannel
   
   
### stream

#### InputStream
 * read方法会阻塞直到
   + 读取到数据
   + 读取到数据结束: 返回-1
   + 抛出异常：IOException
   
 * 实现
   + FileInputStream : 操作文件
     - SocketInputStream
   + FilterInputStream : 用于装饰Steam
     - DataInputStream : 操作对象基础数据类型对象，包括String。允许应用程序以与机器无关方式从底层输入流中读写基本 Java 数据类型
     - BufferedInputStream
   + ByteArrayInputStream : 操作内存数据 
   + ObjectInputStream : 操作对象, java才能使用
   + PipedInputStream
   
   
#### OutputStream
 * write方法
 * 实现
   + FileOutputStream
     - SocketOutputStream
   + FilterOutputStream
     - DataOutputStream
     - BufferedOutputStream
   + ByteArrayOutputStream
   + ObjectOutputStream
   + PipedOutputStream
   
   
### Reader
 * read方法会阻塞直到
   + 读取到数据 
   + 读取到数据结束：返回-1
   + 抛出异常：IOException