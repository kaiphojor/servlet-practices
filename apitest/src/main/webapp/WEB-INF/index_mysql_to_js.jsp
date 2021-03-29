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

	<h2>JSON from MYSQL DB</h2>
	<input id='num' placeholder="조회할 숫자를 입력하세요"></input>
	<button type="button" onclick="getName()">get name!!!</button>
	

	<table id="demo"></table>

	<script>
		function isNumeric(str) {
		  if (typeof str != "string") return false // we only process strings!  
		  return !isNaN(str) && // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
		         !isNaN(parseFloat(str)) // ...and ensure strings of whitespace fail
		}
	
		function getName(){
			var num = document.getElementById("num").value;
			//console.log(num);
			var parsed = parseInt(num,10);
			//console.log(parsed);
			var limit = 10;
			if(isNumeric(num)){
				limit = parseInt(num,10);				
			}
			console.log("param : " + limit);
			
			var xhttp, xmlDoc, txt, x, i;
			xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					var jsonList = JSON.parse(this.responseText);
					var txt="";
				    for (i = 0; i < jsonList.length; i++) {
				    	if(i ==0){
				        	txt = txt + "<tr>";
				            txt = txt + "<td>" + "first_name" + "</td>";
				            txt = txt + "<td>" + "last_name" + "</td>";
				        	txt = txt + "</tr>";    	
				        }
				    	txt = txt + "<tr>";
				    	txt = txt + "<td>" + jsonList[i]['first_name'] + "</td>";
				        txt = txt + "<td>" + jsonList[i]['last_name'] + "</td>";
				      	txt = txt + "</tr>";
				    }
			      	document.getElementById("demo").innerHTML = txt;
				}
			};		
			xhttp.open("GET","http://localhost:8080/apitest/get_name.jsp?num=" + limit,true);
			//xhttp.setRequestHeader("Access-Control-Allow-Origin", "*");
			//xhttp.setRequestHeader("Content-type", "application/json");
			xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			//xhttp.setRequestHeader("Access-Control-Request-Methods","GET")
			//xhttp.setRequestHeader("Access-Control-Allow-Headers",'*')
			//xhttp.setRequestHeader("Access-Control-Max-Age",400000);
			//xhttp.setRequestHeader("Content-type", "application/xml");
			xhttp.send();
		}
	
	</script>

</body>
</html>
