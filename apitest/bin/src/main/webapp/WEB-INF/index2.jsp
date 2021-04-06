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
						"http://openAPI.songpa.seoul.kr:8088/71456d5164666f753539587572624f/xml/SpModelRestaurantDesignate/1/5/",
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


</body>
</html>
