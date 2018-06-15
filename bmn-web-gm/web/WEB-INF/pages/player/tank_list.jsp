<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/demo/demo.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.json.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/jquery.tabs.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>
<style type="text/css">
	.myhovr:hover {
		cursor:pointer;
	}
</style>
<script type="text/javascript">
$(function(){
	var columnHref = [
		{field:'tankId',title:'坦克ID'},
		{field:'tankName',title:'名称'}
	 ];
	 
	 $('#treeGrid').datagrid({
			rownumbers:true,
			fit:true,
			remoteSort:false,
			singleSelect: true,
			fitColumns:true,
			method:'get',
			toolbar:'#dataGrid_tb',
			columns:getDataGridColumns(columnHref),
			pagination:false
		});
		
	 
});

function getDataGridColumns(hrefArray) {
	var columns = [];
	$(hrefArray).each(function(index){
		var c = hrefArray[index];
		//if(c.field != "name") {
			c.width = 100;
			c.align = "center";
		//}
			columns.push(c);
	});
	return [columns];
}

function queryIdBtnClick() {
	queryBtnClick("tankList/queryId");
}

function queryNameBtnClick() {
	queryBtnClick("tankList/queryName");
}

function queryBtnClick(url) {
	var queryKey = $('#dataGrid_queryKey').textbox('getValue');
	if(queryKey == "")
		return;
		
	$.ajax({
		url: url,
		type:"post",
		data: {
			'queryKey':queryKey
		},
		success:function(retData){
			var result = JSON.parse(retData);
			if(typeof(result) == 'object') {
				var data = result.data;
				if(result.success) {
					$('#treeGrid').datagrid('loadData', data);
					divShowTip("查询成功！");
					return;
				}
				
				divShowTip(data);
				return;
			} 
			
			divShowTip("数据提交失败！");
		},error:function() {
			loadingTipClose();
			divShowTip("数据提交失败！");
		}
	});
	
}
</script>
</head>


<body>
	<table id="treeGrid" class="easyui-datagrid" ></table>
	<div id="dataGrid_tb" style="padding: 5px; height: auto">
		<div style="margin-top: 5px">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td style="padding-left:21px;">
						<input id="dataGrid_queryKey" class="easyui-textbox" data-options="prompt:'请输入角色名称或ID'" style="height:28px;width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true, onClick:queryIdBtnClick">ID查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true, onClick:queryNameBtnClick">名称查询</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>