<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="footer">
	<p>(c)opyright 2015, 2016, 2017, 2018</p>
	<ol id='browserHistory'>
	</ol>
</div>
<script>
// 저장
	window.onbeforeunload = function(event) {
		var url = window.location.href;
		var browserHistory = JSON.parse(localStorage.getItem('history'));
		if (browserHistory == undefined) {
			browserHistory = [];
		}
		browserHistory.push(url);
		localStorage.setItem('history', JSON.stringify(browserHistory));
	}
	//가져올 때 
	hist = localStorage.getItem('history');
	var historyList;
	if(hist != null){
		historyList = JSON.parse(hist);
	}else{
		historyList = [];
	}
	console.log(hist);
	console.log(typeof historyList);
	console.log(historyList === null); // false
	console.log(historyList === undefined); // false
	if (typeof (Storage) !== "undefined" && typeof historyList !== "undefined") {
		var ol = document.getElementById('browserHistory');
		for (var i = 0; i < historyList.length; i++) {			
			var li = document.createElement("li");
			var liText = document.createTextNode(historyList[i]);
			li.appendChild(liText);
			ol.appendChild(li);
		}
	}
</script>