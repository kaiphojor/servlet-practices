package com.bitacademy.mysite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.mysite.dao.BoardDao;
import com.bitacademy.mysite.vo.BoardVo;
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
			Long no = Long.parseLong(request.getParameter("no"));
			BoardVo vo = new BoardDao().viewBoard(no);
			request.setAttribute("vo", vo);
			WebUtil.forward("/WEB-INF/views/board/view.jsp", request, response);	
			
		}else if("delete".equals(action)) {			
			WebUtil.redirect(request.getContextPath() + "/board", request, response);			
		}else {
			List<BoardVo> list = new BoardDao().selectAll();
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/board/index.jsp", request, response);			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
