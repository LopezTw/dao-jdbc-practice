package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		List<Department> list = new ArrayList<>();
		
		System.out.println("\n=== TEST 1: department insert ===");
		Department newDepartment = new Department(0, "Plants");
		
		//departmentDao.insert(newDepartment);
		//System.out.println("Inserted! New Department = " + newDepartment.getId());
		
		
		System.out.println("\n=== TEST 2: Department update ===");
		Department department = departmentDao.findById(1);
		department.setName("Joias");
		departmentDao.update(department);
		System.out.println("Update Completed !");
		
		System.out.println("\n=== TEST 3: deparment findAll ===");
		list = departmentDao.findAll();
		for(Department obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 4: department delete ===");
		// intereção com o usuario a fim de ilustraçao:
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		departmentDao.deleteById(id);
		System.out.println("Delete Completed");
				
		sc.close();
		
	}

}
