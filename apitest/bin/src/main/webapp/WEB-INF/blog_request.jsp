<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%
%>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

	<script>
		//var url = 'https://rss.blog.naver.com/yohanlee0602.xml';
		var url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="
			+ "2019";
	url += "&solMonth=" + "12";
	url += "&ServiceKey="
			+ "eYB0JZN3NsY9SlT3WN3qNZm0%2BCYERztmFWTaz4pBpiMgvYx2qcKHA8o3Xbx7d7lDTdj5J2XA4sEMLVG%2BW8Ih%2Bg%3D%3D";
	    //url = "https://api.rss2json.com/v1/api.json?rss_url=" + url;  
		$.ajax({
			type : 'GET',
			url : url,
			dataType : 'jsonp',
			jsonp : "callback",
			success : function(data) {
				console.log(data.feed.description);
				console.log(data.responseXML);
				console.log(data);
			},
			error : function(xhr){
		        console.log('실패 - '+xhr);
		        console.log(xhr.responseXML);
		        
		    }
		});
	</script>
</body>
</html>