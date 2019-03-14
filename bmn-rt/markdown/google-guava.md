## google guava使用


### Cache
 
#### LoadingCache
 * 配置expireAfterAccess
   + 默认为0，不过期
   + 如果配置，只要在key到达过期时间后，通过触发get(keyOther)接口，去删除过期所有key。
   如果多线程调用相同key时，会阻塞等待
   + 缺点阻塞获取
 
 * 配置refreshAfterWrite
   + 默认为，不刷新
   + 如果配置，当到达key的刷新时间后，通过触发get(对应的key)时，才会刷新。是一个异步刷新 
   刷新完成后，先删除旧值，再设置新值，并返回此次触发刷新的get()新值。
   如果刷新过程中有其它线程get相同key，则会返回旧值
   + 缺点如果key一直不访问，则一直不过期
   + 会调用reload，默认reload会调用load产生新值
   
 * get(key)
   + 阻塞获取key， 首先会执行删除过期key操作。如果key不存在阻塞load一个新值
   
 * 一般expireAfterAccess与refreshAfterWrite配合使用
   + refreshAfterWrite可以避免阻塞，expireAfterAccess避免长时间不删除过期数据

#### ManualCache