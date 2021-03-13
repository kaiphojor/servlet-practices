package com.bitacademy.mysite.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.web.mvc.WebUtil;

/**
 * Servlet implementation class BoardServlet
 */
@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");
		if("writeform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/board/write.jsp", request, response);				
		}else if("modifyform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/board/modify.jsp", request, response);			
		}else if("view".equals(action)) {
			
		}else {
			WebUtil.forward("/WEB-INF/views/board/index.jsp", request, response);			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
