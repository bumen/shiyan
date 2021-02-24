### mockito使用

### 与junit一起使用时，创建由Mockito模拟的对象有两种方法
 1. 通过注释
    + 使用@RunWith(MockitoJUnitRunner.class)
    ``` 
        @RunWith(MockitoJUnitRunner.class)
        public class FooTest {
            @Mock
            private Bar barMock;
        
            // ...
        }
    ```
    + 你也可以使用Mockito的JUnit @Rule ，它提供与MockitoJUnitRunner 相同的功能，但不需要@RunWith 测试运行器：
    ``` 
        public class FooTest {
            @Rule
            public MockitoRule mockito = MockitoJUnit.rule();        
        
            @Mock
            private Bar barMock;
        
            // ...
        }
    ```
    + 手动初始化
    ``` 
        public class FooTest {
            @Mock
            private Bar barMock;
        
            @Before
            public void setUp() {
                MockitoAnnotations.initMocks(this);
            }
        
            // ...
        }
    ```
 
 2. 通过模拟功能
    + 普通类
    ``` 
        public class FooTest {
            private Bar barMock = Mockito.mock(Bar.class);
        
            // ...
        }
         
    ```
    + 泛型类
    ``` 
        public class FooTest {
            private Bar<String> genericBarMock = (Bar<String>) Mockito.mock(Bar.class);
        
            // ...
        }
    ```
    + 深度mock
    ``` 
        channel = mock(Channel.class, RETURNS_DEEP_STUBS);
    ```
    
### spy
 * 使用
 ``` 
    handler = spy(new ResultSelectHandler(propertiesHolder, REGEX, timeoutManager));
 ```
 
 
### Mockito将尝试按以下顺序解决依赖注入
 1. 基于构造函数的注入 - 使用大多数参数将模拟注入到构造函数中（如果找不到某些参数，则传递空值）。
 如果通过构造函数成功创建了对象，则不会应用其他策略。
    - 使用@Mock注解时
 2. 基于定位器的注射 - 模拟按类型注入。如果存在多个相同类型的属性，则将匹配属性名称和模拟名称。
    - 注入：@InjectMocks
 3. 直接进样 - 与基于setter的进样相同。
    - 跟据setting注入
 4. 如果都没有，则为默认值
 
### 参数捕获
 * 只能用在verify和stubbing中
 * 如：
  ``` 
        @Mock
        private GatePort port;  // 有一个void someMethod(String s); 方法
        
        public void test() {
            // 不能这么用参数捕获
            doAnswer(i->{
                    return null;
                })
                .when(port).someMethod(anyObject());
            
        }
        将抛出异常为
        ：you cannot use argument matchers outside of verification or stubbing 
  ```
 * 参数匹配器的注意点
   + 如果你使用参数匹配器,所有参数都必须由匹配器提供。
   
### ArgumentCaptor使用
 * 通过verify来捕获参数
 ```` 
    @Mock
    private Foo fooMock;
    
    @InjectMocks
    private Bar underTest;
    
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    
    @Test
    public void should_test_smth() {
        underTest.doSmth();
    
        Mockito.verify(fooMock).bla(stringCaptor.capture());
    
        assertThat(stringCaptor.getValue(), is("val"));
    }
 ````
  
### void 方法可以使用doThrow（） ， doAnswer（） ， doNothing（） ， doCallRealMethod（）方法系列进行存根 。
 