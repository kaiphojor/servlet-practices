package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class EmployeesDao{
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3307/employees?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url,"root","bit");
		} catch (ClassNotFoundException e) {
			System.out.println("error " + e);
		}		
		return conn;
	}
	// 노동자 이름 리스팅
	public JSONArray getEmployeeNameList(int limit){
		JSONArray list = new JSONArray();
		JSONObject item = null; 
		
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;	
		
		try {
			conn = getConnection();			
						
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
//				System.out.println(rs.getString(1));
//				System.out.println(rs.getString(2));
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
		}
		return list;
	}
}
