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
	// 전체 클릭 이벤트 
	var aTags = document.getElementsByTagName('A');
	var tagClickEvent = function(event) {		
		//event.preventDefault();
		event.stopPropagation();
		event.stopImmediatePropagation();
		console.log();
				
		// tag 이름 저장
		var elem = event.target;		
		var clickString = "";
		console.log(elem.tagName);		
		var tag = elem.tagName; 
		clickString += tag + " clicked ";
		
		// A tag 따로 처리 
		if (elem.tagName == 'A') {
			//clickString += elem.href + " ";
			clickString += elem.innerHTML.trim() + " ";			
			console.log(elem.innerHTML.trim());
		}

		// local storage에 저장
		var clickHistory = JSON.parse(localStorage.getItem('clickHistory'));
		if (clickHistory == undefined) {
			clickHistory = [];
		}
		clickHistory.push(clickString);
		localStorage.setItem('clickHistory', JSON.stringify(clickHistory));
		
	}
	for (var i = 0; i < aTags.length; i++) {
		aTags[i].onclick = tagClickEvent;
	}
</script>