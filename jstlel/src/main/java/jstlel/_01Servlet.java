package jstlel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class _01Servlet
 */
@WebServlet("/01")
public class _01Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 값
		int iVal = 10;
		long lVal = 10L;
		float fVal = 3.14f;
		boolean bVal = true;
		String sVal = "가나다라마바사";
		
		// 객체 테스트 
		UserVo userVo = new UserVo();
		userVo.setNo(10L);
		userVo.setName(sVal);
		
		Object obj = null;
		
		// map 사용 일괄 전송
		Map<String,Object> map = new HashMap<>();
		map.put("ival", iVal);
		map.put("lVal", lVal);
		map.put("fVal", fVal);
		map.put("bVal", bVal);
		map.put("sVal", sVal);
		
		request.setAttribute("map",  map);
		// autoboxing 
		request.setAttribute("iVal",iVal );
		request.setAttribute("lVal",lVal );
		request.setAttribute("fVal",fVal );
		request.setAttribute("bVal",bVal );
		request.setAttribute("sVal",sVal );
		
		request.setAttribute("vo",userVo );
		
		request.setAttribute("obj",obj );
		
		request.getRequestDispatcher("/WEB-INF/views/01.jsp").forward(request, response);
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
