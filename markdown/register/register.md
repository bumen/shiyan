### 服务注册与发现

 * consul 
 * zookeeper
 * etcd
 * 
 
#### 网上收集的一些优缺点对比
 
#### zookeeper
 * 优点  
   a. ZooKeeper的主要优势是其成熟、健壮以及丰富的特性
   
 * 缺点  
   a. 其中采用Java开发以及复杂性是罪魁祸首。
   尽管Java在许多方面非常伟大，然后对于这种类型的工作还是太沉重了，ZooKeeper使用Java以及相当数量的依赖使其对于资源竞争非常饥渴。
   ZooKeeper变得非常复杂，维护它需要比我们期望从这种类型的应用程序中获得的收益更多的知识。
      - 维护它，还需要其它更多的知识
      - 应用程序的特性功能越多，就会有越大的可能性不需要这些特性，因此，我们最终将会为这些不需要的特性付出复杂度方面的代价。
      - ZooKeeper为其他项目相当大的改进铺平了道路，“大数据玩家“在使用它，因为没有更好的选择。
      - 今天，ZooKeeper已经老态龙钟了，我们有了更好的选择。