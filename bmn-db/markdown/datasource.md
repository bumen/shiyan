### java DataSource
 * DataSource是一个管理java连接到物理数据源的连接工厂
   + 就是一个java应用使用的数据库连接工厂
 * 替代DriverManager工具，使用DataSource对像是获取数据库连接的首选方式 
 * 实现了DataSource接口的对象通常会被注册到基于java或JDBI api 的命名服务中
 * 驱动服务商实现DataSource，有3种实现方式
   1. 基本实现，产生一个标准的连接对象
   2. 连接池实现，产生一个自动加入连接池的连接。这种方式与中间层连接管理器一起工作
   3. 分布式事务实现，产生一个为分布式事务使用的连接，这个连接通常总是由在连接池中。这种方式与中间层事务管理器一起工作
   通常总是有一个连接池管理器
 
 * 当需要时可以改变一个DataSource对象已经属性
   + 例如一个DataSource对象转移到另一个server，这台server可以配置相应属性。因为使用方便改变DataSource属性而不用
   修改相关联的代码
   
 * 与DataSource对象相关联的驱动自己不需要通过DriverManager来注册。相反DataSource对象通过查表操作来索取，然后来创建连接
 * 对于基础实现，通过DataSource获取的连接与通过DriverManager工具获取的一样
 
 * 注意
   + 实现DataSource接口必须提供一个public无参构造器
   
 * 方法
   + getConnection
     - 尝试与此DataSource对象表示的数据源建立连接
     - SQLException：当数据库访问出错时发生
     - SQLTimeoutException：当驱动确定获取连接时间超过通过setLoginTimeout设置的时间值时且至少尝试取消当前尝试创建的数据库连接后，抛出
     
   + setLoginTimeout
     - 设置此DataSource创建数据库连接最大等待时间秒。0表示使用系统默认超时时间（如果有），否则它指定没有超时
     - 当DataSource被创建，登录超时初始为0
 
 * java.sql.Wrapper
   + 这个类是为jdbc类的接口，当讨论的实例实际上是代理类时，该接口提供检索委托实例的能力
   + 很多jdbc驱动实现都使用了包装器模式来提供指定于数据源的传统jdbc api之外的扩展。开发人员可能希望获取这些资源的访问权，
   这些资源被包装（委托）为表示实际资源的代理类实例。这个接口描述了标准机制来访问由其代理表示的这些包装资源，以允许直接访问这些委托资源
   
   + 