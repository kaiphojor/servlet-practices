package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bitacademy.mysite.vo.GuestbookVo;
/*
 * 데이터에 대한 접근, 조작을 제어하는 Controller ( Data Access Object)
 * 
 */
public class GuestbookDao {
	// jdbc 연동을 통한 mysql connection 획득, sql execption은 각 메소드에서 처리한다.
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url,"webdb","webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("error " + e);
		}		
		return conn;
	}
	// db 복제를 위한 getConnection
	public Connection getConnection(String dbName) throws SQLException {
		// connection map
		Map<String, Integer> portMap = new HashMap<>();
		portMap.put("master", 3307);
		portMap.put("slave1", 3308);
//		portMap.put("slave2", 3309);
//		portMap.put("slave3", 3310);
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:"+portMap.get(dbName)+"/webdb?characterEncoding=utf8&serverTimezone=UTC";
			if("master".equals(dbName)) {
				conn = DriverManager.getConnection(url, "root", "masterpw");
			}else{
				conn = DriverManager.getConnection(url, "root", "slavepw");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("error " + e);
		}
		return conn;
	}
	// 방명록 추가
	public boolean insert(GuestbookVo vo) {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		boolean result = false;
		try {
//			conn = getConnection();
			conn = getConnection("master");
			String sql =  "insert "
					+ "	into guestbook "
					+ "	values(null,?,?,?,now());";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());
			// 결과가 1이 아닌경우 false return
			result = 1 == pstmt.executeUpdate();

			
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
		return result;
	}
	// 전체 방명록 조회, 날짜는 sql에서 String format 반환됨
	public List<GuestbookVo> selectAll() {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		try {
//			conn = getConnection();
			conn = getConnection("slave1");
			String sql =  "select no, name,  date_format(reg_date,'%Y-%m-%d %H:%i:%s'),contents "
					+ "	from guestbook "
					+ "	order by reg_date desc;";
			
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setRegDate(rs.getString(3));
				vo.setContents(rs.getString(4));
				list.add(vo);
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
	// 번호와 비밀번호를 통한 방명록 메시지 삭제
	public boolean delete(GuestbookVo vo) {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		boolean result = false;
		try {
//			conn = getConnection();
			conn = getConnection("master");
			String sql =  "delete from guestbook "
					+ "	where no = ? and password = ?;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());
			// 결과가 1이 아닌경우 false return
			result = 1 == pstmt.executeUpdate();

			
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
		return result;		
	}
}
