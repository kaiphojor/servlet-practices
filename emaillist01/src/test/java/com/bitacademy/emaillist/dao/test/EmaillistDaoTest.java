package com.bitacademy.emaillist.dao.test;

import java.util.List;

import com.bitacademy.emaillist.dao.EmaillistDao;
import com.bitacademy.emaillist.vo.EmaillistVo;

public class EmaillistDaoTest {
	public static void main(String[] args) {
		//insert test 
		testInsert();
		
		//findAll test
		testFindAll();
	}
	public static void testInsert() {
		EmaillistVo vo = new EmaillistVo();
		vo.setFirstName("전");
		vo.setLastName("진");
		vo.setEmail("forward@gmail.com");
		new EmaillistDao().insert(vo);
	}
	public static void testFindAll() {
		List<EmaillistVo> list = new EmaillistDao().findAll();
		for(EmaillistVo vo : list) {
			System.out.println(vo);
		}
	}

}
