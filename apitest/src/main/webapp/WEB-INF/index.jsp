<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>openAPI 통계청:공간정보서비스과</title>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script type="text/javascript">
	var userURL = "http://localhost:8080/apitest";
	function fncGeoCode() {
		var month = document.getElementById("month").value.padStart(2,'0');
		var url = userURL + "/AjaxRequest.jsp?getUrl=";
		var subURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="
				+ document.getElementById("year").value;
		subURL += "&solMonth=" + month;
		subURL += "&ServiceKey=" + document.getElementById("serviceKey").value;
		url += encodeURIComponent(subURL);
		//url += subURL;
		console.log(url);
		$.ajax({
			"url" : url,
			"type" : "GET",
			"success" : function(result) {
				if (result == null || result == "") {
					alert("해당 주소로 얻을수 있는 좌표가 없습니다. 주소값을 다시 입력하세요");
				} else {
					$.each(result, function(i, value) {
						if (result.data == null) {
							if (i == 0) {
							}
						}
						$('#document').innerHTML = value;
						$('#result').innerHTML = result;
					});
				}
			}, 
			"data": "",
			"async" : "true",
			"dataType" : "xml",
			"error" : function(x, o, e) {
				alert(x.status + ":" + o + ":" + e);
			}
		});
	}
	//serviceKey :
</script>
</head>
<body>	
	<input type="hidden" id="serviceKey"
		value="eYB0JZN3NsY9SlT3WN3qNZm0%2BCYERztmFWTaz4pBpiMgvYx2qcKHA8o3Xbx7d7lDTdj5J2XA4sEMLVG%2BW8Ih%2Bg%3D%3D" />
	<br /> 년 :
	<input type="text" id="year" value="2019" />
	<br /> 월 :
	<input type="text" id="month" value="03" />
	<br />
	<input type="button" value="GeoCode Service" onclick="fncGeoCode()" />
	<p id='result'></p>
	<p id='document'></p>
	<br />
</body>
</html>
