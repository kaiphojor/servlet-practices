<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String no = request.getParameter("no");
	int number = -1 ;
	if(no != null && no.matches("\\d*")){
		number = Integer.parseInt(no);
	}
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%
	for(int i =0; i<10 ; i++){
		
	%>
	<h1>hello jsp world</h1>
	<%
	}
	%>
	<a href="/helloweb/tag.jsp" target='_blank'>tag로 가즈아</a>
	<p>
		<%=number %>
	</p>
</body>
</html>