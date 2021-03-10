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
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		//1. jdbc driver loading
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//2. 연결하기 
			String url = "jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url,"webdb","webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("error " + e);
		}
		return conn;
	}
	public boolean insert(EmaillistVo vo) {
		List<EmaillistVo> list = new ArrayList<EmaillistVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		boolean result = false;
		try {
			conn = getConnection();
			//3. sql 준비 
			String sql ="insert into emaillist "
					+ " values(null,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			// 4. 바인딩
			pstmt.setString(1, vo.getFirstName());
			pstmt.setString(2, vo.getLastName());
			pstmt.setString(3, vo.getEmail());
			
			// 5. sql 문 실행 
			int count = pstmt.executeUpdate();
			result = 1 == count ;
			//6 resultSet 가져오기 
			
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
		return result;
	}
	// 전체 이메일 목록 조회
	public List<EmaillistVo> findAll(){
		List<EmaillistVo> list = new ArrayList<EmaillistVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
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
