

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.bitacademy.web.mvc.WebUtil;

import dao.EmployeesDao;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		WebUtil.forward("/WEB-INF/index.jsp", request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		request.setCharacterEncoding("utf-8");
		String numString = request.getParameter("num");
		int num = 10;
		if(numString != null) {
			num = Integer.parseInt(numString);
		}
		JSONArray employeeNameList = new EmployeesDao().getEmployeeNameList(num);
	
		request.setAttribute("nameList", employeeNameList);
	}
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		System.out.println("OPTIONS");
		response.setContentType("text/html");
	}

}
