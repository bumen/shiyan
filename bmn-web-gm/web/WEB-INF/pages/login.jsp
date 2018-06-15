<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html><head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">

  <title>晓博圆-欢迎登录</title>
  <link type="text/css" rel="stylesheet" href="/css/base.css">

  <script type="text/javascript" src="/jquery/jquery-1.6.4.js"></script>
  <script type="text/javascript" src="/js/jsencrypt.min.js"></script>

  <!-- seajsnode: 用来确定base路径。base路径为js的根路径 -->
  <script id = "seajsnode" type="text/javascript" src="/js/sea.js"></script>
  <script type="text/javascript" src="/js/seajs/util-lang.js"></script>
  <script type="text/javascript" src="/js/seajs/util-events.js"></script>
  <script type="text/javascript" src="/js/seajs/module.js"></script>
  <script type="text/javascript" src="/js/seajs/util-cs.js"></script>
  <script type="text/javascript" src="/js/seajs/util-deps.js"></script>
  <script type="text/javascript" src="/js/seajs/util-path.js"></script>
  <script type="text/javascript" src="/js/seajs/util-request.js"></script>
  <script type="text/javascript" src="/js/seajs/config.js"></script>

  <script type="text/javascript" src="/js/login/login2016.js"></script>
  <script type="text/javascript" src="/js/login/login.index.js"></script>


</head>
<body>
<!-- 页头 -->
<div class="w">
  <div id="logo">
    <a href="https://www.baidu.com/" clstag="pageclick|keycount|20150112ABD|45" style="top:10px;">
      <img src="/images/login/l-icon.png" alt="小金圆" width="110" height="40" >
    </a>
    <!--<b></b>-->
  </div>
  <a href="javascript:void(0)" target="_blank" class="q-link"><b></b>登录页面，调查问卷</a>
</div>
<!-- 内容 -->
<div id="content">
  <div class="login-wrap">
    <div class="w">
      <!--登录面板-->
      <div class="login-form">
        <!--登录选项卡-->
        <div class="login-tab login-tab-l">
          <a href="javascript:void(0)" clstag="pageclick|keycount|201607144|1" class="" style="outline: rgb(109, 109, 109) none 0px;"> 扫码登录</a>
        </div>
        <div class="login-tab login-tab-r">
          <a href="javascript:void(0)" clstag="pageclick|keycount|201607144|2" class="checked" style="outline: rgb(109, 109, 109) none 0px;">账户登录</a>
        </div>

        <!--账号登录面板 -->
        <div class="login-box" style="display: block; visibility: visible;">
          <div class="mt tab-h">
          </div>
          <div class="msg-wrap">
            <div class="msg-error hide"><b></b></div>
          </div>
          <div class="mc">
            <div class="form">
              <form id="formlogin" method="post" onsubmit="return false;">
                <!--隐藏参数-->
                <input type="hidden" name="loginType" id="loginType" value="c" class="hide">
                <!--用户名-->
                <div class="item item-fore1">
                  <label for="loginname" class="login-label name-label"></label>
                  <input id="loginname" type="text" class="itxt" name="loginname" tabindex="1" autocomplete="off" placeholder="邮箱/用户名/已验证手机">
                  <span class="clear-btn"></span>
                </div>
                <!--密码-->
                <div id="entry" class="item item-fore2" style="visibility: visible;">
                  <label class="login-label pwd-label" for="nloginpwd"></label>
                  <input type="password" id="nloginpwd" name="nloginpwd" class="itxt itxt-error" tabindex="2" autocomplete="off" placeholder="密码">
                  <span class="clear-btn"></span>
                  <span class="capslock" style="display: none;"><b></b>大小写锁定已打开</span>
                </div>
                <!--验证码-->
                <div id="o-authcode" class="item item-vcode item-fore3  hide " style="display: none;">
                  <input id="authcode" type="text" class="itxt itxt02" name="authcode" tabindex="3">
                  <img id="Qvp_Verification1" class="verify-code"
                   src2=""
                   onclick="this.src= document.location.protocol + 'verify?yys='+new Date().getTime();$('#authcode').val('');">
                  <a href="javascript:void(0)" onclick="$('#Qvp_Verification1').click();">看不清换一张</a>
                </div>
                <!--注册-->
                <div class="item item-fore4">
                  <div class="safe">
                        <span> </span>
                        <span class="forget-pw-safe">
                            <a href="javascript:" class="" target="_blank" clstag="pageclick|keycount|201607144|3">忘记密码</a>
                        </span>
                  </div>
                </div>
                <!--登录btn-->
                <div class="item item-fore5">
                  <div class="login-btn">
                    <a href="javascript:" class="btn-img btn-entry" id="loginsubmit" tabindex="6" clstag="pageclick|keycount|201607144|3">登&nbsp;&nbsp;&nbsp;&nbsp;录</a>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>

        <!--扫码登录面板 -->
        <div class="qrcode-login" style="display: none; visibility: visible;">
          <div class="mc">
            <div class="qrcode-error-2016" style="left: 0px; display: none;">
              <div class="qrcode-error-mask"> </div>
              <p class="err-cont">服务器出错</p>
              <a href="javascript:void(0)" class="refresh-btn">刷新</a>
            </div>
            <div class="qrcode-main">
              <div class="qrcode-img" style="left: 64px;">
                  <img src="" alt="">
              </div>
              <div class="qrcode-help" style="display: none;"></div>
            </div>
            <div class="qrcode-panel">
              <ul>
                <li class="fore1">
                  <span>打开</span>
                  <a href="javascript:void(0)" target="_blank" clstag="pageclick|keycount|201607144|9">
                    <span class="red">手机应用</span></a>
                </li>
                <li>扫描二维码</li>
              </ul>
            </div>
            <div class="coagent qr-coagent" id="qrCoagent">
              <ul>
                <li><b></b><em>免输入</em></li>
                <li><b class="faster"></b><em>更快&nbsp;</em></li>
                <li><b class="more-safe"></b><em>更安全</em></li>
              </ul>
            </div>
          </div>
        </div>

        <div class="coagent" id="kbCoagent">
          <ul>
            <li>
              <b></b>
              <a href="javascript:void(0)" onclick="" clstag="pageclick|keycount|201607144|6" class="pdl"><b class="QQ-icon"></b><span>QQ</span></a>
              <span class="line">|</span>
            </li>
            <li>
              <a href="javascript:void(0)" onclick="" clstag="pageclick|keycount|201607144|7" class="pdl"><b class="weixin-icon"></b><span>微信</span></a>
            </li>
            <li class="extra-r">
              <div>
                <div class="regist-link">
                  <a href="javascript:void(0)" clstag="pageclick|keycount|201607144|8" target="_blank" style="outline: rgb(109, 109, 109) none 0px;">
                    <b></b>立即注册
                  </a>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!--背景图-->
    <div class="login-banner" style="background-color: #a09b9b">
      <div class="w">
        <div id="banner-bg" clstag="pageclick|keycount|20150112ABD|46" class="i-inner"
             style="background: url(/images/login/bg.jpg) 0px 0px no-repeat;background-color: #bb2934"></div>
      </div>
    </div>
  </div>
</div>
<!-- 页脚 -->
<div class="w">
  <div id="footer-2013">
    <div class="links">
      <a rel="nofollow" target="_blank" href="#">
        关于我们
      </a>
      |
      <a rel="nofollow" target="_blank" href="#">
        联系我们
      </a>
      |
      <a rel="nofollow" target="_blank" href="#">
        人才招聘
      </a>
      |
      <a rel="nofollow" target="_blank" href="#">
        商家入驻
      </a>
      |
      <a rel="nofollow" target="_blank" href="#">
        广告服务
      </a>
      |
      <a target="_blank" href="#">
        友情链接
      </a>
      |
      <a target="_blank" href="#">
        销售联盟
      </a>
    </div>
    <div class="copyright">
      Copyright&nbsp;&#169;&nbsp;2004-2017&nbsp;&nbsp;小金圆QVP.com&nbsp;版权所有
    </div>
  </div>
</div>

</body></html>
