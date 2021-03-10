<%@page import="com.bitacademy.guestbook01.dao.GuestbookDao"%>
<%@page import="com.bitacademy.guestbook01.vo.GuestbookVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");

String name = request.getParameter("name");
String contents = request.getParameter("contents");
String password = request.getParameter("password");

GuestbookVo vo = new GuestbookVo();
vo.setName(name);
vo.setPassword(password);
vo.setContents(contents);

new GuestbookDao().insert(vo);
response.sendRedirect("/guestbook01/index.jsp");
%>
