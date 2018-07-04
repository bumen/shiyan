## chapter 4 国际化

### 概述
 * 字符集
   + 一组字符的集合。如：a-z 26个英文组一个字符集
   
 * 代码点(code point)
   + 给一个字符分配一个整数编号，这个编号称为代码点
   
 * 编码
   + 给每个字符分配一个代码点。
   + 如：a-z 分配 95-120
   
 * 编码字符集
   + 一个字符集，被编码为一个对应的代码点集合
   + 如：ASCII字符集， UNICODE字符集， GBK字符集，GB2312字符集
   
### unicode 编码字符集
 * unicode 目标能够包括世界上所有语言的文字
 
#### unicode 编码格式
 * 代码点范围：0-0x10FFFF , 1114112个代码点
 * 一百多万个代码点， 只有10%使用
 * 代码点被分成17个区， 每个代码点0-0xFFFF 65536个
   + 0-0xFFFF, 1区, BMP
   + 0x10000-0x1FFFF,  2区, SMP
   + 0x20000-0x2FFFF,  3区, SIP
   + 0x30000-0x3FFFF,  4区, TIP
   + 0xD0000-0xDFFFF,  15区, SSP
   + 0xE0000-0xEFFFF,  16区, PUA 
   + 0xFFFFF-0x10FFFF, 17区, PUA
   
#### unicode 代码点映射方式
 * 没有采用：一一映射方式（即一个代码点占用0x10FFFF位）
 * 采用：一个代码点分别映射到多个代码单元上
 
 * UTF(unicode transformation format), 传输格式
   + UTF8,  8位表示一个代码单元
      - 一个代码点会被映射到1到6个代码单元。
      - 应用：作为字符传输时使用
      - 8位一个代码单元，没有字节顺序
      
   + UTF16, 16位表示一个代码单元
      - 1个代码点会被映射到1到2个代码单元。
      - 应用：作为字符在系统中的内部表示方式。
      - 16位一个代码单元，需要注意字节顺序，大端，小端
      
   + UTF32, 32位表示一个代码单元
 * UCS(universal character set)， 统一字符集
   + UCS2, 2个字节表示一个代码单元
   + uCS4, 4个字符表示一个代码单元
   

#### java 与 unicode
 * 一个char是2个字节，只有表示1区BMP中的字符代码点
 * 通过int 32位可以表示BMP之名的代码点
 * 通过Character来获取BMP之外的代码点
 
#### jvm 外部， 即java文件，.class文件编码格式
 * java源文件可以采用不同的编码格式，一般为UTF-8
 * javac编码java源文件时，如果不指定编码格式，默认使用系统指定编码(windows gbk, linux utf-8), 
   来读取java源文件。
    + 如：java源文件utf-8, javac 使用gbk 读取， 则出现乱码
    + 如：java源文件utf-8, javac 使用utf-8读取，则正确
    
 * class文件，是javac 编码器输出的，编码为utf-8
 
#### java中编码， jvm内部使用的utf-16, 即运行时
 * java平台内部统一使用utf-16编码
 * 单个程序内部不会存在编码问题
 * 发生编码问题：
   + 内部程序与外部发生数据传递
   + 用户输入数据到程序中
   + 程序读取外部文件
   + 程序传递参数给操作系统底层方法调用
   + 程序产生输出文件
   + 一个字节序列，跨越当前程序边界， 且这些字节序列当成字符来处理时。
 * 如果运行时编码转换的两种格式都不是utf-16，则需要utf-16做为中间格式。
 
### javaNio 编码，解码器
 * StandardCharsets 可以获取标准编码集
 * CharsetEncoder
   * 有状态
   * CoderResult 4个状态
      + UNDERFLOW, 输入缓冲区没有数据，编码完成
      + OVERFLOW,  输出缓冲区没有足够空间，放编码后的结果
      + MALFORMED, 输入缓冲区中的字符不是合法utf-16格式， 输入必须是utf-16
      + UNMAPPABLE, 输入缓冲区中的某些字符，在当前字符集中无法表示
      + 后两种结果CodingErrorAction来处理
      + CodingErrorAction.REPORT  通过encode返回值说明错误信息
      + CodingErrorAction.REPLACE 用规定字节数组替换
      + CodingErrorAction.IGNORE  跳过输入中引起错误的字符
      
 * CharsetDecoder
   * 有状态