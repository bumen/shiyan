## java加密

### java与密码学
 * java安全领域
   + JCA, java加密体系结构
   + JCE, java加密扩展
   + JSSE, java安全套接字扩展
   + JAAS, java认证与安全服务
 
 * JCA
   + 提供基本的加密框架
     - 证书
     - 数字签名
     - 消息摘要
     - 密钥对产生器
   + 与JCE是java平台提供的用户安全和加密服务的两组API
 * JCE
   + 是对JCA的基础上作了扩展  
 * JSSE
 * JAAS
   + 在java平台上进行用户身份认证的功能
 > JCA与JCE是java平台提供的用户安全和加密服务的两组API, 它们并不执行任何算法，软件开发商根据接口提供实现。
 
#### 使用
 * 所有服务都业务层通过Engine类来使用
 * 密钥就是密钥映射表，(个人了解,经过加密的字符串中每个字符都是密钥中的字符)

 
#### Provider
 * 服务提供者
 * 每个提供者都有一个名称和版本号
 * 可能的实现
   + 算法提供者
   + 密钥生成、转换和管理
 * 实际开发很少直接使用，已经自带好了。
 * 如果实现一个Provider，要提供哪些服务就实现相应的引擎类SPI。业务通过引擎类SPI来调用服务
 * 源码：
   + Provider类中定义了所有可以的Engine（引擎类）
   + 如果Provider子类可以提供某种服务就实现相应的引擎SPI，即是一个Provider.Service
   + 每个Provider子类可以提供多个Service实现。
   + Provider创建时会初始化legacyStrings
   + getService时会解析legacyStrings，然后获取Service
   
   
 
#### Security
 * Security类的任务就是管理java程序中所用到的提供者类。
 * 初始化java.home/lib/security/java.security
 
#### Key
 * Key接口是所有密钥接口的顶层类
 * 有三个特征
   + 算法 DES, RSA
   + 编码形式
     - 指的密钥外部编码形式，密钥根据标准格式编码（X.509,PKC#8)
   + 格式
     - 已编码密钥格式名称
 * 有SecretKey, PublicKey, PrivateKey
 * SecretKey
   + 是对称密钥顶层接口
 * PublicKey, PrivateKey
   + 是非对称密钥顶层接口
   
 * KeyPair
   + 是非对称密钥的扩展，是密钥对的载体，称为密钥对。
   
 * Signature
   + 用来生成和验证数字签名
   + 生成签名数据和验证签名包括三个阶段
     - 初始化：初始化验证签名的公钥，初始化签署签名的私钥
     - 更新：根据初始化类型，可更新要签名或验证的字节
     - 签署或验证所有更新字节的签名
 * SignedObject
   + 签名对象
   
 * KeyStore
   + 密钥库，用于管理密钥和证书的存储
   + 类型：JKS, PKCS12
   
### javax.crypto
#### Mac
 * Message Authentication Code,  安全消息摘要也称为消息认证码
 * 不同于一般消息滴要，必须有一个由发送方和接收方共享的私密密钥才能生成最终的消息摘要。
 * 支持算法 HmacMD5, HmacSHA1, HmacSHA256, HmacSHA284, HmacSHA512

#### KeyGenerator
 * 生成秘密密钥
 
#### KeyAgreement
 * 密钥协定，在DH算法实现中使用
 
#### SecretKeyFactory
 * 生成秘密密钥
 
#### Chpher
 * JCE框架核心，实现加密解密功能
 * 参数transformation "算法/工作模式/填充模式"
 
#### SealedObject
 * 对象加密

### java.scurity.spec, javax.crypto.spec
 * 提供了密钥规范和算法参数规范
 * KeySpec, AlgorithmParameterSpec
 * KeySpec
   + X509EncodedKeySpec， 公钥，私钥
   + PKCS8EncodedKeySpec， 公钥，私钥
   + SecretKeySpec, 秘密密钥
 
#### EncodedKeySpec
 * 用编码格式来表示公钥，私钥
 * 把二近制密钥转成密钥对象
 * X509EncodedKeySpec, 用于转换公钥编码密钥
   + 以编码格式来表示公钥
 * PKCS8EncodedKeySpec, 用于转换私钥编码密钥
 
#### SecretKeySpec
 * 二近制密钥转成密钥对象
 * 私密密钥
 
#### DESKeySpec
 * 与SecretKeySpec一样，只是它指定了DES算法
 
### java.security.cert
 * 提供用于解析和管理证书，证书撤销列表(CRL)和证书路径的类和接口。
 
#### Sertificate
 * 是一个管理证书的抽象
 * 证书有多种类型：如X.509, PGP, SDSI
 
#### CertficateFactory
 * 证书工厂，可以通过它将证书导入到程序中
 * 此类定义了从相关编码中生成证书、证书路径和证书撤销列表对象的功能

#### X.509Certificate
 * 引类提供了一种访问x.509证书所有属性的标准方式
 
#### CRL
 > 证书可能由于各种原因失效，如由于申请证书的请求有问题，或者用户使用该证书做了非法操作
 > 这时证书将立即被置为无效。将证书置为无效的结果就是产生CRL
 > CA负责发布CRL， 其中列出了该CA已经撤销的证书，验证证书时，首先需要查询此列表，然后再考虑接受证书的合法性。
 
#### X509CRLEntry
 * 该类用于撤销证书

#### X509CRL
 * 标明了类型为X.509的CRL
 
#### CertPath
 *
 
### java.net.ssl
 * 主要用于安全套接字包的类

#### KeyManagerFactory
 * 密钥管理工厂
 
#### TrustManagerFactory
 * 管理信任材料的管理器工厂 
 
#### SSLContext
 * 安全套接字上下文
 
#### HttpsURLConnection
 * d
 