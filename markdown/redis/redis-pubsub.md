## redis 发布与订阅

### 频道的订阅与退订
 * redis将所有频道的订阅关系都保存在服务器状态的pubsub_channels字典里
   + key是某个被订阅的频道 
   + value是一个链表，记录所有订阅这个频道的客户端
 * 伪代码
 ``` 
    struct redisServer{
        dict *pubsub_channles;
    }
 ```
 
 * 频道订阅
   + 每当客户端执行subscribe命令订某个频道时，服务器将客户端与被订阅频道在pubsub_channels字典中关联
     - 如果找到其订阅者，则将客户端添加到订阅者链表尾
     - 如果没有任何订阅者，则创建一个，并添加
   + 伪代码
   ``` 
        def subscribe(*all_input_channels):
            # 遍历输入的所有频道
            for channel in all_input_channels:
                # 如果channel不存在于pubsub_channels字典
                # 那么在字典中添加channel键，并设置一个空链表
                if channel no in server.pubsub_channels:
                    server.pubsub_channels[channel] = []
            
                # 将订阅者添加到链表尾
                server.pubsub_channels[channel].append(client)
   ``` 
   
 * 退订频道
   + unsubcribe命令的行为，服务器从pubsub_channels中解出客户端与频道的关联
   + 从链表中删除对应client
   + 如果链表为变为空链表，程序将从pubsub_channels中删除频道对应的键
   + 伪代码
   ``` 
        def unsubscribe(*all_input_channels):
            #遍历输入的所有频道
            for channel in all_input_channels:
                # 在订阅链表中删除客户端
                server.pubsub_channels[channel].remove(client);
                # 如果频道已经没有任何订阅者
                # 那么将频道从字典中删除
                if len(server.pubsub_channels[channel]) == 0:
                    server.pubsub_channels.remove(channel);
   ```
    
### 模式的订阅与退订
 * redis将所有模式的订阅关系都保存在服务器状态的pubsub_patterns字典里
   + pubsub_patterns是一个链表，链表 的每个节点都包含着一个pubsubPattern结构
 * 伪代码
 ``` 
    typedef struct pubsubPattern{
        # 订阅模式的客户端
        redisClient *client;
        # 被订阅的模式
        robj *pattern;
    }
 ```
 
 * 订阅模式
   + 每当客户端执行psubscribe命令订阅某个模式时
     - 新建一个pubsubPattern结构，
     - 将pubsubPattern添加到pubsub_patterns链表的末尾
   + 伪代码
   ``` 
        def psubscribe(*all_input_patterns):
            # 遍历输入的所有模式
            for pattern in all_input_patterns:
                # 创建模式结构
                pubsubPattern = create_newPattern()
                pubsubPattern.client = client
                pubsubPattern.pattern = pattern
                
                # 添加到链表
                server.pubsub_patterns.append(pubsubPattern)
   ```
   
 * 退订模式
   + 每当客户端执行punsubscribe命令时，服务器在pubsub_patterns中找到并删除pattern匹配，client匹配的结构
   + 伪代码
   ``` 
        def punsubscribe(*all_input_patterns):
           # 遍历输入的所有模式
           for pattern in all_input_patterns:
                # 遍历链表中所有模式
                for pubsubPattern in server.pubsub_patterns:
                    # 判断匹配
                    if client == pubsubPattern.client and pattern == pubsubPattern.pattern:
                        # 删除订阅
                        server.pubsub_patterns.remove(pubsubPattern)
                        
   ```
   
### 发送消息
 * 当服务器执行publish <channel> <message>, 服务器执行 
   + 将消息message发送给channel所有订阅者
   + 如果有一个或多个模式pattern与频道channel匹配，那么将消息发送给pattern模式的订阅者
   + 伪代码
   ``` 
        def publish(channel, message):
            channel_publish(channel, message);
            pattern_publish(channel, message);
   ```
   
 * 将消息发送给channel所有订阅者
   + 在pubsub_channels字典里找到频道channel的订阅者名单链表
   + 伪代码
   ``` 
        def channel_publish(channel, message):
            if channel no in server.pubsub_channels:
                return;
            
            for subscriber in server.pubsub_channels[channel]:
                send_message(subscriber, message);
           
   ```
   
 * 将消息发送给模式订阅者
   + 需要遍历pubsub_patterns链表，查找那些与channel频道相匹配的pattern，然后发送消息给订阅了这些模式的客户端
   + 伪代码
   ``` 
        def pattern_publish(channel, message):
            for pubsubPattern in server.pubsub_patterns:
                if match(channel, pubsubPattern.pattern):
                    send_message(pubsubPattern.client, message);
   ```
   
### 查看订阅信息
 * redis2.8新增加命令
 * pubsub channels
   + 命令：pubsub channels [pattern]
   + 返回服务器当前被订阅的频道
   + pattern参数可选，如果有pattern参数，则选择与pattern匹配的所有订阅频道
   + 伪代码
   ``` 
        def pubsub_channels(pattern=None):
            channel_list= [];
            for channel in server.pubsub_channels:
                if pattern is None or match(channel, pattern):
                    channel_list.append(channel);
                    
            return channel_list;
   ```
      
 * pubsub numsub
   + 命令：pubsub numsub [channel1 channel2 ... channel-n]
   + 返回接受任意多个channel返回订阅这个频道的所有订阅者数量
   + 伪代码
   ``` 
        def pubsub_numsub(*all_input_channels):
            for channel in all_input_channels:
                if channel not in server.pubsub_channes:
                    reply_channel_name(channel);
                    reply_subscribe_count(0);
                    
                else: 
                    reply_channel_name(channe);
                    reply_subscribe_count(len(server.pubsub_channels[channel]));
   ```
   
 * pubsub numpat
   + 返回服务器当前被订阅模式的数量
   + 就是返回pubsub_patterns 长度
   + 伪代码
   ``` 
        def pubsub_numpat():
            reply_pattern_count(len(pubsub_patterns));
   ```