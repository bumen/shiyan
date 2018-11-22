## converter 类型转换器

### ConverterService使用
 * 添加Converter三个方法
   + addConverter
     - 会被内部适配成ConditionalGenericConverter
     - 再getConverter时，会调用适配器matches
   + addConverterFactory
     - 会被内部适配成ConditionalGenericConverter
     - 再getConverter时，会调用适配器matches
   + addGenericConverter
     - 直接注册，不适配
     - 再getConverter时，如果实现ConditionalConverter会调用matches
   
 * convert
   + 先去调用getConverter获取convert
   + 然后convert
   
 * getConverter
   + 通过sourceType, targetType去查找匹配convert
   + 查找过程
     - 如果根据类型找到convert，再判断convert类型
     - 类型如果是普通convert直接返回
     - 类型是ConditionalGenericConverter，则会调用matches方法成功返回
     - 否则返回null
   
 * 子类可以重写方法
   + convertNullSource
     - 当convert时，if source == null，则会调用这个方法
   + getDefaultConverter
     - 如果getConverter没有找到convert时，会调用getDefaultConverter
     
     
### ConverterFactory<S,R>
 * 一个类型可以转换成一个父类的多个子类型
 如果实现ConditionalConverter，则可以根据具体类型返回不同Converter
 
 * 将S类型转为R类型
 * 但是R类型一般为父类
   + 如：Enum（表示各种Enum类型都可以）
   + 如：Number(表示String 可以转成 byte, short, int, long, float, double)
   + 如果只实现ConverterFactory接口，则默认只有转成父类型
 * 如果同时实现ConditionalGenericConverter
   + 可以通过getConvertibleTypes方法返回多个转换组合
   + 同时可以实现matches确定是否匹配
 
### Converter<S, R>
 * 将S类型转为R类型
 * 最普通的直接转换
 
### GenericConverter
 * 主要方法是getConvertibleTypes，返回convert所要转换的类型
   + 即一个转换器可以匹配多种类型的转换
   + addConverter果，会根据其返回值绑定convert
   当getConverter时，先匹配返回值，找到对应convert
   如果实现是ConditionConverter再去调用matches
   
 * ConvertService再添加convert时，内部都把convert转换成GenericeConvert
   + 通接口适配器模式完成转换
   
### ConditionalConverter
 * 再ConvertService调用getConvert时判断
 如果是ConditaionalConverter，则会调用matches方法再去匹配
 看看是否真正可以转换
 
### ConditionalGenericeConverter
 * 是GenericConverter与ConditionalConverter组合
 
 
### 使用
 * 目标类型只匹配一种确定的类型
   + 直接使用Converter
 * 目标类型只需要匹配父类
   + 直接使用ConverterFactory
 * 目标类型只需要匹配相同父类，不同子类。每个子类不同转换方式
   + 使用ConverterFactory+ConditionalGenericeConverter
 * 可以实现多种source与target匹配
   + 使用GenericConverter