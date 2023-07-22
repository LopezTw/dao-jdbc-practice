package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	// Criado uma dependencia q estará disponivel em qualquer lugar dentro dessa
	// classe
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("	INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?) ", Statement.RETURN_GENERATED_KEYS

			);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);

				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected Error ! No Rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ? ", Statement.RETURN_GENERATED_KEYS

			);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			st.setInt(6, obj.getId());

			st.executeUpdate();

			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM seller " + "WHERE Id = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n" + "FROM seller INNER JOIN department\r\n"
							+ "ON seller.DepartmentId = department.Id\r\n" + "WHERE seller.Id = ?");

			st.setInt(1, id); // O primeiro (1) ponto de interrogaçao recebera como paramento o id
			rs = st.executeQuery();

			/**
			 * IMPORTANTE ENTENDIMENTO:
			 * 
			 * O ResulSet, nos traz os dados em formato de TABELA sendo um objeto com Linhas
			 * e Colunas (igual aparece no Workbench. Só que como estamos progamando
			 * Orientado a Objetos, nos precisamos transformar esses dados em Objetos
			 * Associados ! Objetos Associados: são objetos que tem uma associação com outro
			 * ( Seller <-> Department ).
			 * 
			 * Entao como estamos tratando de OOP, precisamos ter na memória do computador,
			 * Objetos Associados INSTANCIADOS em memória.
			 * 
			 * O IF abaixo, serve pra testar se recebeu algum resultado referente ao ID
			 * recebido la no inicio do método. Se nao retonar nada, vai dar NULL e se
			 * retornar alguma informação (no caso as informações que constam na tabela),
			 * teremos que navegar por esse resultado pra podermos instanciarmos os Objetos
			 * (No caso abaixo, o Seller com Department)
			 * 
			 * 
			 */

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);

				return obj;

			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;

	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name ");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();

			Map<Integer, Department> map = new HashMap<>(); // Vai ser guardado qualquer Department que for instanciado

			while (rs.next()) { // percorrer a lista enquanto tiver dados

				Department dep = map.get(rs.getInt("DepartmentId")); // tenta buscar um Department que possui algum
																		// Departamento com Id = ?;
																		// caso nao exista o valor especificado em ? ,
																		// retornará nulo !

				if (dep == null) { // Caso seja null o valor pesquisado, vai ser instanciado o valor pesquisado e
									// salvar o valor no map.
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name ");

			st.setInt(1, department.getId()); // O primeiro (1) ponto de interrogaçao recebera como paramento
												// Departament
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();

			Map<Integer, Department> map = new HashMap<>(); // Vai ser guardado qualquer Department que for instanciado

			while (rs.next()) { // percorrer a lista enquanto tiver dados

				Department dep = map.get(rs.getInt("DepartmentId")); // tenta buscar um Department que possui algum
																		// Departamento com Id = ?;
																		// caso nao exista o valor especificado em ? ,
																		// retornará nulo !

				if (dep == null) { // Caso seja null o valor pesquisado, vai ser instanciado o valor pesquisado e
									// salvar o valor no map.
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);

			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
