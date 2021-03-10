package com.bitacademy.guestbook01.test;

import java.util.List;

import com.bitacademy.guestbook01.dao.GuestbookDao;
import com.bitacademy.guestbook01.vo.GuestbookVo;
/*
 * test case들
 */

public class GuestbookDaoTest {

	public static void main(String[] args) {
		//insert test 
//		insertTest();
		//delete test
//		deleteTest();
		//select all test 
		selectAllTest();

	}
	public static void selectAllTest() {
		List<GuestbookVo> list = new GuestbookDao().selectAll();
		for(GuestbookVo vo : list) {
			System.out.println(vo);
		}
	}
	public static void insertTest() {
		GuestbookVo vo = new GuestbookVo();
		vo.setName("정대만");
		vo.setContents("선생님 농구가 하고 싶어요....!");
		vo.setPassword("8282");
		new GuestbookDao().insert(vo);
	}
	public static void deleteTest() {
		GuestbookVo vo = new GuestbookVo();
		vo.setNo(2);
		vo.setContents("선생님 농구가 하고 싶어요....!");
		vo.setPassword("8282");
		new GuestbookDao().delete(vo);
	}
}
