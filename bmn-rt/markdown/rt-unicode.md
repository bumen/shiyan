## java unicode 编码

### unicode字符集
 * 有17个平面，每个面有65536个码点，即每个面占两个字节
   + 所有unicode可以表示 17 * 65536 个码点
   
 * 0面叫BMP, 也叫基本平面, 用0000-ffff表示，基本全世界常用字符  
 * 1面叫SMP, 是基本平面的增补面，用10000-1ffff
 * 2面叫增补象形平面，是20000-2ffff
 * 3面-13面，为保留30000-Dffff
 * 14面，为专用平面，e0000-effff
 * 15面-16面，为私用平面，f0000-10ffff
 
 * unicode可以3个字节，表示完成
 
#### java中表示 
 * java也按面构建不同范围的码点。实现快速查找 
 * java用int 4个节点表示码点，可以包括所有unicode码值
 * 通过码点 int >>> 16位，判断出该码点属于哪个平面
   ``` 
      //可以快速判断一个ASCII码
      if (ch >>> 8 == 0) {     // fast-path
          return CharacterDataLatin1.instance;
      } else {
          switch(ch >>> 16) {  //plane 00-16
          case(0):  0000-ffff
              return CharacterData00.instance;
          case(1):  10000-1ffff
              return CharacterData01.instance;
          case(2):  20000-2ffff
              return CharacterData02.instance;
          case(14):  e0000-effff
              return CharacterData0E.instance;
          case(15):   // Private Use
          case(16):   // Private Use  f0000-10ffff
              return CharacterDataPrivateUse.instance;
          default:
              return CharacterDataUndefined.instance;
          }
      }
   ```
   
### UTF-8编码
 * 一般使用1-4字节表示unicode字符
   + 1字节：0xxxxxxx
   + 2字节：110xxxxx, 10xxxxxx
   + 3字节：1110xxxx, 10xxxxxx, 10xxxxxx
   + 4字节：11110xxx, 10xxxxxx, 10xxxxxx, 10xxxxxx
   + 1字节：码点：u+0000-u+007f
   + 2字节：码点：u+0080-u+07ff
   + 3字节：码点：u+0800-u+7fff  , 可以表示所有0平面
   + 4字节：码点：u+10000-u+1ffff, 可以表示所有unicode
   + 首字节有几个1就表示由几个字节组成

 * 特点
   + 无字节序，由于是单字节
   + 兼容ASCII
   + 自同步和纠错能力强：不会应用一个字节序列不正常，而影响其它字节
   + 适合网络传输；扩展性好
   
 * 缺点：
   + 变长编码方式不利于程序内部处理
   
### UTF-16编码
 * 固定两个字节编码0000-ffff, 即0平面码点
 * 对于2平面码点采用使用1平面未使用码点作为代理实现
 * 0xD800-0xDfff 共2048个码点。称为代理区
   + 高位代理0xD800-0xDBff
   + 代位代理0xDC00-0xDfff
   + 高位与低位码点结合来刚好表示1平面所有码点
 * 优点：
   + 适合内存中unicode处理
 * 缺点：
   + 不兼容ASCII
   + 1平面码点需要使用代理
   + 编码复杂；扩展性差
### UTF-32编码
 * 固定4个字节，与每个unicode码点对应
 * 实现简单，但浪费空间，一般很少使用
 * 优点：
   + 简单码点一一对应
 * 缺点
   + 浪费存储空间与带宽
 
### BOM byte order mark 字节顺序标记
 * 大端 Big-endian
   + 高字节在8低位，低字节在8高位
 * 小端 Little-endian
   + 高字节在8高位，低字节在8低位
   
 * 如果文件是带BOM编码时，开头会加入0xEF0xBB0xBF 表示带标记，表示utf8
   + UTF-8 EF BB BF(标记),  通过EF BB BF表示文件是utf-8编码
   + UTF-16-BE FE FF(标记), 通过FE FF表示文件是utf-16-BE编码
   + UTF-16-LE FF FE(标记), 通过FF FE表示文件是utf-16-LE编码
   + UTF-32-BE 00 00 FE FF(标记), 通过00 00 FE FF表示文件是utf-32-BE编码
   + UTF-32-LE FF FE 00 00(标记), 通过FF FE 00 00表示文件是utf-32-LE编码
 * 使用BOM应该是windows的锅
 * 对UTF-8与UTF-8 bom 没有区别，只是windows会通过bom标记，来判断文本编码是utf8
   + 因为UTF是单字节编码，所以不存在顺序。网络接收时通过首字节可以知道后顺序字节
   + 因为文件如果是UTF-8编码，则系统会一个字节一个字节读取
 * 对于UTF-16, UTF-16-BE, UTF-16-LE
   + 因为UTF-16是两字节编码，所以在网络传输时需要确定字节顺序
   + 因为文件如果是UTF-16编码，则系统会两个字节两个字节读取然后去解码