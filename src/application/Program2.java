package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		
		System.out.println("\n=== TEST 1: department insert ===");
		Department newDepartment = new Department(0, "Plants");
		
		//departmentDao.insert(newDepartment);
		//System.out.println("Inserted! New Department = " + newDepartment.getId());
		
		
		System.out.println("\n=== TEST 2: Department update ===");
		Department department = departmentDao.findById(1);
		department.setName("Joias");
		departmentDao.update(department);
		System.out.println("Update Completed !");
		
	}

}
