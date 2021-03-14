package com.bitacademy.mysite.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.bitacademy.mysite.vo.BoardVo;
// BoardDao interface
public interface BoardDaoService {
	// 연결
	public Connection getConnection()  throws SQLException;
	// 게시물 생성 
	public boolean insertBoard(BoardVo vo);
	// 답글 생성
	public boolean insertBoardReply(BoardVo originVo, BoardVo vo);
	// 게시물 리스팅 (본문 미포함)
	public List<BoardVo> selectAll();
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
	public abstract List<BoardVo> searchBoardByName(String name);
	public abstract List<BoardVo> searchBoardByContents(String contents);
	public abstract List<BoardVo> searchBoardByTitle(String title);
	public abstract List<BoardVo> searchBoardByTitleContents(String titleContents);
	// paging
	public abstract List<BoardVo> selectPage(int pageNum);
	
}
