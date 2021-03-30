<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>OPEN API TEST</title>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script type="text/javascript">
	// 입력값 정수인지 판별
	function isNumeric(str) {
	  if (typeof str != "string") return false // we only process strings!  
	  return !isNaN(str) && // use type coercion to parse the _entirety_ of the string (`parseFloat` alone does not do this)...
	         !isNaN(parseFloat(str)) // ...and ensure strings of whitespace fail
	}


	var userURL = "http://localhost:8080/apitest";
	// 공휴일 검색 기능 
	function fnHoliday() {
		var month = document.getElementById("month").value.padStart(2,'0');
		var url = userURL + "/AjaxRequest.jsp?getUrl=";
		var subURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="
				+ document.getElementById("year").value;
		subURL += "&solMonth=" + month;
		subURL += "&ServiceKey=" + document.getElementById("serviceKey").value;
		url += encodeURIComponent(subURL);
		console.log(url);
		$.ajax({
			"url" : url,
			"type" : "GET",
			"success" : function(result) {
				$('#result').innerHTML = result;
				console.log("success : " + result.responseXML);
				console.log(result);
				x = result.getElementsByTagName("resultMsg")[0];
				console.log(x);
				console.log(x.childNodes[0].nodeValue);
				var items = result.getElementsByTagName("item"); 
				var txt = "";
				
				// 테이블 구성
				if (result == null || result == "") {
					alert("해당 주소로 얻을수 있는 좌표가 없습니다. 주소값을 다시 입력하세요");
				} else {
					$.each(items, function(i, value) {
						if (i == 0) {
							txt += "<tr>";
							txt += "<th>날짜</th>";
							txt += "<th>명칭</th>";
							txt += "<th>종류</th>";
							txt += "<th>공공기관 휴일 여부</th>";
							txt += "</tr>";     
						}
						if (result.data == null) {
						}
						txt += "<tr>";
						txt += "<th>"+value.getElementsByTagName("locdate")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("dateName")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("dateKind")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("isHoliday")[0].childNodes[0].nodeValue+"</th>";
						txt += "</tr>";
												
					});
					document.getElementById("result").innerHTML = txt;
				}
			},
			"async" : "true",
			"dataType" : "xml",
			"error" : function(x, o, e) {
				$('#result').innerHTML = x.responseText;
				console.log(x.responseText);
				console.log(x);
				//alert(x.status + ":" + o + ":" + e);
			}
		});
	}
	// 송파구 모범 음식점 목록 기능
	function fnRestaurant() {
		// 입력값 처리 - default 10개
		var num = document.getElementById("count").value;
		var limit = 10;
		if(isNumeric(num)){
			limit = parseInt(num,10);				
		}
		var month = document.getElementById("month").value.padStart(2,'0');
		var url = userURL + "/AjaxRequest.jsp?getUrl=";
		var subURL = "http://openAPI.songpa.seoul.kr:8088";
		subURL += "/71456d5164666f753539587572624f";
		subURL += "/xml";
		subURL += "/SpModelRestaurantDesignate";
		subURL += "/1";
		subURL += "/"+ limit + "/";
		url += encodeURIComponent(subURL);
		console.log(url);
		$.ajax({
			"url" : url,
			"type" : "GET",
			"success" : function(result) {
				$('#result').innerHTML = result;
				x = result.getElementsByTagName("SpModelRestaurantDesignate")[0];
				document.getElementById('totalCount').innerHTML = x.getElementsByTagName("list_total_count")[0].childNodes[0].nodeValue;
				var rows = result.getElementsByTagName("row"); 
				var txt = "";
				if (result == null || result == "") {
					alert("해당 주소로 얻을수 있는 좌표가 없습니다. 주소값을 다시 입력하세요");
				} else {
					// 테이블 구성 
					$.each(rows, function(i, value) {
						if (i == 0) {
							// 헤더 구성 
							txt += "<tr>";
							txt += "<th>시군구코드</th>";
							txt += "<th>지정년도</th>";
							txt += "<th>지정번호</th>";
							txt += "<th>신청일자</th>";
							txt += "<th>지정일자</th>";
							txt += "<th>업소명</th>";
							txt += "<th>소재지도로명</th>";
							txt += "<th>소재지지번</th>";
							txt += "<th>허가(신고)번호</th>";
							txt += "<th>업태명</th>";
							txt += "<th>주된음식</th>";
							txt += "<th>영업장면적(㎡)</th>";
							txt += "<th>행정동명</th>";
							txt += "<th>급수시설구분</th>";
							txt += "<th>소재지전화번호</th>";
							txt += "</tr>";   
						}
						// 테이블 내용 구성 
						txt += "<tr>";
						txt += "<th>"+value.getElementsByTagName("CGG_CODE")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("ASGN_YY")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("ASGN_SNO")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("APPL_YMD")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("ASGN_YMD")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("UPSO_NM")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("SITE_ADDR_RD")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("SITE_ADDR")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("PERM_NT_NO")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("SNT_UPTAE_NM")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("MAIN_EDF")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("TRDP_AREA")[0].childNodes[0].nodeValue+"</th>";
						txt += "<th>"+value.getElementsByTagName("ADMDNG_NM")[0].childNodes[0].nodeValue+"</th>";
												
						// 값이 존재하지 않을 때 예외 처리
						var isFacility = value.getElementsByTagName("GRADE_FACIL_GBN")[0].childNodes[0];
						//console.log(isFacility);
						var isFacilityVal = typeof isFacility != "undefined" ? isFacility.nodeValue : "없음";						
						txt += "<th>"+isFacilityVal+"</th>";
						var isTelNo = value.getElementsByTagName("UPSO_SITE_TELNO")[0].childNodes[0];
						var isTelNoVal = typeof isTelNo != "undefined" ? isTelNo.nodeValue : "미기재";						
						txt += "<th>"+isTelNoVal+"</th>";						
						txt += "</tr>";
					});
					document.getElementById("restaurantResult").innerHTML = txt;
				}
			},
			"async" : "true",
			"dataType" : "xml",
			"error" : function(x, o, e) {
				$('#result').innerHTML = x.responseText;
				console.log(x.responseText);
				console.log(x);
			}
		});
	}
</script>
</head>
<body>	
	<h1>(공공데이터포털)년,월을 입력해서 공휴일을 확인</h1>
	<input type="hidden" id="serviceKey"
		value="eYB0JZN3NsY9SlT3WN3qNZm0%2BCYERztmFWTaz4pBpiMgvYx2qcKHA8o3Xbx7d7lDTdj5J2XA4sEMLVG%2BW8Ih%2Bg%3D%3D" />
	<br /> 년 :
	<input type="text" id="year" value="" placeholder="연도 입력(예:2019)"/>
	<br /> 월 :
	<input type="text" id="month" value="" placeholder="월 입력(예:3)"/>
	<br />
	<input type="button" value="공휴일 검색" onclick="fnHoliday()" />
	<table id='result'></table>
	<h1>(서울열린데이터광장API)송파구 모범음식점 목록</h1>
	<br /> 출력할 음식점 개수 :
	<input type="text" id="count" value="" placeholder="음식점 개수 입력(예:3)"/>
	<br />
	<input type="button" value="음식점 목록 가져오기" onclick="fnRestaurant()" />
	<br/>
	<span>송파구 내 총 음식점<h3 id='totalCount'></h3></span>
	<br/>
	<table id='restaurantResult'></table>
	<br />
</body>
</html>
