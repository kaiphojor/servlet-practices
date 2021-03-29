<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>

<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>

<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%

Connection conn = null;
try {
	Class.forName("com.mysql.cj.jdbc.Driver");
	String url = "jdbc:mysql://localhost:3307/employees?characterEncoding=utf8&serverTimezone=UTC";
	conn = DriverManager.getConnection(url,"root","bit");
} catch (ClassNotFoundException e) {
	System.out.println("error " + e);
}		
JSONArray list = new JSONArray();
JSONObject item = null; 


PreparedStatement pstmt = null;
String sql = null;	
String param = request.getParameter("num");
int limit = param != null ? Integer.parseInt(param) : 10;

try {		
				
	sql =  "select  first_name, last_name"
			+ "	from employees "
			+ "  limit ?;";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1, limit);
	
	ResultSet rs = pstmt.executeQuery();
	while(rs.next()) {
		item = new JSONObject();
		item.put("first_name", rs.getString(1));
		item.put("last_name", rs.getString(2));
//		System.out.println(rs.getString(1));
//		System.out.println(rs.getString(2));
		list.add(item);
	}
	
} catch (SQLException e) {
	e.printStackTrace();
}finally {
	try {
		if(conn != null) {
			conn.close();
		}
		if(pstmt != null) {
			pstmt.close();
		}
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	out.println(list);
}

%>