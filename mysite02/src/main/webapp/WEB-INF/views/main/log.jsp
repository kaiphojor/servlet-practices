<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath}/assets/css/main.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content">
				<h2>접근 주소 목록</h2>
				<div>
					<ol id='browserHistory'>
					</ol>
				</div>
				<h2>클릭한 내용</h2>
				<div>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
	<script>
		//가져올 때 
		hist = localStorage.getItem('history');
		var historyList;
		if (hist != null) {
			historyList = JSON.parse(hist);
		} else {
			historyList = [];
		}
		console.log(hist);
		console.log(typeof historyList);
		console.log(historyList === null); // false
		console.log(historyList === undefined); // false
		if (typeof (Storage) !== "undefined"
				&& typeof historyList !== "undefined") {
			var ol = document.getElementById('browserHistory');
			for (var i = 0; i < historyList.length; i++) {
				var li = document.createElement("li");
				var liText = document.createTextNode(historyList[i]);
				li.appendChild(liText);
				ol.appendChild(li);
			}
		}
	</script>
</body>
</html>