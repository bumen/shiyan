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
		{field:'id', title:'成员ID'},
		{field:'name',title:'名称'},
		{field:'level',title:'等级'},
		{field:'exp',title:'经验'},
		{field:'forgeLvl',title:'深造'},
		{field:'starLvl',title:'星级'},
		{field:'medal',title:'勋章'}
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
			//c.width = 100;
			c.align = "center";
		//}
			columns.push(c);
	});
	return [columns];
}

function queryBtnClick() {
	var roleId = $('#dataGrid_roleId').textbox('getValue');
	if(roleId == "" || Number(roleId) <= 0)
		return;
	
	$.ajax({
		url:"member/query",
		type:"post",
		data: {
			'roleId':roleId
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
						<input id="dataGrid_roleId" class="easyui-numberbox" data-options="prompt:'请输入角色ID'" style="width: 150px;float:right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" data-options="plain:true, onClick:queryBtnClick">查询</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>

</html>