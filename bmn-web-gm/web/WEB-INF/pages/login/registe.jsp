<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content=all name=robots>


<title>注册页面 </title>
<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/js/adr_yym.js"></script>
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/registerCheck.js?1"></script>
<style>
BODY {
	FONT-SIZE: 12px; COLOR: #000; font-family:Arial, Helvetica, sans-serif,"宋体"; margin:0px auto;overflow-x:hidden;word-break:break-all;
	/*
	background:url(2nd_bodybg.jpg) repeat-x #fff;
	*/
}
IMG {
	BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; VERTICAL-ALIGN: middle; BORDER-RIGHT-WIDTH: 0px
}
H1 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: normal; FONT-SIZE: 100%; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
H2 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: normal; FONT-SIZE: 100%; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
H3 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: normal; FONT-SIZE: 100%; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
H4 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: normal; FONT-SIZE: 100%; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
H5 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: normal; FONT-SIZE: 100%; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
H6 {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-WEIGHT: normal; FONT-SIZE: 100%; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
.clear {
	CLEAR: both; OVERFLOW: hidden; WIDTH: 1px; LINE-HEIGHT: 0px; HEIGHT: 0px
}
DL {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
DT {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
DD {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px
}
UL {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; LIST-STYLE-POSITION: outside; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px; LIST-STYLE-TYPE: none
}
LI {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; LIST-STYLE-POSITION: outside; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px; LIST-STYLE-TYPE: none
}
TD {PADDING-RIGHT: 10px; PADDING-LEFT: 10px;  PADDING-BOTTOM: 5px; MARGIN: 0px; PADDING-TOP: 5px; 
}
A:link {
	COLOR: #000; TEXT-DECORATION: none
}
A:visited {
	COLOR: #000; TEXT-DECORATION: none
}
A:hover {
	COLOR:#009900; TEXT-DECORATION: none
}
/*顶部开始==================================================================================*/
#topr{width:800px; height:60px;margin:10px auto; border:1px solid #AED2F0; overflow:hidden; position:relative; padding-bottom:10px; background:#F0F7FD;}
#topr .logo{ margin-top:10px; margin-left:20px;}
#topr .other{ margin-top:30px; margin-right:20px; }
/*中部开始==================================================================================*/
#middler{width:800px; margin:10px auto; margin-bottom:0px; border:1px solid #CCCCCC; height:auto; overflow:hidden; padding-top:105px; position:relative; padding-bottom:10px; background:#fff;}

#middler .left{width:790px; margin:0px auto}
#middler .left dl{width:780px; margin:0px auto;overflow:hidden; height:30px; line-height:24px; padding-top:4px;border:1px dashed #f9f9f9; padding-left:5px; padding-right:5px; }
#middler .left dl:hover{ background:#F5FAFE ;border:1px solid #DEEDFA;}
#middler .left dl dt{width:380px;overflow:hidden}
#middler .left dl dd{width:auto;overflow:hidden; line-height:24px;}

#middler .left dl dt div.biaoti{width:100px; padding-right:10px;overflow:hidden; line-height:29px;font-size:12px; font-weight:normal;}
#middler .left dl dt div.shurukuang{width:270px;overflow:hidden}
#middler .left dl dt div.shurukuang input{width:250px; height:20px; border:1px solid #ccc; background-color:#fff;}
#middler .left dl dt div.shurukuang textarea{width:250px; height:20px; border:1px solid #ccc; background-color:#fff;}
#middler .left dl dt div.shurukuang select{height:24px;}
#middler .left dl dd span{ margin:0px; padding:0px; color:#555}
/*顶部开始==================================================================================*/
#bottomr{width:800px; margin:10px auto; margin-bottom:0px; }
#bottomr .menu{margin:10px auto; width:100%; text-align:center; margin-bottom:0px; font-size:12px;}
#bottomr  a{ color:#4B4B4B}
#bottomr  a:hover{ color:#B5381E}
#bottomr .menu ul{width:630px; margin:0px auto;}
#bottomr .menu ul li{ float:left; width:70px;}
#bottomr .copyright{width:100%; margin:10px auto; text-align:center;color:#4B4B4B}
/*其它==================================================================================*/
.fr{
float:right}
.fl{
float:left}
.t-a-l{
text-align:left}
.t-a-r{
text-align:right}
.t-a-c{
text-align:center}
.hongse {
    color:#FF0000;
}
.lvse{
	color:#009900}
.lanse{
	color:#0000CC}

.jiacu {
    font-weight:bold;
}
</style>
</head>
<body>
<!-- top-->

<div id="topr">
    <div class="logo fl"><!-- img src="images/logo.gif" /--></div>
    <div class="other fr"><a href="" target="_blank" style="display:none">首页</a> &nbsp; &nbsp;
    &nbsp; &nbsp;<a href="login" target="_self">登录</a></div>
</div>
	<div id="middler">
    <div style="position:absolute;top:10px; font-size:16px; padding-bottom:2px; padding-left:30px;left:20px; width:790px; height:30px; line-height:30px;">
    <span class="lanse jiacu">创建您的GM管理员帐号 </span>&nbsp;
    <span style="font-size:12px;">(带&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>&nbsp;为必填项。)</span>
    </div>
    <div style="position:absolute;top:40px; padding-bottom:2px;padding-left:30px; left:20px; width:790px; height:30px; line-height:30px;"" class="lanse">
    	这是您的管理员帐号，创建成功后，您可以登录。
    </div>
    <div style="position:absolute;top:60px; padding-bottom:2px;padding-left:30px; left:20px; width:790px; height:30px; line-height:30px;" class="lanse">
     </div>
 <div class="left">
 <span id="submitTip"></span>
 <form name="registerForm" id="registerForm" action="signUp"  method="post" >
   <!-- <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!{newAdmToken}">
   <input type="hidden" id="hiddenProvince"  name="hiddenProvince" value="$!{NewAdminUserForm.province}" />
   <input type="hidden" id="hiddenCity" name="hiddenCity" value="$!{NewAdminUserForm.city}" />
   <input type="hidden" id="comFrom" name="comFrom" value="$!{NewAdminUserForm.comFrom}" /> 
   <input type="hidden" id="agentStrid" name="agentStrid" value="$!{agentStrid}" />
   <input type="hidden" id="origin_media" name="origin_media" value="$!{origin_media}" />
   <input type="hidden" id="longitude" name="longitude" value="$!{NewAdminUserForm.longitude}" />
   <input type="hidden" id="latitude" name="latitude" value="$!{NewAdminUserForm.latitude}" />-->
 <dl>
     <dt class="fl">
         <div class="biaoti fl t-a-r">登录名：</div>
         <div class="shurukuang fl t-a-l">
         <INPUT name="uin" id="strid"
    		    type="text" 
    			value="" 
    			maxLength="32" 
    			size="30" 
    			onFocus = "javascript:stridGetFocus()"   
    			onBlur="javascript:stridLoseFocus()"  />&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
    	 </div>
     </dt>
     <dd class="fl">
		<span id="stridTip">以字母开头，由字母、数字、下划线组成，总长度为3~20位</span>  
     </dd>
 </dl>
 <dl>
     <dt class="fl">
         <div class="biaoti fl t-a-r">登录密码：</div>
         <div class="shurukuang fr t-a-l">
           <input name="password" id="passWord"
				  onpaste="return false;"
                  type="password" 
                  value=""
                  maxlength="32" size="30"
                  styleclass="12black"                   
                  onblur="javascript:passWordLoseFocus()" 
                  onfocus = "javascript:passWordGetFocus()" />&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
	 </dt>
     <dd class="fl">
		<span id="passWordTip">3-32个字符组成，区分大小写</span> 
     </dd>
 </dl>
 <dl>
     <dt class="fl">
         <div class="biaoti fl t-a-r">确认密码：</div>
         <div class="shurukuang fr t-a-l">
           <input id="surePassWord" 
				  onpaste="return false;"
    			  type="password" 
                  value=""                
                  maxlength="32" size="30" 
                  styleclass="12black" 
                  onblur = "javascript:surePssWordLoseFocus()" 
                  onfocus = "javascript:surePassWordGetFocus()"/>&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="surePassWordTip">请再次输入您的密码</span>  
     </dd>
 </dl>
 
<div style="border-bottom:0px dashed #ccc; width:100%; background:none; height:20px;"></div>

 <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">姓名：</div>
         <div class="shurukuang fr t-a-l">
         <input id="nickName"
				type="text"
				value=""  
				maxLength="8" size="30" 
				styleClass="12black"  
				onblur="javascript:nickNameLoseFocus()"  
				onFocus = "javascript:nickNameGetFocus()"/>&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="nickNameTip">不超过8个汉字，或8个字节(数字，字母和下划线)</SPAN> 
     </dd>
 </dl>
 <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">电子邮箱：</div>
         <div class="shurukuang fr t-a-l">
           <input id="email"
        		  type="text"
                  value=""              
                  maxlength="50"  
                  styleclass="12black" 
                  onblur = "javascript:emailLoseFocus()" 
                  onfocus= "javascript:emailGetFocus()" />&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="emailTip">为了便于找回密码等重要服务,请正确输入邮箱</span>  
     </dd>
 </dl>

 <dl title="公司电话或手机号码必须至少填写一项" style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">手机号码：</div>
         <div class="shurukuang fr t-a-l">
           <input id="mobile" 
				  type="text" 
   				  value="公司电话或手机号码必须至少填写一项" 
   				  style="color:#ccc;"
				  size="24"
                  onblur = "javascript:mobileLoseFocus()" />
  		 <font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="mobileTip">为了便于找回密码等重要服务,请正确输入手机号码</span></font>
	</dd>
 </dl>
 <br>
 <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">公司网址：</div>
         <div class="shurukuang fr t-a-l">
           <input  id="homePage"
				  type="text"
        		  value=""                              
                  maxlength="50"  
                  styleclass="12black" 
                  onblur = "javascript:homePageLoseFocus()" 
                  onfocus = "javascript:homePageGetFocus()" />&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="homePageTip"  calss="">请输入真实网址，嵌入代码开通服务</span>  
     </dd>
 </dl>
 
 <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">公司名称：</div>
         <div class="shurukuang fr t-a-l">
           <input  id="company"
    			  type="text"
                  value=""                              
                  maxlength="50"  
                  styleclass="12black"                  
                  onblur = "javascript:companyLoseFocus()" />&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="companyTip"  calss="">请输入您所在公司名称</span>
     </dd>
 </dl>
 
 <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">所在地区：</div>
         <div class="shurukuang fr t-a-l">
           <select  id="province"  onchange="javascript:clearOption(this.form.city);showCity(this.options[this.options.selectedIndex].value,this.form.city);checkCity(1)">
            <option value="0" size="10">--选择--</option>
           </select>
                &nbsp;
            <select id="city" onchange = "javascript:checkCity()" >
              <option value="0" size="10">--选择--</option>
            </select>&nbsp;<font style=" color:#ff0000; font-weight:bold;"></font>
         </div>
     </dt>
     <dd class="fl">
		<span id="cityTip">选择您所在地区，便于提供服务</span>
	 </dd>
 </dl>
  <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">详细地址：</div>
         <div class="shurukuang fr t-a-l">
           <input  id="address"
    			  type="text"                             
                  maxlength="50"  
                  styleclass="12black"
				  value=""
                  onblur = "addressLoseFocus()" />&nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl" >
    	<input type="button" value="地图标注" onclick="search();">
		<span id="addressTip"  calss="">输入或标注公司具体位置，便GM提供更好服务</span>
     </dd>
 </dl>
 
 <div id="havePhone" style="padding-left:120px;width:100%;height:30px;line-height:30px;color:#f00;display:none;">公司电话或手机号码必须至少填写一项</div>
 <dl title="公司电话或手机号码必须至少填写一项" style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">公司电话：</div>
         <div class="shurukuang fr t-a-l">
           <input  id="phone"
				  type="text"
    				  value="公司电话或手机号码必须至少填写一项" 
    				  style="color:#ccc;"
				  size="24"
				  onblur = "javascript:phoneLoseFocus();" />
		 <font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="phoneTip">请输入您的电话，可开通免费电话回呼</span>
     </dd>
 </dl>

<div style="border-bottom:0px dashed #ccc; width:100%; background:none; height:20px;"></div>
 
 <dl style="display:none">
     <dt class="fl">
         <div class="biaoti fl t-a-r">所属行业：</div>
         <div class="shurukuang fr t-a-l">
           <select size="1"  id="hangye" onchange="javascript:hangyeLoseFocus();">
             <option value="0" >--请选择公司的所属行业--</option>
			 
           </select>
         </div>
     </dt>
     <dd class="fl">
		<span id="hangyeTip">选择行业，让更多客户找到您</span>
	 </dd>
 </dl>
 <dl  style="height:70px;display:none">
     <dt class="fl" >
         <div class="biaoti fl t-a-r">关键字：</div>
         <div class="shurukuang fr t-a-l">
		 <textarea style="height:60px;width:248px;" id="keywords" onblur = "keywordsLoseFocus()" maxlength="2000"></textarea>
		 &nbsp;<font style=" color:#ff0000; font-weight:bold;">*</font>
         </div>
     </dt>
     <dd class="fl">
		<span id="keywordsTip">多个用逗号&quot;，&quot;隔开，输入产品名称，服务名，品牌或其他关键词</span>
	 </dd>
 </dl>
 <!--
  <dl>
     <dt class="fl">
         <div class="biaoti fl t-a-r">验证码：</div>
         <div class="shurukuang fr t-a-l">
		   <input type="text" id="vaildateCode" 
				  size="24" value="" onBlur="javascript:validateCode()"/>
		 <font style=" color:#ff0000; font-weight:bold;">*<s:property value="#attr.checkRet"/></font>
         </div>
     </dt>
     <dt class="fl">
			<img align="middle" onclick="getValidateCode();" id="vaildateImg" name="vaildateImg" style="cursor:hand;" title="看不清，点击更换图片。" src="verificationCodeGet?rand="'+Math.random()+'> 
		<span id="vaildateTip" style="color:#f00;height:50px;">
		</span> 
	 </dt>
 </dl>-->
 
 <dl class="t-a-l" style="padding-left:110px;display:none">
 	<input type="checkbox"  checked="checked" value="1" id="ckAgree" />
           已经阅读并同意 <a  href="/vip/rule-1.html" target="_blank">
    <span class="lanse">《xxx用户服务条款》</span></a></p>
 </dl>
 <dl class="t-a-l" style="padding-left:110px; height:33px;">
	<a id="submitButton"  name="submitButton"  href="#" onclick="checkForm();"/><img src="${pageContext.request.contextPath}/images/b_next.gif" /></a>
 </dl>
 </form>             
 </div>
 </div>

<div id="bottomr">
<div class="menu fl" style="display:none">
<ul>
<li><a href="">公司介绍</a></li>
<li><a href="">公司理念</a></li>
<li><a href="">资质荣誉</a></li>
<li><a href="">诚聘英才</a></li>
<li><a href="">新闻动态</a></li>
<li><a href="">付款方式</a></li>
<li><a href="">联系方式</a></li>
<li><a href="">合作伙伴</a></li>
<li><a href="">友情链接</a></li>
</ul>
</div>
<div class="copyright fl">Copyright &copy; 2016 The Tank2 project</div>
</div>

<!-- bottom over-->

</body>
</html>
<script language="javascript"> 

function resetRegForm(){   document.registerForm.strid.value="";}

showprovince(document.registerForm.province);

/* if(document.registerForm.hiddenProvince.value>0){
    clearOption(document.registerForm.city);
    selectedProv(document.registerForm.province,document.registerForm.hiddenProvince.value);
    selectedCity(document.registerForm.city,document.registerForm.hiddenProvince.value,document.registerForm.hiddenCity.value) ;
} */

function addListener(element,e,fn){    
        if(element.addEventListener){    
             element.addEventListener(e,fn,false);    
         } else {    
             element.attachEvent("on" + e,fn);    
          }    
   }
   var phone = document.getElementById("phone");
   var mobile = document.getElementById("mobile");
   
   addListener(phone,"click",function(){
    phone.style.color ="";
	if(phone.value == '公司电话或手机号码必须至少填写一项'){
        phone.value = "";
	}
   })
   
   addListener(mobile,"click",function(){
   	mobile.style.color ="";
	if(mobile.value == '公司电话或手机号码必须至少填写一项'){
        mobile.value = "";
	}
   })

   addListener(phone,"blur",function(){
       if(phone.value==""){
          phone.style.color ="#ccc";
          phone.value = "公司电话或手机号码必须至少填写一项";
       }
   })
   
   addListener(mobile,"blur",function(){
      if(mobile.value==""){
         mobile.style.color ="#ccc";
       	 mobile.value = "公司电话或手机号码必须至少填写一项";
      }
   })
   
   
   function selectMenu(o){
		var selectText ='';
		for(i=0;i<o.options.length;i++){ 
			if(o.options[i].selected){
				var optValue = o.options[i].value;
				if(optValue != 0){
					selectText = o.options[i].text ;
				}
			}
		} 
		return selectText;
	}

    var is_ie6 = false;
	try{
    	var browser=navigator.appName;
        var trim_Version=navigator.appVersion.split(";")[1].replace(/[ ]/g,"");
       	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0"){ 
        	is_ie6 = true;
        }
	}catch(e){}

	var isMt = false;
	function search(){
	  isMt = true;
	  var topAddress = document.getElementById('address').value;
	  var url = "tq_map.html";
	  var isLock = is_ie6?false:true;
	  art.dialog.open(url, {
			lock: isLock,
			width: '800px',
			height: '500px',
			title: "选择定位",
			yesFn: function(iframeWin, topWin){
				iframeWin.setFormInit(iframeWin, topWin,topAddress);
				return false;
			},
			initFn:function(){
				var topProvince = selectMenu(document.getElementById("province")) + selectMenu(document.getElementById("city"));
				var nodes = this.ui.content[0].childNodes[1]; // IE
				if(nodes == undefined)
					nodes = this.ui.content[0].childNodes[0]; // Firefox
				nodes.contentWindow.init(art.dialog.parent,topProvince);
				
			},
			noFn:  function(){}
		});
	}
	
	
    function checkForm(){
    	stridLoseFocus();
    	setTimeout(function(){
    		if(isOk != 1) return;
    		/*if(checkValidateCodeRet != 1) {
        		return;
        	}*/
        	if(passWordLoseFocus()&& surePssWordLoseFocus()) {
        		if(checkStridReturnValue == -1) return false;
        		document.getElementById("submitButton").style.display="none" ; 
        		document.getElementById("submitTip").innerHTML="系统正在处理，请稍后……";
        		document.forms['registerForm'].submit();
	    	}else{
	    		return;
	    	}
    	}, 200);
    }
	
	function submitForm(){
		document.forms['registerForm'].submit();
	}
</script>
