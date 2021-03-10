package com.bitacademy.emaillist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bitacademy.emaillist.vo.EmaillistVo;

public class EmaillistDao {
	public List<EmaillistVo> findAll(){
		List<EmaillistVo> list = new ArrayList<EmaillistVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			//1. jdbc driver loading
			Class.forName("com.mysql.jdbc.Driver");
			//2. 연결하기 
			String url = "jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url,"webdb","webdb");
			//3. sql 준비 
			String sql ="select no, first_name, last_name, email "
					+ " from emaillist "
					+ " order by no desc;";
			pstmt = conn.prepareStatement(sql);
			// 4. 바인딩
			// 5. sql 문 실행 
			rs = pstmt.executeQuery();
			//6 resultSet 가져오기 
			while(rs.next()) {
				Long no = rs.getLong(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);
				String email = rs.getString(4);
				EmaillistVo vo = new EmaillistVo();
				vo.setNo(no);
				vo.setFirstName(firstName);
				vo.setLastName(lastName);
				vo.setEmail(email);
				list.add(vo);
			}
		} catch (ClassNotFoundException e) {
			// 1. 사과 
			// 2. 로그 
			System.out.println("error:" + e);
			// 3. 안전하게 종료 
		} catch (SQLException e) {
			// 1. 사과 
			// 2. 로그 
			System.out.println("error:" + e);
		}finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();				
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
		return list;
	}
}
