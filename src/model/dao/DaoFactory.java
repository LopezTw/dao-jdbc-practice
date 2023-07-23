package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	// Essa é uma classe auxiliar que vai ser responsavel por instanciar os DAOs;
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());         /*
		 							 		Macete pra nao expor a implementação, deixando apenas a interface
									 		Na Class main nos instanciariamos dessa forma: SellerDao sellerDao = DaoFactory.createSellerDao(); 
									 		Dessa forma o programa nao conhece a implementacao, so a interface.	*/
	}
	
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
	
}
