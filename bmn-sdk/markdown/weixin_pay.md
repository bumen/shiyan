## 微信支付 -公众号支付
 * 前端jsapi， 需要先进行微信认证通过
 * 前端代码需要外网环境，且使用外网域名访问到
 * 前端请求后端需要使用服务器内网穿透
   + 这一步可以走代理实现


1. 服务器配置
 * 后端调用的微信所有接口，需要API安全Key
 * API安全key用来签名
 * Sandboxnew 测试环境， 需要获取Sandboxnew API安全key
 * 正试环境，需要商店中配置生自己的API安全Key
 
2. 请求支付
 * 后端，调微信下订单接口
 * 订单时，参数需要签名
 * 发送给微信
 * 微信返回提交结果
 * 注意
   + clientIP 可以是127.0.0.1
   + openId 要真实
 
3. 根据下单结果，获取JSAPI调用参数
 * 对返回参数进行签名，与服务器接口签名方式，API安全key使用同样。
 * 签名参数  
 [ps] ：prepay_id 通过微信支付统一下单接口拿到，
 paySign 采用统一的微信支付 Sign 签名生成方法，
 注意这里 appId 也要参与签名，appId 与 config 中传入的 appId 一致，
 即最后参与签名的参数有
   * [appId, timeStamp, nonceStr, package, signType]。
   * 需要区分大小写

 
4. 提供回调接口
 * 前端支付成功后，由微信方调用通知，会通知多次可能
 * 处理完成后需要返回
 ```
          InputStream inputStream = request.getInputStream();
          InputSource inputSource = new InputSource(inputStream);
          inputSource.setEncoding("utf-8");
  
          String r = WeixinPayServices.getInstance().payCallback(inputSource);
  
          inputStream.close();
  
          // 一定要配置头，如果微信判断通知失败，则会多次通知
          response.setCharacterEncoding("utf-8");
                  
          response.setContentType("text/xml;charset=UTF-8");
          try (PrintWriter out = response.getWriter()) {
              out.println(r);
              out.flush();
          }
 ```

5. 调用查询接口
 

### 前端JSAPI

1. 初始化接口认证

2. 支付
 * success function中不能使用callback.call(target) 回调方式
```
   pay: function(param) {
      	
      	wx.chooseWXPay({
  			  timestamp: param.timestamp,
  			  nonceStr: param.nonceStr,
  			  package: param.packageStr,
  			  signType: param.signType,
  			  paySign: param.paySign,
  			  success: function(res){
  				  if(res.errMsg == "chooseWXPay:ok") {
  					  param.callbackTarget.back();
  				  }
  			  }
      	});
      	  
  	    console.log("hehe: " + "update7");
      }
```