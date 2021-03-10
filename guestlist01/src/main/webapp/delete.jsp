<%@page import="com.bitacademy.guestbook01.dao.GuestbookDao"%>
<%@page import="com.bitacademy.guestbook01.vo.GuestbookVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String no = request.getParameter("no");
String password = request.getParameter("password");
int number = -1 ;
if(no != null && no.matches("\\d*")){
	number = Integer.parseInt(no);
}
GuestbookVo vo = new GuestbookVo();
vo.setNo(number);
vo.setPassword(password);
// 삭제 결과에 따라서 성공시 index.jsp로 이동, 삭제 실패시 deleteform.jsp로 다시 복귀
boolean result = new GuestbookDao().delete(vo);
if(result){
	response.sendRedirect("/guestbook01/index.jsp");	
}else{
	%>
	<script>alert("비밀번호가 일치하지 않습니다.");history.back();</script>
	<%	
}
%>
