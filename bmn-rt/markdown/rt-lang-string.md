## String 字符串

 * final char[] value 字节数组实现
 * hash码
   + "" 字段串的hash永远为0
   
   

   
## StringBuilder
 * 扩容
   + (value.length << 1) + 2， 2倍+ 2
   
 * append
   + len + str.length， 实现一个字符数组拷贝
   + 最好创建时初始化好大小
   

## StringBuffer
 * 线程安全的StringBuilder