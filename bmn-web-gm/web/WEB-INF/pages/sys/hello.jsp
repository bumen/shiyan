<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/jquery/demo/demo.css">
<script type="text/javascript" src="/jquery/jquery.min.js"></script>
<script type="text/javascript" src="/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/jquery/plugins/jquery.tabs.js"></script>
<script src="/js/common.js" type="text/javascript" charset="utf-8"></script>
<style type="text/css">
	.mycss {
		display:-moz-inline-box;
		display:inline-block;
		width:35px;
		padding-top:3px;
		height:20px;  
		filter:none;
		color:#FFF;
	}
	.mycss:hover{
		background-color: #3F6CA4;
		font-color:#FFF;
		cursor: pointer;
	}
</style>
<script language="javascript">
$(function(){  
	var permitMenuStr = '${requestScope.permitMenu}';
	if(permitMenuStr) {
		var permitMenu = JSON.parse(permitMenuStr);
		for(var i in permitMenu) {
			var accordionInfo = permitMenu[i];
			if(accordionInfo.id) {
				var accordionId = createAccordion(accordionInfo);
				
				JSNode(accordionId).tree({
					data: accordionInfo.tree,
					onClick: function(node){leafOnClick(accordionId, node); }
				});
			}
		}
	}
});

function createAccordion(accordionInfo) {
	var accordionId = "accordion_user_" + accordionInfo.id;
		
	var accordion = {
		'title' : accordionInfo.title,
		'iconCls' : 'icon-ok',
		'selected':accordionInfo.selected,
		'content': '<ul id="'+ accordionId +'" class="easyui-tree" data-options="animate:true,lines:true" > </ul>'
	};
	
	$('#ac').accordion('add', accordion);
	
	return accordionId;
}

function JSNode(id) {
	return $('#' + id);
}

function leafOnClick(tid, node) {
	var isLeaf = JSNode(tid).tree('isLeaf', node.target);
	if(isLeaf) {
		var tabTitle = node.text;
		var findTab = JSNode("job_tabs").tabs("exists", tabTitle);
		if(findTab) {
			JSNode("job_tabs").tabs("select", tabTitle);
		} else {
			var url = node.attributes.url;
			JSNode("job_tabs").tabs('add',{
				title: tabTitle,
				//content: '<div style="padding:10px">Content'+node.text+'</div>',
				//href:'test1',
				content:'<iframe style="width:100%;height:100%"  src="'+url+'" frameborder="0"  scrolling="yes"></iframe>',
				closable: true
			});
		}
	}
}

function logout() {
	window.location.href="logout";
}

</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:60px;background:#244977;padding:10px">
		<div style="text-align:center">
			<font style="font-size:20px;color:#FC0">晓博圆</font>
			<span  style="float:right;display:inline-block;padding-top:3px">
				<span style="color:#FFF;" >
					<img src="${pageContext.request.contextPath}/images/man.png" style="height:18px;position:relative;top:4px;"/>
					${sessionScope._USER_.uin }
				</span>
				<span class="mycss" onclick="logout()">退出</span>
			</span>
		</div>
		
	</div>
	<div data-options="region:'west',split:true,title:'资源管理器'" style="width:200px;">
		<div id="ac" class="easyui-accordion"  data-options="" fit="true"> </div>
	</div>
	<!-- 中间 -->
	<div data-options="region:'center',title:'工作区'" style="">
		<div id="job_tabs" class="easyui-tabs" fit="true">
		</div>
	</div>
	
	
	<div data-options="region:'south',border:false" style="overflow:hidden;height:30px;background:#E1E1E1;padding:8px;">
		<div style="text-align:center;">
			Copyright &copy; 2016 The Tank2 project
		</div>
	</div>
</body>
</html>