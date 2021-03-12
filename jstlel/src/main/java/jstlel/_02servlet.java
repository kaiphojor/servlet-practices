package jstlel;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class _02servlet
 */
@WebServlet("/02")
public class _02servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
 * 1. 객체가 오래 존속되는 순서 
 * Application(Context) Scope > Session Scope > Request Scope > Page Scope
 * 
 * 2. EL에서 이름을 찾는 순서
 * Application(Context) Scope < Session Scope < Request Scope < Page Scope
 * 
 * 주의 : 같은 일므으로 여러 범위에 객체를 저장하지 말 것!
 */
		// requst scope 
		UserVo vo1 = new UserVo();
		vo1.setNo(1L);
		vo1.setName("나다");
		request.setAttribute("vo", vo1);
		//session scope
		UserVo vo2 = new UserVo();
		vo2.setNo(22L);
		vo2.setName("나다2");
		request.getSession(true).setAttribute("vo", vo2);
		
		request.getRequestDispatcher("/WEB-INF/views/02.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
