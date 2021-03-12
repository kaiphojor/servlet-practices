package com.bitacademy.mysite.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		}else if("logout".equals(action)) {
			HttpSession session = request.getSession();
			if(session == null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			// 로그 아웃 처리
			if(session != null && session.getAttribute("authUser")!= null) {
				session.removeAttribute("authUser");
				session.invalidate();
			}
			WebUtil.redirect(request.getContextPath(), request, response);			
		}else{
			WebUtil.redirect(request.getContextPath(), request, response);			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		if("login".equals(action)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserVo userVo = new UserVo();
			userVo.setEmail(email);
			userVo.setPassword(password);
			
			UserVo authUser = new UserDao().findByEmailAndPassword(userVo); 
			System.out.println(authUser);
			if(authUser == null) {
				request.setAttribute("authResult", "fail");
				WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
				return;
			}
			// 인증 처리 
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", authUser);
			
			// 응답
			WebUtil.redirect(request.getContextPath(), request, response);
			
//			response.setContentType("text/html; charset=UTF-8");
//			if(new UserDao().select(userVo)) {
////				PrintWriter writer = response.getWriter();
////				writer.println("<script>alert('로그인 성공');</script>");
////				writer.close();
//				WebUtil.redirect(request.getContextPath(), request, response);
//			}else {
//				PrintWriter writer = response.getWriter();
//				writer.println("<script>alert('로그인 실패');history.back();</script>");
//				writer.close();
//			}
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