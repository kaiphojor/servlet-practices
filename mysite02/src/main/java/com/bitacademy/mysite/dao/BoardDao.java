package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bitacademy.mysite.vo.BoardVo;

public class BoardDao implements BoardDaoService{
	public Connection getConnection() throws SQLException {
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
	// 게시물 입력 (답글 제외)
	public boolean insertBoard(BoardVo vo) {
		boolean result = false;
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();	
			
			sql =  "insert into board(user_no, title, group_no, order_no, "
				+ " depth, contents, reg_date) "
				+ "	values(?, ?, ifnull((select max(group_no)+1 from board b),1), 1, "
				+ " 0, ?, now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getUserNo());
			pstmt.setString(2, vo.getTitle());
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
	@Override
	public boolean insertBoardReply(BoardVo originVo, BoardVo vo) {
		boolean result = false;
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();
			
			// update 문에서 key column 이외의 column을 where절에서 사용하기 위해 필요하다.
			// 변경사항은 mysql 해당 DB세션에서만 유지된다.
			sql =  "set sql_safe_updates=0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();	
			pstmt.close();			
			
			// 답글 작성시 먼저 수행해야할 구문								
			sql = "update board "
					+ "	set order_no = order_no + 1 "
					+ " where group_no = ? and order_no >= ? + 1;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, originVo.getGroupNo());
			pstmt.setLong(2, originVo.getOrderNo());
			pstmt.executeUpdate();	
			pstmt.close();		
			
			// insert 수행 (원글의 순서, 깊이를 이용)
			sql =  "insert into board(user_no, title, group_no, order_no, "
					+ " depth, contents, reg_date) "
					+ "	values(?, ?, ?, ?+1, "
					+ " ?+1, ?, now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getUserNo());
			pstmt.setString(2, vo.getTitle());
			pstmt.setLong(3, originVo.getGroupNo());
			pstmt.setLong(4, originVo.getOrderNo());
			pstmt.setLong(5, originVo.getDepth());
			pstmt.setString(6, vo.getContents());
			
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
	// 게시물 리스팅(게시판)
	public List<BoardVo> selectAll(){
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = getConnection();
			//
			// 변경사항은 mysql 해당 DB세션에서만 유지된다.
			sql =  "set sql_safe_updates=0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();	
			pstmt.close();
			
			
			sql =  "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, "
					+ " date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views, u.name "
					+ "	from board b "
					+ " join user u "
					+ " on b.user_no = u.no "
					+ "	order by group_no DESC, order_no ASC;";
			
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setUserNo(rs.getLong(2));
				vo.setTitle(rs.getString(3));
				vo.setGroupNo(rs.getLong(4));
				vo.setOrderNo(rs.getLong(5));
				vo.setDepth(rs.getLong(6));
				vo.setRegDate(rs.getString(7));
				vo.setViews(rs.getLong(8));
				vo.setUserName(rs.getString(9));
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
	@Override
	public boolean deleteBoard(Long no) {
		boolean result = false;
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			conn = getConnection();
						
			sql = " delete from board "
				+ "	where no = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			
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
	@Override
	public BoardVo viewBoard(Long no) {
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		BoardVo vo = null;
		try {
			conn = getConnection();	
			// 조회수 증가
			sql = "update board "
					+ "	set views = views + 1"
					+ " where no = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			pstmt.executeUpdate();
			pstmt.close();
			
			// 게시물 전체 내용 가져오기
			sql =  " select b.no, b.user_no, b.title, b.group_no, b.order_no, "
					+ " b.depth, b.contents, date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views "
					+ "	from board b "
					+ " join user u on b.no = ? and b.user_no = u.no;";					
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setUserNo(rs.getLong(2));
				vo.setTitle(rs.getString(3));
				vo.setGroupNo(rs.getLong(4));
				vo.setOrderNo(rs.getLong(5));
				vo.setDepth(rs.getLong(6));
				vo.setContents(rs.getString(7));
				vo.setRegDate(rs.getString(8));
				vo.setUserName(rs.getString(9));
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
		return vo;
	}
	@Override
	public boolean updateBoard(BoardVo vo) {
		boolean result = false;
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = getConnection();	
			// 조회수 증가
			sql = "update board "
					+ "	set title = ?, contents = ? "
					+ " where no = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
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
	@Override
	public List<BoardVo> searchBoardByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BoardVo> searchBoardByContents(String contents) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BoardVo> searchBoardByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BoardVo> searchBoardByTitleContents(String titleContents) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BoardVo> selectPage(int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}