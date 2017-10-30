<html>
<head>
	<title>student</title>
</head>
<body>
	student message:<br>
	${dateing?date}<br>
	${dateing?time}<br>
	${dateing?datetime}<br>
	${dateing?string("yyyyyMM/dd hhh-mmm-s")}
	<#if list??>
		hehe is null
	<#else>
		hehe is not null
	</#if>
	null is :${val!"val is null"}
	<br>
	<#list list as item>
		<#if item_index%2==0>
			2!<br>
		<#else>
			1!<br>
		</#if>
		下标 is ${item_index}|&nbsp;&nbsp;&nbsp;${item.id}:${item.name}<br>
	</#list>
	
	<#include "hello.ftl">
</body>
</html>