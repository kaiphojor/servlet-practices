<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="footer">
	<p>(c)opyright 2015, 2016, 2017, 2018</p>
	
</div>
<script>
//저장
window.onbeforeunload = function(event) {
	var url = window.location.href;
	var browserHistory = JSON.parse(localStorage.getItem('history'));
	if (browserHistory == undefined) {
		browserHistory = [];
	}
	browserHistory.push(url);
	localStorage.setItem('history', JSON.stringify(browserHistory));
}

document.getElement
</script>