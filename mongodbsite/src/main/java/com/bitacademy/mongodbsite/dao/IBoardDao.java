package com.bitacademy.mongodbsite.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.bitacademy.mongodbsite.pagination.PagingBean;
import com.bitacademy.mongodbsite.vo.BoardVo;
// BoardDao interface
public interface IBoardDao {
	// 연결
	public Connection getConnection()  throws SQLException;
	// 게시물 생성 
	public boolean insertBoard(BoardVo vo);
	// 답글 생성
	public boolean insertBoardReply(BoardVo originVo, BoardVo vo);
	// 게시물 리스팅 (본문 미포함)
	public List<BoardVo> getBoardPageList(PagingBean pagingBean);
	// 게시물 삭제
	public abstract boolean deleteBoard(BoardVo vo);
	// 조회수 증가 
	public boolean updateBoardViews(Long no);
	// 게시물 가져오기
	public abstract BoardVo getBoard(Long no);
	// 게시물 수정 (글, 제목)
	public abstract boolean updateBoard(BoardVo vo);
	/*
	 *  additional
	 */
	// search by name, contents, name + contents
	public abstract List<BoardVo> searchBoardListByKeyword(PagingBean pagingBean, String column, String keyword);
	// 총 게시글 수 가져오기 
	public abstract int selectBoardListCnt();
	// 검색어 조건에 맞는 게시글 수를 가져온다. 
	public abstract int selectBoardListCnt(String column, String keyword);
	
}
