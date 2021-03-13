package com.bitacademy.mysite.dao.test;

import java.util.List;

import com.bitacademy.mysite.dao.BoardDao;
import com.bitacademy.mysite.vo.BoardVo;

public class BoardDaoTest {
	public static void main(String[] args) {
		// 게시물 삽입 테스트
//		insertBoardTest();
		
		// 답글 삽입 테스트
//		insertBoardReplyTest();
		
		// 조회수 증가 테스트
//		viewBoardTest();
		
		// 게시물 삭제 테스트 
//		deleteBoardTest();
		
		// 게시물 수정 테스트 
//		updateBoardTest();
		
		
		
		// 게시물 전체 조회 테스트
		selectAllTest();
//		selectVariableTest();

	}
	public static void selectVariableTest() {
//		new BoardDao().setSqlSafeUpdates(false);
//		new BoardDao().selectVariable(); 
//		new BoardDao().setSqlSafeUpdates(true);
//		new BoardDao().selectVariable(); 
	}
	public static void insertBoardTest() {
		BoardVo vo = new BoardVo();
		Long userNo = 2L;
		String title = "삶이 질린다";
		String contents = "무엇을 해야 \n행복할 수 있을까?";
		vo.setUserNo(userNo);
		vo.setTitle(title);
		vo.setContents(contents);
		new BoardDao().insertBoard(vo);
	}
	
	public static void selectAllTest() {
		List<BoardVo> list = new BoardDao().selectAll();
		for(BoardVo vo : list) {
			System.out.println(vo);
		}		
	}
	public static void insertBoardReplyTest() {
		BoardVo ori = new BoardDao().viewBoard(3L); 
		BoardVo vo = new BoardVo(); 
		Long userNo = 8L;
		String title = "한번 그래볼까? 같이 ㄱㄱㄱ";
		String contents = "ㄴㄴㄴ";
		vo.setUserNo(userNo);
		vo.setTitle(title);
		vo.setContents(contents);
		new BoardDao().insertBoardReply(ori, vo);		
	}
	public static void viewBoardTest() {
		new BoardDao().viewBoard(2L);
	}
	public static void deleteBoardTest() {
		new BoardDao().deleteBoard(4L);
	}
	public static void updateBoardTest() {
		BoardVo vo = new BoardVo(); 
		Long no = 3L;
		String title = "한강 수온체크 ㄱㄱㄱ";
		String contents = "ㄷㄷㄷ";
		vo.setNo(no);
		vo.setTitle(title);
		vo.setContents(contents);
		new BoardDao().updateBoard(vo);
	}
	

}
