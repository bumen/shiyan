## python模块
 * 在Python中，一个.py文件就称之为一个模块（Module）
 * 为了避免模块名冲突，Python又引入了按目录来组织模块的方法，称为包（Package）
 
 
### package
 * 每一个包目录下面都会有一个__init__.py的文件，这个文件是必须存在的，
 否则Python就把这个目录当成普通目录，而不是一个包。
 * __init__.py可以是空文件，也可以有Python代码.
 因为__init__.py本身就是一个模块，而它的模块名就是当前目录名。
 
 
### class
 * _name，这样的实例变量外部是可以访问的，但是，按照约定俗成的规定，当你看到这样的变量时
 * __name, 不能直接访问__name是因为Python解释器对外把__name变量改成了_Student__name，
 所以，仍然可以通过_Student__name来访问__name变量
 
### __slots__
 * 为了达到限制的目的，Python允许在定义class的时候，定义一个特殊的__slots__变量，
 来限制该class实例能添加的属性：
 ``` 
    class Student(object):
        __slots__ = ('name', 'age') # 用tuple定义允许绑定的属性名称
 ```
 * 使用__slots__要注意，__slots__定义的属性仅对当前类实例起作用，对继承的子类是不起作用的：
   

   
### @property
 * 可以直接使用属性名赋值，同时会执行方法调用
 ``` 
    class Student(object):
    
        @property
        def score(self):
            return self._score
    
        @score.setter
        def score(self, value):
            if not isinstance(value, int):
                raise ValueError('score must be an integer!')
            if value < 0 or value > 100:
                raise ValueError('score must between 0 ~ 100!')
            self._score = value
     
 ```
   + 把一个getter方法变成属性，只需要加上@property就可以了，
   此时@property本身又创建了另一个装饰器@score.setter，负责把一个setter方法变成属性赋值
   + s.score = 9999 相录于调用方法s.score(9999)