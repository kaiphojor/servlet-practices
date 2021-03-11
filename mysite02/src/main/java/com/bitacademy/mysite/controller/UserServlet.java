package com.bitacademy.mysite.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.mysite.dao.UserDao;
import com.bitacademy.mysite.vo.UserVo;
import com.bitacademy.web.mvc.WebUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		
		if("joinform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/user/joinform.jsp", request, response);
		}else if("loginform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
		}else{
			WebUtil.redirect(request.getContextPath(), request, response);			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		if("login".equals(action)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setPassword(password);
			
			response.setContentType("text/html; charset=UTF-8");
			if(new UserDao().select(userVo)) {
//				PrintWriter writer = response.getWriter();
//				writer.println("<script>alert('로그인 성공');</script>");
//				writer.close();
				WebUtil.redirect(request.getContextPath(), request, response);
			}else {
				PrintWriter writer = response.getWriter();
				writer.println("<script>alert('로그인 실패');history.back();</script>");
				writer.close();
			}
		}else if("join".equals(action)) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo();
			userVo.setName(name);
			userVo.setEmail(email);
			userVo.setPassword(password);
			userVo.setGender(gender);
			
			new UserDao().insert(userVo);
			WebUtil.redirect(request.getContextPath() + "/user?a=joinsuccess", request, response);
		}
	}

}
