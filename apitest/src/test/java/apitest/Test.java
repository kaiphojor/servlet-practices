package apitest;

import java.sql.SQLException;

import dao.EmployeesDao;

public class Test {

	public static void main(String[] args) {
		new EmployeesDao().getEmployeeNameList(2);
		System.out.println("done");

	}

}
