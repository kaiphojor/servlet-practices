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
		xhttp.open("GET","http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear=2019&solMonth=03&ServiceKey=yeOtQ6F6iagsaWvarW55K0aPRFQgYqFv+V4OttlA8Qv9eXtsyDwZsTYJBi+sDhLXLvK3MMG864XwlZHfIZRHwQ==",true);
		//xhttp.setRequestHeader("Access-Control-Allow-Origin", "*");
		//xhttp.setRequestHeader("Content-type", "application/xml");
		//xhttp.setRequestHeader("Access-Control-Request-Methods","GET")
		//xhttp.setRequestHeader("Access-Control-Allow-Headers",'*')
		//xhttp.setRequestHeader("Access-Control-Max-Age",400000);
		//xhttp.setRequestHeader("Content-type", "application/xml");
		xhttp.send();
	</script>

</body>
</html>
