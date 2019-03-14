## 基础
 * Python是更高级语言

### 变量
 * 没有确定类型，因为是动态语言
 
### boolean
 * True, False

### None
 * 空
 
### 输入输出
 * input()
 * print()
 
### 除法
 * /  非整除
 * // 整除
 * %  取余
 
### 换行
 * ...
 
### bytes类型数据表示 
 * 用带b前缀的单引号或双引号表示
   + x = b'ABC'
   
### 字符串
 * 使用 '', ""
 * 不可变对象
   
### 字符串格式化
 * %d：整数
 * %s：字符串
 * %f：符点数
 * %x: 十六进制
 * %?: 点位符
   + %2d
   + %02d, 整数与浮点数可以补0
   + %.2f, 指定小数位数
 * format()函数
   + {0}, {1} 占位符
   + 'hello {0}, score {1:.1f}%'.format("zyq", 17.12)
   + xx.x%是保留小数点后几位
   
### list
 * list=['a', 123, 'c']
   + 中元素可以不同
 * 下标从0开始
 * -n 表示最后一个元素索引
 * list.append('d');
 * list.insert(1, '-a');
 * list.pop()
 * list[i] = 'rlp'
 * 空列表：[]
 * enumerate(list) 将list转dict
 
### tuple
 * 元组与list类似，一但初始化就不能再改变
 * tp=('a', 123, 'c)
 * 空元组：tp=()
 * 特殊创建一个元素
   + tp=(123,)
  
### range(n)
 * 0-n-1的数
 * 使用 list(rang(3))
 
### dic字典
 * 创建
  + dic={'key':value}
 * 'a' in dic 判断是否存在key
 * d.get('a'). 
   + 获取，如果不存在返回None
 * d.pop('a')
   
### set
 * s=set([1,2,3])
 * s.add()
 * s.remove()

## 函数
 * 定义使用**def** 语句依次是函数名，括号，参数，冒号:，缩进块，return
 * def hello(x):
     if x >= 0:
        return x
     return -x
 * 函数可以赋值给一个变量。此变量为函数的别名。通过别名可以引用函数
 * 空函数
   + def nop():
       pass
 * 异常
   + 参数个数不正确，参数类型不正确。则抛出TypeError
 * 变量类型判断 
   + isinstance(x, (int, float))
 * 返回多个值 
   def move(x, y):
     return x, y
   + p1, p2 = move(1,3)或 p = move(1,3)
   + print(p1, p2)
   + print(p): 其实返回的是一个tuple
   
### 函数参数
 * 可以定义必选参数：即位置参数
 * 默认参数，可变参数，关键字参数
 * 默认参数
   + def move(x, y=3):
   + 必选参数放在前，默认参数放在后
   + 默认参数，需指向不变对象
 * 可变参数
   + def move(*numbers):
   + 当list, tuple做为参数传递时可以使用*name, 传递
   如：list=[1,2,3] move(*list)
   + 其实传递进入被封装成了tuple
 * 关键字参数
   + def move(x,y, **kv)
   + 关键字参数是可选的。是用dict封装的key-value对象
   + 如果传递 extr={'a':1, 'b':2}
     使用move(1,2, **extr) 传递
     注意kw获得的dict是extra的一份拷贝，对kw的改动不会影响到函数外的extra。
 * 命名关键字参数
   + 如果只允许指定的key传入
   + def move(x, y, *, city, job), 通过*分隔，指允许key是city, job的传递
   + def move(x, y, *number, city, job) 如果已经有可变参数就不需要再指定*分隔
   
 * 参数组合
   + 顺序：必选参数，默认参数，可变参数，命名关键字参数，关键字参数
   

### 切片
 * list=[1,3,4,5,6]
   + list[:3] ：1，3，4
   + list[1,3] : 3,4
   + list[-2:-1] : 6
   + list[-3:] :后3个
   + list[:10:2] ：取前10个，每隔2个取一个
   + list[::5] : 每5个取一个
   + list[:] : 复制
 * 字符串，tuple的切分过程一样
 
### 迭代 
 * for key in dict:
 * for value in dict.values():
 * for key, v in dict.items():
 * for c in "abc":
 * 判断是否为可迭代对象
   + form collections import Iterable
   + isinstance('abc', Iterable)
 * for x,y in [(1,3),(3,4),(5,6)]
 
### 列表生成
 * list(rang(n))
 * [x*x for x in rang(1, 11)]
   + 1到11每个相乘写法
 * [x*x for x in rang(1,11) if x% 2==0]
   + for后可以写if
 * [m+n for m in 'abc' for n in 'xyz']
   + 双循环
 
### 生成器 generator
 * 延迟获取所有值。避免一下装入所有数据造成内存溢出
 * g=(x*x for x in rang(10)) 用()
 * 通过for v in g: 调用
 * 函数生成器，如果函数中把return 的地方改为yield。函数就成了一个生成器
 
### 迭代器 Iterator
 * list, dict, set, str 都是Iterable, 却不是Iterator
 * 使用Iter()将Iterable转为Iterator
 * Iterator表示一个数据流，只有调用next时，则计算下一个值，并不知道列表长度
 
### 函数式编程特点
 * 允许把函数本身作为参数传入另一个函数，还允许返回一个函数
 * 函数名其实是指向函数的变量
 
### 匿名函数 
 * lambda x: x * x
   + def f(x):
       return x *x

### 装饰函数 

### 偏函数
 * 通过functools.partial定义
   + int2= functools.partial(int, base=2)
 * 当函数的参数个数太多，需要简化时，使用functools.partial可以创建一个新的函数，
 这个新函数可以固定住原函数的部分参数，从而在调用时更简单。
 
## 模块
 * 一个模块就是一个文件
 * 函数名相同，使用模块区分。模块名相同使用包名区分
 * 任何模块代码的第一个字符串都被视为模块的文档注释；
 * 模块中测试入口，相当于java的main方法
 if __name__ == '__main__':
    test()
    
#### 导入
 * import package.module
   + 最后一项必须是包或模块，前面必须是包
 * from package import item
   + item可是模块，函数，变量
   + 首在当前包查找item是否定义过，如果没有将它做为模块导入。否则出错
  
#### python 包搜索路径
 * sys.path
 * 可以配置PYTHONPATH。直接添加到sys.path中
 
#### 私有变量方法
 * _, __开头
 
### 包
 * 不同目录代表不同包
   + 目录下必需有一个__init__.py文件也是模块，表示当前目录的模块
   如果没有__init__.py文件则被认为是一个普通的文件目录。
   + __all__=['a'] 定义加载模块
   
   
## Class
 * class Student(object):
    def __init__(self, name):
      self.name=name
    def print(self):
      print(self.name)
 * 使用
   + s1 = Student('a')
   s1.print()
 * 创建类 class 类名(object): 
   + object是继承的对象，如果没有就是object
 * 创建实例
   + 类名()
 * __init__ 
   + 因为类启到了模板的作用，创建时需要绑定属性就需要使用__init__
 * 定义方法
   + 第一个参数需要是self
   
 * 私有属性
   + 使用__开头定义变量名
 * 私有方法
 * 支持多态，继承， 多重继承
   + 多态接口传递不一定非是被继承类的子类，只是有相同接口就可以
   不一定是鸭子，只是像鸭子就可以。
   + 与java不同，java必须是子类

 * 类属性 
   + 直接写在class中的普通属性
   + 实例相同属性名会覆盖类中属性   
### dir(obj), getattr(obj), setattr(obj), hasattr(obj)
 * 判断一个对象有哪些属性

### 动态给类，实例绑定属性和方法
 * obj.name='a', obj.setName=MethodType(fn, obj)
   + __slots__ = ('name','age')
   限制动态绑定特定的属性。只对当前类实例有效，子类不生效，子类需要再重新定义
 * MyClass.setName=fn 
   + 对所有实例有效
   
### __getattr__ 当实例不存在属性会访问这个函数

### __call__ 对实例直接调用
 * s = MyClass()
 * s() 会调用__call__
 
### 枚举对象

### 通过type()动态创建Class
 * def fn(self, name='word'):
     print(name)
 * 创建：Hello = type('Hello', (object), dict(hello=fn))
   + 1类名
   + 2继承 
   + 3方法名=函数
   
### 单元测试
 * import unitest
 * 继承 unitest.TestCase
 * 已test开头的就去为测试方法
   + setUp: 测试开始前执行
   + tearDown：测试结束后执行
 * 运行 if__name=='__main__'
         unitest.main()
 * 运行 python -m unitest test_my 
    + 一般使用这种方式，一次测试多个
 
   
   