package com.bitacademy.guestbook.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.guestbook02.dao.GuestbookDao;
import com.bitacademy.guestbook02.vo.GuestbookVo;
import com.bitacademy.web.mvc.WebUtil;

/**
 * Servlet implementation class GuestbookServlet
 */
public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		// 방명록 삭제 기능, 처리 후 메인페이지로 redirect
		if("delete".equals(action)) {
			String no = request.getParameter("no");
			int number = -1 ;
			if(no != null && no.matches("\\d*")){
				number = Integer.parseInt(no);
			}
			String password = request.getParameter("password");
			GuestbookVo vo = new GuestbookVo();
			vo.setNo(number);
			vo.setPassword(password);
			// 삭제 결과에 따라서 성공시 index.jsp로 이동, 삭제 실패시 deleteform.jsp로 다시 복귀
			boolean result = new GuestbookDao().delete(vo);
			if(result){
				WebUtil.redirect(request.getContextPath()+"/gb", request, response);
			}else{
				response.setContentType("text/html; charset=UTF-8"); 
				PrintWriter writer = response.getWriter(); 
				writer.println("<script>alert('비밀번호가 일치하지 않습니다.');history.back();</script>"); 
				writer.close();
			}
		// 방명록 삭제 화면
		}else if("deleteform".equals(action)) {
			String no = request.getParameter("no");
			int number = -1 ;
			if(no != null && no.matches("\\d*")){
				number = Integer.parseInt(no);
			}
			request.setAttribute("no",number);
			WebUtil.forward("/WEB-INF/views/deleteform.jsp",request,response);
		}else {
			List<GuestbookVo> list = new GuestbookDao().selectAll();
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/index.jsp",request,response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		// 방명록 추가 기능, 처리후 메인 페이지로 redirect
		if("add".equals(action)) {
			String name = request.getParameter("name");
			String contents = request.getParameter("contents");
			String password = request.getParameter("password");

			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(contents);

			new GuestbookDao().insert(vo);			
			WebUtil.redirect(request.getContextPath()+"/gb", request, response);
		}else {
			List<GuestbookVo> list = new GuestbookDao().selectAll();
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/index.jsp",request,response);
		}
//		doGet(request, response);
	}

}
