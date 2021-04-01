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

import com.bitacademy.mysite.pagination.PagingBean;
import com.bitacademy.mysite.vo.BoardVo;

public class BoardDao implements IBoardDao{
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
	// 게시물 입력 (답글 제외)
	public boolean insertBoard(BoardVo vo) {
		boolean result = false;
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
//			conn = getConnection();	
			conn = getConnection("master");	
			
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
//			conn = getConnection();
			conn = getConnection("master");
			
			// update 문에서 key column 이외의 column을 where절에서 사용하기 위해 필요하다.
			// 변경사항은 mysql 해당 DB세션에서만 유지된다.
			sql =  "set sql_safe_updates=0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();	
			pstmt.close();			
			
			// 답글 작성시 먼저 수행해야할 구문
			// 같은 그룹 에서 원글보다 나중에 달린 글들의 순서를 하나씩 증가시켜서 답글이 달릴 자리(순서)를 확보한다. 
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
	public List<BoardVo> getBoardPageList(PagingBean pagingBean){
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;	
		
		try {
//			conn = getConnection();
			conn = getConnection("slave1");
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
					+ "	order by group_no DESC, order_no ASC"
					+ " LIMIT ?, ? ;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagingBean.getStartRowNumber()-1);
			pstmt.setInt(2, pagingBean.getEndRowNumber() - pagingBean.getStartRowNumber()+1);
			
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
	// 답글이 달린 원 게시글은 삭제하지 않는다.
	@Override
	public boolean deleteBoard(BoardVo vo) {
		boolean result = false;
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = "";
		try {
//			conn = getConnection();						
			conn = getConnection("master");						
			
			// 해당 그룹 내에서 답글이 없는 글(마지막 순서) 만 삭제 가능하다.
			sql = " delete from board "
					+ "where no = ? and order_no = (select max(order_no) from (select order_no from board where group_no = ?) b) ;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getNo());
			pstmt.setLong(2, vo.getGroupNo());
			
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
	
	public boolean updateBoardViews(Long no) {
		boolean result = false;
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		BoardVo vo = null;
		try {
//			conn = getConnection();	
			conn = getConnection("master");	
			// 조회수 증가
			sql = "update board "
					+ "	set views = views + 1"
					+ " where no = ?;";
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
	public BoardVo getBoard(Long no) {
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		BoardVo vo = null;
		try {
//			conn = getConnection();	
			conn = getConnection("slave1");	
			
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
//			conn = getConnection();	
			conn = getConnection("master");	
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
	public List<BoardVo> searchBoardListByKeyword(PagingBean pagingBean, String column, String keyword){
		List<BoardVo> list = new ArrayList<BoardVo>();
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;	
		
		try {
//			conn = getConnection();
			conn = getConnection("slave1");
			// 변경사항은 mysql 해당 DB세션에서만 유지된다.
			sql =  "set sql_safe_updates=0;";
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();	
			pstmt.close();
			
			sql =  "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, "
					+ " date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views, u.name "
					+ "	from board b "
					+ " join user u "
					+ " on b.user_no = u.no "; 
			// column 입력 값 별 질의문 차별화
			if("user".equals(column)) {
				sql += " and u.name like ? ";
			}else if("title".equals(column)) {
				sql += " and b.title like concat('%',?,'%') ";
			}else if("contents".equals(column)) {
				sql += " and b.contents like concat('%',?,'%') ";
			}else {
				sql += " and b.title like concat('%',?,'%') ";
			}
			sql += "	order by group_no DESC, order_no ASC"
					+ " LIMIT ?, ? ;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setInt(2, pagingBean.getStartRowNumber()-1);
			pstmt.setInt(3, pagingBean.getEndRowNumber() - pagingBean.getStartRowNumber()+1);				
			
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
	public int selectBoardListCnt() {
//		https://gangnam-americano.tistory.com/18
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		try {
//			conn = getConnection();	
			conn = getConnection("slave1");	
			
			// 게시물 전체 내용 가져오기
			sql =  " select count(no) "
					+ "	from board;";
			
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
		return result;
	}
	// 검색어 조건에 맞는 게시글 수를 가져온다. 
	@Override
	public int selectBoardListCnt(String column, String keyword) {
		// TODO Auto-generated method stub
		Connection conn = null ;
		PreparedStatement pstmt = null;
		String sql = null;
		int result = 0;
		try {
//			conn = getConnection();	
			conn = getConnection("slave1");	
			
			// 게시물 전체 내용 가져오기
			sql =  " select count(1) "
					+ "	from board "; 
			if("title".equals(column)){
				sql += " where title like concat('%',?,'%');";				
			}else if("contents".equals(column)) {
				sql += " where contents like concat('%',?,'%');";				
			}else if("user".equals(column)) {
				sql += " b join user u on b.user_no = u.no "
					+ " and u.name like ?;";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
		return result;
	}
}
