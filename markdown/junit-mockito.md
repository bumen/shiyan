## mockito

### 静态引入
 * `org.mockito.Mockito.*;`
 
### 创建mock对象
 * 通过mock(T.class)创建
 * 通过@mock注解创建
   + 需要使用`MockitoAnnotations.initMocks(this)`初始化
   + 或使用`@RunWith(MockitoJUnitRunner.class)`来初始化
 
### 配置mock对象
 * when().thenReturn().thenReturn()
 * 根据输入参数返回  
 `when(a.say('a')).thenReturn(3);`
 * 让返回不依赖于输入  
 `when(a.say(anyString())).thenReturn(3)`
 * 根据参数类型返回  
 `when(a.say(isA(T.class))).thenReturn(3)`
 * 让返回不依赖于类型  
 `when(a.say(any(T.class))).thenReturn(3)`
  
### 无返回值
 * doReturn().when().methodCall()
   + 当调用obj的close()方法时抛出异常  
 `doThrow(new Exception()).when(a).close()`
 
### 验证mock对象方法是否被调用
 * 验证方法传入的特定参数是否被调用
 * 是一种行为测试，不会检查方法的返回值
 * `verifty(a).say('a')`  
 * 其它
   ```
      verify(mock, never()).someMethod("never called");
      verify(mock, atLeastOnce()).someMethod("called at least once");
      verify(mock, atLeast(2)).someMethod("called at least twice");
      verify(mock, times(5)).someMethod("called five times");
      verify(mock, atMost(3)).someMethod("called at most 3 times");
   ```
 
### @InjectMocks 依赖注入
 * 根据类型来注入对象里的成员方法和变量
 * 注入的成员变量与方法都是由mock管理的对象
 
### ArgumentCaptor 捕捉参数
 * 可以捕捉方法执行时，输入的参数
 * 是在方法执行后进行捕捉
   ``` 
    Resource resource = new ClassPathResource("refresh.json");
    String body = FileUtils.readFileToString(resource.getFile(), "utf-8");
    CloseableHttpResponse resp = mock(CloseableHttpResponse.class);
    StatusLine statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(200);
    HttpEntity entity = new StringEntity(body, "UTF-8");
    when(resp.getStatusLine()).thenReturn(statusLine);
    when(resp.getEntity()).thenReturn(entity);
    when(client.execute(Mockito.any(HttpPost.class))).thenReturn(resp);
  
    String refreshToken = "x";
    SmartToken token = broadConnector.refresh(refreshToken);
    Assert.assertEquals("refresh2xdb6", token.getAccessToken());
    Assert.assertEquals(1209600l, token.getExpiresIn());
    Assert.assertEquals("refresh98x9", token.getRefreshToken());
  
    ArgumentCaptor<HttpPost> argument = ArgumentCaptor.forClass(HttpPost.class);
    verify(client).execute(argument.capture());
  
    String url = "https://x/oauth/v2/token?grant_type=refresh_token&client_id=x&client_secret=x&refresh_token=x";
    Assert.assertEquals(url, argument.getValue().getURI().toString());
   ```
   
### 限制 
 * 三种类型不能被测试
   + final classes
   + anonymous classes
   + private types
   