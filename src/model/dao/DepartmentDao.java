package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj); // Vai inserir no banco de dados o obj que for recebido no parametro

	void update(Department obj); // Vai atualizar no banco de dados o obj que for recebido no parametro

	void deleteById(Integer id); // Deletar no banco de dados de acordo com o ID recebido no parametro

	Department findById(Integer id); // Vai pegar o id recebido no parametro e consultar no banco de dados o obj com esse id

	List<Department> findAll(); // Gerar uma lista com tudo do Department
	
}
