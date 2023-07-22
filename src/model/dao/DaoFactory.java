package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	// Essa é uma classe auxiliar que vai ser responsavel por instanciar os DAOs;
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();         /*
		 							 		Macete pra nao expor a implementação, deixando apenas a interface
									 		Na Class main nos instanciariamos dessa forma: SellerDao sellerDao = DaoFactory.createSellerDao(); 
									 		Dessa forma o programa nao conhece a implementacao, so a interface.	*/
	}
	
	
}
