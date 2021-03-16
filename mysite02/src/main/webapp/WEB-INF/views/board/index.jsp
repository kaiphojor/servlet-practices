<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<%@ taglib uri="http://enchiridion.tistory.com/jsp/encode" prefix="encode" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.request.contextPath}/board" method="post">
					<c:choose>
						<c:when test="${empty keyword}">
							<input type="text" id="kwd" name="kwd" value="">
						</c:when>
						<c:otherwise>
							<input type="text" id="kwd" name="kwd" value="${keyword}">
						</c:otherwise>
					</c:choose>
					
					
					
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:set var="count" value="${fn:length(list)}"/>
					
					<c:forEach items="${list}" var="vo" varStatus="status">
						<tr>
							<td>${count-status.index+pagingBean.lastIndexOfNextPage}</td>
							<td style="text-align:Left; padding-left:${(vo.depth-1)*40}">
								<a href="${pageContext.request.contextPath}/board?a=view&no=${vo.no}" >
								<c:if test="${vo.depth > 0 }">
									<img src="${pageContext.request.contextPath}/assets/images/reply.png">
								</c:if>
								${vo.title}
								</a>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.views }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="${pageContext.request.contextPath}/board?a=delete&no=${vo.no}" class="del">삭제</a>
								</c:if>
							</td>
 						</tr>	
					</c:forEach>				
				</table>
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
					<c:choose>
						<c:when test="${empty keyword}">
					        <c:if test="${pagingBean.previousPageGroup == true}">
				            <a href="${pageContext.request.contextPath}/board?page=${pagingBean.startPageOfPageGroup -1 }">◀</a>
					        </c:if>
						        <c:forEach var="page" begin="${pagingBean.startPageOfPageGroup}" end="${pagingBean.endPageOfPageGroup}" varStatus="order">
						        	<c:choose>
						        		<c:when test="${order.index == pagingBean.nowPage }">
						        			<li class="selected">
						        		</c:when>
						        		<c:otherwise>
						        			<li>
						        		</c:otherwise>
						        	</c:choose>
						        		<a href="${pageContext.request.contextPath}/board?page=${order.index}">${order.index}</a>
						        	</li>
						        </c:forEach>
					        <c:if test="${pagingBean.nextPageGroup == true}">
				            <a href="${pageContext.request.contextPath}/board?page=${pagingBean.endPageOfPageGroup +1 }">▶</a>
				        </c:if>
						</c:when>
						
						<c:otherwise>
							<c:set var="encodedKeyword" value='${encode:urlEncode(keyword,"utf-8")}'/>
					        <c:if test="${pagingBean.previousPageGroup == true}">
					            <a href="${pageContext.request.contextPath}/board?page=${pagingBean.startPageOfPageGroup -1 }&kwd=${encodedKeyword}">◀</a>
					        </c:if>
					        <c:forEach var="page" begin="${pagingBean.startPageOfPageGroup}" end="${pagingBean.endPageOfPageGroup}" varStatus="order">
					        	<c:choose>
					        		<c:when test="${order.index == pagingBean.nowPage }">
					        			<li class="selected">
					        		</c:when>
					        		<c:otherwise>
					        			<li>
					        		</c:otherwise>
					        	</c:choose>
					        		<a href='${pageContext.request.contextPath}/board?page=${order.index}&kwd=${encodedKeyword}'>${order.index}</a>
					        	</li>
					        </c:forEach>
					        <c:if test="${pagingBean.nextPageGroup == true}">
					            <a href="${pageContext.request.contextPath}/board?page=${pagingBean.endPageOfPageGroup +1 }&kwd=${encodedKeyword}">▶</a>
					        </c:if>
						</c:otherwise>
					</c:choose>
					
					
					

					</ul>
				</div>					
				<!-- pager 추가 -->
				<c:if test="${not empty authUser}">
					<div class="bottom">
						<a href="${pageContext.request.contextPath}/board?a=writeform" id="new-book">글쓰기</a>
					</div>				
				</c:if>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>