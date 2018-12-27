## boot->context->properties

### ConfigurationPropertyName

#### of 构造合法名称
 * 名称组成
   + 字母
   + 数字
   + -，且不能开头
   + 其它都是非法字符
 
 * UNIFORM类型
   + 点分隔的合法名称
   + 如：log.name。其中log与name为UNIFORM类型
 * NUMERICALLY_INDEXED类型
   + 使用[Number]括住的类型
   + 如：log.[1]。其中1为NUMERICALLY_INDEXED类型
 * INDEXED类型
   + 使用[String]括信的类型
   + 如：log.[name]。其中name为INDEXED类型
   + 其中String，可是任意字符。包括非法字符
 * DASHED类型
   + 如果在UNIFORM类型的字符串中包函 -，时的类型
   + 如：log.name-zyq。其中name-zyq为DASHED类型
 * NON_UNIFORM类型
   + 为不合法类型
   + 如果字符串是UNIFORM，DASHED类型，其中包括了非法字符，则为NON_UNIFORM类型
   
 * 合法名称
   + log.name：解析后为log(UNIFORM), name (UNIFORM)
   + log.name-zyq：解析后为log(UNIFORM), name-zyq(DASHED)
   + log.[1] | log[1]：解析后为log(UNIFORM), 1(NUMERICALLY_INDEXED)
   + log.[name] | log[name]：解析后为log(UNIFORM), name(INDEXED)
   + log.[name.zyq]：解析后为log(UNIFORM), name.zyq(INDEXED)
   + log.[name-zyq]：解析后为log(UNIFORM), name-zyq(INDEXED)
   + log.[name+zyq]：解析后为log(UNIFORM), name+zyq(INDEXED)
   + log.[name[zyq]]：解析后为log(UNIFORM), name[zyq] (INDEXED)
    
#### append添加名称
 * 只能添加一级
   + log
   + log-zyq
   + [1]
   + [name]
   + [name-zyq]
   + 能不添加log.name, log[name]
   
 * 添加后
   + size+1
   + type[]， 中添加入新的类型
   + 新加原素加到resolved[size]对于位置
   + 原始start[], end[]不变
   
#### chop截取
 * 截取到指定长度
 * 截取后
   + size为指定长度
   + resolved[]减小到指定长度 
   + 原始type[], start[], end[] 不变，只是多的获取不到
  