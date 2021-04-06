<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>test handle response</title>
<script type="text/javascript"
		src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

	//build the yql query. Could be just a string - I think join makes easier reading
	var url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="
			+ "2019";
	url += "&solMonth=" + "12";
	url += "&ServiceKey="
			+ "eYB0JZN3NsY9SlT3WN3qNZm0%2BCYERztmFWTaz4pBpiMgvYx2qcKHA8o3Xbx7d7lDTdj5J2XA4sEMLVG%2BW8Ih%2Bg%3D%3D"
	

	//Now do the AJAX heavy lifting        
	$.getJSON(url, function(data) {
		xmlContent = $(data.results[0]);
		var Abstract = $(xmlContent).find("Abstract").text();
		console.log(xmlContent.text());
		console.log(Abstract);
	});

	//xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	jsonp(url, function(data) {
		//alert(data);
		document.findElementById("res").innerHTML = data;
	});
	//xmlhttp.open("GET", url);
	//xmlhttp.send();
</script>
</head>
<body>
	<script>
		
	</script>
	<p id="res"></p>
</body>
</html>