package com.bitacademy.mongodbsite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.bitacademy.mongodbsite.pagination.PagingBean;
import com.bitacademy.mongodbsite.vo.BoardVo;
import com.bitacademy.mongodbsite.vo.GuestbookVo;
import com.bitacademy.web.mvc.WebUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Variable;
import com.mongodb.internal.client.model.AggregationLevel;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static java.util.Arrays.*;

public class BoardDao implements IBoardDao{
	
	/*
	 *  Client, DB, Collection을 호출하는 method
	 */
	public static MongoClient getClient(String host,int port) throws Exception{
		return MongoClients.create("mongodb://"+host+":"+port);			
	}
	public static MongoClient getClient() throws Exception{
		return getClient("localhost",27017);
	}
	public static MongoDatabase getDB(MongoClient client,String databaseName) throws Exception{
		return client.getDatabase(databaseName);
	}
	public static MongoDatabase getDB(MongoClient client) throws Exception{
		return getDB(client,"webdb");
	}
	public static MongoCollection<Document> getCollection(MongoDatabase database,String collectionName) throws Exception{
		return database.getCollection(collectionName);
	}
	public static MongoCollection<Document> getCollection(MongoDatabase database) throws Exception{
		return database.getCollection("board");
	}
	
	// auto increment를 대신하는 counter collection 
	private static Long updateCounter(MongoDatabase database) {
		MongoCollection<Document> collection = database.getCollection("bcounter");
		Document docRevised = collection.findOneAndUpdate(eq("_id","userid"),inc("seq",1));
		return Long.valueOf(docRevised.get("seq").toString());
	}
	// 게시물 입력 (답글 제외)
	public boolean insertBoard(BoardVo vo) {
		/*
		 * 			sql =  "insert into board(user_no, title, group_no, order_no, "
				+ " depth, contents, reg_date) "
				+ "	values(?, ?, ifnull((select max(group_no)+1 from board b),1), 1, "
				+ " 0, ?, now());";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getUserNo());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContents());
		 */
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);

			Long no = updateCounter(db);
			Long order_no = WebUtil.intToLong(1);
			Document groupNoDoc = collection.find().sort(descending("group_no")).first();
			Long group_no = groupNoDoc != null ? groupNoDoc.getLong("group_no") + 1 : 1;
			Long depth = WebUtil.intToLong(0);
			Long views = depth;
			Document doc = new Document()
					.append("no", no)
					.append("user_no",vo.getUserNo())
					.append("title", vo.getTitle())
					.append("group_no", group_no)
					.append("order_no", order_no)
					.append("depth", depth)
					.append("contents", vo.getContents())
					.append("reg_date", new Date())
					.append("views", views);
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	@Override
	public boolean insertBoardReply(BoardVo originVo, BoardVo vo) {
		/*
		 * 			// 답글 작성시 먼저 수행해야할 구문
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
		 */
		boolean result = false;
//		List<BoardVo> list = new ArrayList<BoardVo>();
		
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			// 답글 작성시 먼저 수행해야할 구문
			// 같은 그룹 에서 원글보다 나중에 달린 글들의 순서를 하나씩 증가시켜서 답글이 달릴 자리(순서)를 확보한다. 
			collection.updateMany(and(
					eq("group_no", originVo.getGroupNo())
					,gt("order_no", originVo.getOrderNo()))
					,inc("order_no", 1));
			
			// insert 수행 (원글의 순서, 깊이를 이용)
			Long no = updateCounter(db);
			Document doc = new Document()
					.append("no", no)
					.append("user_no",vo.getUserNo())
					.append("title", vo.getTitle())
					.append("group_no", originVo.getGroupNo())
					.append("order_no", originVo.getOrderNo()+1)
					.append("depth", originVo.getDepth()+1)
					.append("contents", vo.getContents())
					.append("reg_date", new Date())
					.append("views", 0L);
			collection.insertOne(doc);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	// 게시물 리스팅(게시판)
	public List<BoardVo> getBoardPageList(PagingBean pagingBean){

//		return list;
		/*
		 * 			sql =  "select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, "
					+ " date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views, u.name "
					+ "	from board b "
					+ " join user u "
					+ " on b.user_no = u.no "					
					+ "	order by group_no DESC, order_no ASC"
					+ " LIMIT ?, ? ;";
					
								pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagingBean.getStartRowNumber()-1);
			pstmt.setInt(2, pagingBean.getEndRowNumber() - pagingBean.getStartRowNumber()+1);
			
					
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
//				list.add(vo);
			}
		 */
		
		List<BoardVo> list = new ArrayList<BoardVo>();

		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		Document resultDoc = null;
		BoardVo boardVo = null;
		MongoCursor<Document> cursor = null;
		BoardVo vo = null;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			
//			match(eq("no",2L));
//			project(fields(include("no","name")));
//			sort(orderBy(descending("group_no"), ascending("order_no")));
//			limit(5);

            //Aggregations.
			/*
			 * https://github.com/mongodb/mongo-java-driver/blob/master/docs/reference/content/builders/aggregation.md
			 * 
			 * https://docs.mongodb.com/manual/reference/operator/aggregation/
			 * 
			 * https://developer.mongodb.com/community/forums/t/aggregation-pipeline-with-lookup-in-java/100454
			 * 
			 * https://www.baeldung.com/java-mongodb-aggregations
			 * 
			 * https://mongodb.github.io/mongo-java-driver/3.2/builders/aggregation/
			 * 
			 * https://docs.mongodb.com/manual/tutorial/aggregation-with-user-preference-data/
			 */
			
//			List<Document> docList = collection
//					.find()
//					.sort(orderBy(descending("group_no"),ascending("order_no")))
//					.limit(5)
//					.into(new ArrayList<Document>());
			
			List<Variable<String>> variable = asList(new Variable<>("uno", "$user_no"));
			List<Bson> pipeline = asList(
					match(
							expr(
									new Document("$eq", asList("$no", "$$uno"))
								)
						)
					,project(
							fields(include("name"),excludeId())
							)
					);
			
			List<Document> docList = collection.aggregate(
					asList(
							lookup("user", variable, pipeline, "user_name")
							,unwind("$user_name")
							,new Document("$addFields",new Document("user_name","$user_name.name"))
							,sort(orderBy(descending("group_no"),ascending("order_no")))
							,skip(0)
							,limit(5)
						)
					).into(new ArrayList<Document>());
			
			
			for(Document doc : docList) {
				vo = new BoardVo();
				long no = (long)(doc.get("no")); 
				vo.setNo(no);
				vo.setUserNo( doc.getLong("user_no"));
				vo.setUserName( doc.getString("user_name"));
				vo.setTitle( doc.getString("title"));
				vo.setGroupNo( doc.getLong("group_no"));
				vo.setOrderNo( doc.getLong("order_no"));
				vo.setDepth( doc.getLong("depth"));
				vo.setContents((String) doc.get("contents"));
				vo.setRegDate(WebUtil.getFormatDate((Date) doc.get("reg_date")));
				vo.setViews( doc.getLong("views"));
				list.add(vo);
			}
//			cursor = collection.find().iterator();
//			try {
//				while (cursor.hasNext()) {
//					resultDoc = cursor.next();
//					boardVo = new BoardVo();
////					System.out.println(resultDoc.get("no").getClass().getName());
//					long no = (long)(resultDoc.get("no")); 
//					boardVo.setNo(no);
////					boardVo.setName((String) resultDoc.get("name"));
//					boardVo.setUserNo( resultDoc.getLong("user_no"));
//					boardVo.setUserName( resultDoc.getString("user_name"));
//					boardVo.setTitle( resultDoc.getString("title"));
//					boardVo.setGroupNo( resultDoc.getLong("group_no"));
//					boardVo.setOrderNo( resultDoc.getLong("order_no"));
//					boardVo.setDepth( resultDoc.getLong("depth"));
//					boardVo.setContents((String) resultDoc.get("contents"));
//					boardVo.setRegDate(WebUtil.getFormatDate((Date) resultDoc.get("reg_date")));
//					boardVo.setViews( resultDoc.getLong("views"));
//					list.add(boardVo);
//				}
//			} finally {
//				cursor.close();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return list;
	}
	// 답글이 달린 원 게시글은 삭제하지 않는다.
	@Override
	public boolean deleteBoard(BoardVo vo) {
		/*
		 * 			// 해당 그룹 내에서 답글이 없는 글(마지막 순서) 만 삭제 가능하다.
			sql = " delete from board "
					+ "where no = ? and order_no = (select max(order_no) from (select order_no from board where group_no = ?) b) ;";
		 */
		
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			// 해당 그룹 내에서 답글이 없는 글(마지막 순서) 만 삭제 가능하다.
			Long group_no = vo.getGroupNo();
			Long order_no = (Long)(collection.find(eq("group_no",group_no)).sort(descending("order_no")).first().get("order_no"));
			collection.findOneAndDelete(
					and(eq("no",vo.getNo()),eq("group_no",group_no),eq("order_no",order_no)));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
		
	}
	
	public boolean updateBoardViews(Long no) {
		// 조회수 증가
//					sql = "update board "
//							+ "	set views = views + 1"
//							+ " where no = ?;";
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			collection.findOneAndUpdate(eq("no",no),inc("views",1));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	@Override
	public BoardVo getBoard(Long no) {
		/*
		 * 			// 게시물 전체 내용 가져오기
			sql =  " select b.no, b.user_no, b.title, b.group_no, b.order_no, "
					+ " b.depth, b.contents, date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views "
					+ "	from board b "
					+ " join user u on b.no = ? and b.user_no = u.no;";		
					
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
		 */
		BoardVo vo = null;
		return vo;
	}
	@Override
	public boolean updateBoard(BoardVo vo) {
//		sql = "update board "
//				+ "	set title = ?, contents = ? "
//				+ " where no = ?;";
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		boolean result = false;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			collection.findOneAndUpdate(eq("no",vo.getNo()),combine(
					set("title",vo.getTitle())
					,set("contents",vo.getContents())));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
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
//			conn = getConnection("slave1");
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
//		// 게시물 전체 내용 가져오기
//		sql =  " select count(no) "
//				+ "	from board;";
//		
		MongoClient client = null;
		MongoDatabase db = null;
		MongoCollection<Document> collection = null;
		long result = -1L;
		try {
			client = getClient();
			db = getDB(client);
			collection = getCollection(db);
			
			result = collection.countDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return WebUtil.longToInt(result);
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
//			conn = getConnection("slave1");	
			
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
