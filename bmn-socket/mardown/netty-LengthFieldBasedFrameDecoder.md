## LengthFieldBasedFrameDecoder解码器


### 参数
 * byteOrder
 * maxFrameLength
 * lengthFieldOffset
 * lengthFieldLength
 * lengthAdjustment
 * initialBytesToStrip
 * failFast
### 功能
 * 该类的功能主要是找到length所在位置，然后再找到Content，最后根据不同配置参数解析协议
 * 场景1
   + 2 bytes length field at offset 0, do not strip header
   + Length：占用2个字节，值为0x0C=12, 表示Content占用字节数
   + Content: 占用12个字节
   + 总共消息长度为14字节
   + 配置  
   lengthFieldOffset = 0 （Length：在最开头，所以offset=0）  
   lengthAdjustment  = 0  
   lengthFieldLength = 2 （Length：占用两个字节）  
   initialBytesToStrip = 0
   + decode之后传到下一个handler中得到的消息为**AFTER DECODE**
   ```
    * BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
    * +--------+----------------+      +--------+----------------+
    * | Length | Actual Content |----->| Length | Actual Content |
    * | 0x000C | "HELLO, WORLD" |      | 0x000C | "HELLO, WORLD" |
    * +--------+----------------+      +--------+----------------+
   ```
 * 场景2
   + 2 bytes length field at offset 0, strip header
   + Length：占用2个字节，值为0x0C=12, 表示Content占用字节数
   + Content: 占用12个字节
   + 总共消息长度为14字节
   + 配置  
   lengthFieldOffset = 0  
   lengthFieldLength = 2 （Length：占用两个字节）  
   lengthAdjustment  = 0  
   initialBytesToStrip = 2 （跳过Length：占用的两个字节）
   + decode之后传到下一个handler中得到的消息为**AFTER DECODE**
   ``` 
    * BEFORE DECODE (14 bytes)         AFTER DECODE (12 bytes)
    * +--------+----------------+      +----------------+
    * | Length | Actual Content |----->| Actual Content |
    * | 0x000C | "HELLO, WORLD" |      | "HELLO, WORLD" |
    * +--------+----------------+      +----------------+
   ```
 * 场景3
   + 2 bytes length field at offset 0, do not strip header, the length field represents the length of the whole message
   + Length：占用2个字节，值为0x0e=14, 表示Length+Content占用字节数
   + Content: 占用12个字节
   + 总共消息长度为14字节
   + 配置  
   lengthFieldOffset = 0  
   lengthFieldLength = 2 （Length：占用两个字节）  
   lengthAdjustment  = -2 （在获取Content时长度要-2。即0x0e-2=0x0C）
   initialBytesToStrip = 0
   + decode之后传到下一个handler中得到的消息为**AFTER DECODE**
   ``` 
    * BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
    * +--------+----------------+      +--------+----------------+
    * | Length | Actual Content |----->| Length | Actual Content |
    * | 0x000E | "HELLO, WORLD" |      | 0x000E | "HELLO, WORLD" |
    * +--------+----------------+      +--------+----------------+
   ```
 * 场景4
   + 3 bytes length field at the end of 5 bytes header, do not strip header
   + Header1：占用2个字节
   + Length：占用3个字节，值为0x0C=12, 表示Content占用字节数
   + Content: 占用12个字节
   + 总共消息长度为17字节
   + 配置  
   lengthFieldOffset = 2 （跳过Header1：占用的2个字节，找到Length） 
   lengthFieldLength = 3 （Length：占用3个字节）  
   lengthAdjustment  = 0 
   initialBytesToStrip = 0
   + decode之后传到下一个handler中得到的消息为**AFTER DECODE**
   ``` 
    * BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
    * +----------+----------+----------------+      +----------+----------+----------------+
    * | Header 1 |  Length  | Actual Content |----->| Header 1 |  Length  | Actual Content |
    * |  0xCAFE  | 0x00000C | "HELLO, WORLD" |      |  0xCAFE  | 0x00000C | "HELLO, WORLD" |
    * +----------+----------+----------------+      +----------+----------+----------------+
   ```
 * 场景5
   + 3 bytes length field at the beginning of 5 bytes header, do not strip header
   + Length：占用3个字节，值为0x0C=12, 表示Content占用字节数
   + Header1：占用2个字节
   + Content: 占用12个字节
   + 总共消息长度为17字节
   + 配置  
   lengthFieldOffset = 0   
   lengthFieldLength = 3 （Length：占用3个字节）  
   lengthAdjustment  = 2 （跳过Header1：占用的2个字节，找到Content） 
   initialBytesToStrip = 0
   + decode之后传到下一个handler中得到的消息为**AFTER DECODE**
   ``` 
    * BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
    * +----------+----------+----------------+      +----------+----------+----------------+
    * |  Length  | Header 1 | Actual Content |----->|  Length  | Header 1 | Actual Content |
    * | 0x00000C |  0xCAFE  | "HELLO, WORLD" |      | 0x00000C |  0xCAFE  | "HELLO, WORLD" |
    * +----------+----------+----------------+      +----------+----------+----------------+
   ```
 * 场景6
   + 2 bytes length field at offset 1 in the middle of 4 bytes header, 
   strip the first header field and the length field
   + HDR1: 占用1个字节
   + Length: 占用2个字节，值为0x0C=12, 表示Content占用字节数
   + HDR2：占用2个字节
   + Content: 占用12个字节
   + 总共消息长度为16字节
   + 配置 
   lengthFieldOffset = 1 （跳过HDR1：占用的1个字节，找到Length） 
   lengthFieldLength = 2 （Length：占用2个字节）  
   lengthAdjustment  = 1 （跳过HDR2：占用的1个字节，找到Content） 
   initialBytesToStrip = 3 （跳过HDR1+Length占用的字节）
   + decode之后传到下一个handler中得到的消息为**AFTER DECODE**
   ``` 
    * BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
    * +------+--------+------+----------------+      +------+----------------+
    * | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
    * | 0xCA | 0x000C | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
    * +------+--------+------+----------------+      +------+----------------+
   ```
 * 场景7
   + 2 bytes length field at offset 1 in the middle of 4 bytes header,
   strip the first header field and the length field, the length field
   represents the length of the whole message
   + HDR1: 占用1个字节
   + Length: 占用2个字节，值为0x10=16, 表示HDR1+Length+HDR2+Content占用字节数
   + HDR2: 占用1个字节
   + HDR2: 占用1个字节
   + Content: 占用12个字节
   + 总共消息长度为16字节
   + 配置 
   lengthFieldOffset = 1 （跳过HDR1：占用的1个字节，找到Length） 
   lengthFieldLength = 2 （Length：占用2个字节）  
   lengthAdjustment  = -3 （跳过HDR1+Length：占用的1个字节，找到HDR2+Content） 
   initialBytesToStrip = 3 （跳过HDR1+Length占用的字节）
   ``` 
    * BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
    * +------+--------+------+----------------+      +------+----------------+
    * | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
    * | 0xCA | 0x0010 | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
    * +------+--------+------+----------------+      +------+----------------+
   ```