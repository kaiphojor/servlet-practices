<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

tr:nth-child(even) {
	background-color: #dddddd;
}
</style>
</head>


<body>

	<h2>The XMLHttpRequest Object</h2>
	<div class="result"></div>


	<p id="demo"></p>

	<script>
		var xhttp, xmlDoc, txt, x, i;
		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				xmlDoc = this.responseXML;
				txt = xmlDoc;
				document.getElementById("demo").innerHTML = txt;
			}
		};
		xhttp
				.open(
						"GET",
						"http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear=2019&solMonth=03&ServiceKey=yeOtQ6F6iagsaWvarW55K0aPRFQgYqFv%2BV4OttlA8Qv9eXtsyDwZsTYJBi%2BsDhLXLvK3MMG864XwlZHfIZRHwQ%3D%3D",
						true);
		//xhttp.setRequestHeader("Access-Control-Allow-Origin", "*");
		xhttp.setRequestHeader("Content-type", "application/xml");
		//xhttp.setRequestHeader("Access-Control-Request-Methods","GET")
		//xhttp.setRequestHeader("Access-Control-Allow-Headers",'*')
		//xhttp.setRequestHeader("Access-Control-Max-Age",400000);
		//xhttp.setRequestHeader("Content-type", "application/xml");
		xhttp.send();
	</script>
	<script type="text/javascript"
		src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

	<script>
		$(document).ready(function() {
			$.ajax({
				type : 'GET',
				dataType : 'jsonp',
				data : {
					'name' : 'kimyeonsuk'
				},
				url : 'http://www.stoneis.pe.kr/jsonp/jsonpResult.jsp',
				// jsonp 값을 전달할 때 사용되는 파라미터 변수명
				// 이 속성을 생략하면 callback 파라미터 변수명으로 전달된다.
				jsonp : 'stone',
				success : function(json) {
					$('.result').html(json.data.name);
				}
			});
		});
	</script>
</body>
</html>
