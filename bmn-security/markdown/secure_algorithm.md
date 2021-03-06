## 对称加密

### 使用
 * DES算法使用DESKeySpec
 * DESede算法使用DESedeSpec
 * AES算法使用SecretKeySpec
 
### DES
 * 默认支持56位，现在已经不安全了。算法半公开。
 * 算法/工作模式/填充方式
 
### DESede
 * 是DES的改良，但处理速度较慢，密钥计算时间长，加密效率不高
 
### AES
 * 高级数据加密标准
 * 密钥设置快，存储要求低。
 
### IDEA
 * 国际数据加密标准
 * 与AES类似
 
### PBE
 * 基于口令加密
 * 没有密钥概念，使用口令代替密钥。
 * 口令便于记忆
 * 实现通过盐和DES或AES算法结合实现
 * 工作模式：CBC
 * 密钥长度为AES或DES算法密钥长度
 
## 非对称加密

### DH
 * 密钥交换算法
 * A产生pubA, priA
 * A将pubA发给B, B用pubA产生pubB, priB
 * B将pubB发给A
 * A使用priA + pubB 产生私密密钥s1
 * B使用priB + pubA 产生私密密钥s2
 * s1 == s2
 * DH算法支持的密钥长度是64的倍数取值在512-1024之间
 * 对称加密算法DES, AES DESede
 * 合理使用密钥长度和对称加密算法是DH算法的密码系统的关键。
 
### RSA
 * 支持“私钥加密，公钥解密”与“公钥加密，私钥解密”
 
### EIGamal
 * 只支持"公钥加密，私钥解密"
 

## 数字签名
 * 是非对称加密算法与消息摘要结合体
 * 遵循：私钥签名，公钥验证
 * 具有数据完整性，认证性，抗否性
 * 是对明文的签名验证过程。不对明文进行加密，解密
 
### RSA
 * 即包含加密/解密算法，同时兼有效数字签名算法
### DSA
 * 权包含数字签名算法
 * 权支持SHA系列的消息摘要算法
 
### ECDSA
 * 具有速度快，强度高，签名短优点
 
## 数字证书
 * 申请是对公钥做数字签名，证书的验证过程是对公钥做签名验证，还有有效期验证
 * DSA算法的证书无法完成加密、解密实现，这样的证书不包括加密，解密功能。
 * 文件编码格式
   + CER(canonical encoding rules规范编码格式)
     - 是BER(basic 基本编码格式)的一个变种，更加严格
     - 使用变长模式
   + DER(distinguished encoding rules 卓越编码格式)
     - 是BER的一个变种
     - 使用定长模式
   + PKCS(public-key cryptography standards, 公钥加密标准)
     - 所有证书都符合公钥基础设施(PKI), 制定的ITU-X509国际标准(x.509标准), 共三个版本
     - PKCS已经发布了15个标准，常用PKCS#7, PKCS#10, PKCS#12
     - 以上标准主要用于证书的申请和更新等操作
     - 常用Base64编码格式作为数字证书文件存储格式
 * 自签名证书
   + 证书申请者为自己的证书签名，内部使用
     
 * openssl命令
   + `openssl genrsa -aes128 -out private/ca.key.pem 1024`
   + `E:\ca>openssl req -new -key private/ca.key.pem -config "D:\Bumen Files\openssl-0
      .9.8\share\openssl.cnf" -out private/ca.csr -subj "/C=CN/ST=BJ/L=BJ/O=zlex/OU=zl
      ex/CN=*.zlex.org"`
   + 
     
### 证书签发
 * 由数字证书需求方产生自己的密钥对
 * 由数字证书需求方将算法，公钥和证书申请都身份信息传送给认证机构
 * 由认证机构核实用户身份，执行相应必要的步骤，确保用户身份信息正确
 * 由认证机构将数字证书颁发给用户
 
 
### 证书使用
 * 密钥库管理私钥，数字证书管理公钥
 
### SSL/TLS
 * 协商算法
 * 验证证书
 * 产生密钥
 
### 单向认证服务
 * 仅需要服务器提供证书，验证服务器身份。
 
### 双向认证服务
 * 需要根证书，服务器认证，客户证书3项证书