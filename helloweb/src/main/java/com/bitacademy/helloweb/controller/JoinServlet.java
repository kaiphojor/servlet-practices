package com.bitacademy.helloweb.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JoinServlet
 */
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String email = request.getParameter("email");
		String pw = request.getParameter("password");
		String birth = request.getParameter("birth-year");
		String gender = request.getParameter("gender");
		String[] hobbies = request.getParameterValues("hobbies");
		String description = request.getParameter("description");
				
		response.setContentType("text/html; charset=utf-8"); 
		System.out.println(email);
		System.out.println(pw);
		System.out.println(Integer.parseInt(birth));
		System.out.println(gender);
		for(String h : hobbies) {
			System.out.println(h);
		}
		System.out.println(description);
		response.getWriter().print("OK");
		// TODO Auto-generated method stub
//		doGet(request, response);
	}

}
