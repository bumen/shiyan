## python基础
 
### 参数类型判断
``` 
def my_abs(x):
    if not isinstance(x, (int, float)):
        raise TypeError('bad operand type')
    if x >= 0:
        return x
    else:
        return -x
```

### 默认参数
 * 必选参数在前，默认参数在后，否则Python的解释器会报错（思考一下为什么默认参数不能放在必选参数前面）；
 * 如何设置默认参数。
 * 也可以不按顺序提供部分默认参数。当不按顺序提供部分默认参数时，需要把参数名写上。  
   `enroll('Adam', 'M', city='Tianjin')`
 *  定义默认参数要牢记一点：默认参数必须指向不变对象！

### 可变参数
 * 定义：
   + 参数numbers接收到的是一个tuple
 ``` 
 def calc(*numbers):
     sum = 0
     for n in numbers:
         sum = sum + n * n
     return sum
 ```
 * 如果已经有一个list或者tuple，要调用一个可变参数怎么办？
 ```
    >>> nums = [1, 2, 3]
    >>> calc(*nums)
    14
 ```
   + *nums表示把nums这个list的所有元素作为可变参数传进去。这种写法相当有用，而且很常见。
   
### 关键字参数
 * 定义
   + **kw是关键字参数
   + 关键字参数是可选的
   + 函数的调用者可以传入任意不受限制的关键字参数
 ``` 
    def person(name, age, **kw):
        print('name:', name, 'age:', age, 'other:', kw)
 ```
 * 调用
   + **extra表示把extra这个dict的所有key-value用关键字参数传入到函数的**kw参数，
   kw将获得一个dict，注意kw获得的dict是extra的一份拷贝，对kw的改动不会影响到函数外的extra
 ``` 
    >>> extra = {'city': 'Beijing', 'job': 'Engineer'}
    >>> person('Jack', 24, **extra)
 ```
 
### 命令关键字参数
 * 和关键字参数**kw不同，命名关键字参数需要一个特殊分隔符*，*后面的参数被视为命名关键字参数
 * 定义
   + 只接收city和job作为关键字参数
 ``` 
    def person(name, age, *, city, job):
        print(name, age, city, job)
 ```
 * 调用
 ``` 
    >>> person('Jack', 24, city='Beijing', job='Engineer')
    Jack 24 Beijing Engineer
 ```
 * 如果函数定义中已经有了一个可变参数，后面跟着的命名关键字参数就不再需要一个特殊分隔符*了
 ``` 
    def person(name, age, *args, city, job):
        print(name, age, args, city, job)
 ```
 
### 参数组合
 * 参数定义顺序
   + 必选参数
   + 默认参数
   + 可变参数
   + 命名关键字参数
   + 关键字参数
   
 * 定义
 ``` 
    def f1(a, b, c=0, *args, **kw):
        print('a =', a, 'b =', b, 'c =', c, 'args =', args, 'kw =', kw)
    
    def f2(a, b, c=0, *, d, **kw):
        print('a =', a, 'b =', b, 'c =', c, 'd =', d, 'kw =', kw)
 ```
 
### 递归调用
 * 解决递归调用栈溢出的方法是通过尾递归优化，事实上尾递归和循环的效果是一样的，所以，把循环看成是一种特殊的尾递归函数也是可以的。
 * 尾递归是指，在函数返回的时候，调用自身本身，并且return语句不能包含表达式。
 这样编译器或者解释器就可以把尾递归做优化，使递归本身无论调用多少次，都只占用一个栈帧，不会出现栈溢出的情况。
 * 遗憾的是，大多数编程语言没有针对尾递归做优化，Python解释器也没有做优化，所以，即使把上面的fact(n)函数改成尾递归方式，也会导致栈溢出。
   

### 列表生成器
 *  [x * x for x in range(1, 11)]
 
### 生成器
 * 通过列表生成式，我们可以直接创建一个列表。但是，受到内存限制，列表容量肯定是有限的。
 而且，创建一个包含100万个元素的列表，不仅占用很大的存储空间，如果我们仅仅需要访问前面几个元素，
 那后面绝大多数元素占用的空间都白白浪费了。
   + 所以，如果列表元素可以按照某种算法推算出来，那我们是否可以在循环的过程中不断推算出后续的元素呢
 * 创建生成器  
   ` g = (x * x for x in range(10))`
   + 创建L和g的区别仅在于最外层的[]和()，L是一个list，而g是一个generator。
 * 如果要一个一个打印出来，可以通过next()函数获得generator的下一个返回值：
   `next(g)`   
 * 一般使用for来迭代
 
### 迭代器
 * 这些可以直接作用于for循环的对象统称为可迭代对象：Iterable。
 * 可以被next()函数调用并不断返回下一个值的对象称为迭代器：Iterator。
 * 生成器都是Iterator对象，但list、dict、str虽然是Iterable，却不是Iterator。
 * 把list、dict、str等Iterable变成Iterator可以使用iter()函数：
 ``` 
 >>> isinstance(iter([]), Iterator)
 True
 >>> isinstance(iter('abc'), Iterator)
 True
 ```
 
### 闭包函数
 * 相关参数和变量都保存在返回的函数中
 * 当一个函数返回了一个函数后，其内部的局部变量还被新函数引用
 * 注意
   + 返回函数不要引用任何循环变量，或者后续会发生变化的变量。
   
### 匿名函数
 * lambda
   + 关键字lambda表示匿名函数，冒号前面的x表示函数参数。  
   `lambda x: x * x`  
   + 匿名函数有个限制，就是只能有一个表达式，不用写return，返回值就是该表达式的结果。
     
### 装饰器
 * 因为它是一个decorator，所以接受一个函数作为参数，并返回一个函数
 * 定义：参数是一个函数
 ``` 
    def log(func):
        def wrapper(*args, **kw):
            print('call %s():' % func.__name__)
            return func(*args, **kw)
        return wrapper
 ```
   + 使用
   ``` 
    @log
    def now():
        print('2015-3-25')
      
    相当于now = log(now)
   ```
   + 原来的now()函数仍然存在，只是现在同名的now变量指向了新的函数

* 如果decorator本身需要传入参数，那就需要编写一个返回decorator的高阶函数，写出来会更复杂
``` 
def log(text):
    def decorator(func):
        def wrapper(*args, **kw):
            print('%s %s():' % (text, func.__name__))
            return func(*args, **kw)
        return wrapper
    return decorator
```
   + 相当于  
   `now = log('execute')(now)`
   
 * 因为我们讲了函数也是对象，它有__name__等属性，但你去看经过decorator装饰之后的函数，
 它们的__name__已经从原来的'now'变成了'wrapper'
   + 因为返回的那个wrapper()函数名字就是'wrapper'，所以，需要把原始函数的__name__等属性复制到wrapper()函数中，
   否则，有些依赖函数签名的代码执行就会出错。
   + 不需要编写wrapper.__name__ = func.__name__这样的代码，
   Python内置的functools.wraps就是干这个事的，所以，一个完整的decorator的写法如下：
   ``` 
    import functools
    
    def log(func):
        @functools.wraps(func)
        def wrapper(*args, **kw):
            print('call %s():' % func.__name__)
            return func(*args, **kw)
        return wrapper
        
    
    def log(text):
        def decorator(func):
            @functools.wraps(func)
            def wrapper(*args, **kw):
                print('%s %s():' % (text, func.__name__))
                return func(*args, **kw)
            return wrapper
        return decorator
   ```
     
