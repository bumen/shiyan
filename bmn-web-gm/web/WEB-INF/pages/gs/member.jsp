<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
	<script src="${pageContext.request.contextPath}/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/js/member.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>

	服务器列表：
	<select id="areaList">
		<c:forEach var="area" items="${areaList}">
			<option value="${area.id}">${area.text}</option>
		</c:forEach>
	</select>
	<select id="accountType">
		<option value="1" selected>账号</option>
		<option value="2">角色</option>
		<option value="3">角色Id</option>
	</select>
	<input id="accountName" type="text" />
	<input id="queryBtn" type="button" onclick="queryMember()" value="查询"/>

	<table id="memberTable" border="1">
		<tr>
			<td id="title_instanceId">实例ID</td>
			<td id="title_templateId">模板ID</td>
			<td id="title_level">等级</td>
			<td id="title_exp">经验</td>
			<td id="title_aptitude">资质</td>
			<td id="title_medal">勋章列表</td>
	</table>
	<p id="tipsInfo" style="color: red"></p>
</body>
</html>