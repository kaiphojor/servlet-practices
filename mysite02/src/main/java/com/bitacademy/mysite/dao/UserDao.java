package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bitacademy.mysite.vo.GuestbookVo;
import com.bitacademy.mysite.vo.UserVo;

public class UserDao {
	// 이메일, 비밀번호가 일치하는 user를 가져온다. 
	public UserVo findByEmailAndPassword(UserVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		UserVo userVo = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql =  "select no, name  "
					+ "	from user "
					+ "	where email=? and password=?;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getEmail());
			pstmt.setString(2, vo.getPassword());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Long no = rs.getLong(1);
				String email = rs.getString(2);
				userVo = new UserVo(); 
				userVo.setNo(no);
				userVo.setEmail(email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
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
		return userVo;
	}
	// 사용자 추가
	public boolean insert(UserVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String sql =  "insert "
					+ "	into user "
					+ "	values(null,?,?,?,?,now());";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
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
	
	public boolean select(UserVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String sql =  "select no, name  "
					+ "	from user "
					+ "	where email=? and password=?;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getEmail());
			pstmt.setString(2, vo.getPassword());
			// 결과가 1이 아닌경우 false return
			ResultSet rs = pstmt.executeQuery();
			
			result = rs.next();			
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

	// 전체 방명록 조회, 날짜는 sql에서 String format 반환됨
	public List<GuestbookVo> selectAll() {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
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
			conn = getConnection();
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
