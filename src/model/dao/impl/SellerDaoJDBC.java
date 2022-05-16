package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection connection;

	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Department department = instantiateDepartament(resultSet);

				Seller obj = instantiateSeller(resultSet, department);
				return obj;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			
			DB.closeStatement(preparedStatement);
			DB.closeResulSet(resultSet);
		}

	}

	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller objSellerTemporarioSeller = new Seller();
		objSellerTemporarioSeller.setId(resultSet.getInt("Id"));
		objSellerTemporarioSeller.setNome(resultSet.getString("Name"));
		objSellerTemporarioSeller.setEmail(resultSet.getString("Email"));
		objSellerTemporarioSeller.setBaseSalary(resultSet.getDouble("BaseSalary"));
		objSellerTemporarioSeller.setBirthdate(resultSet.getDate("BirthDate"));
		objSellerTemporarioSeller.setDepartment(department);
		return objSellerTemporarioSeller;
	}

	private Department instantiateDepartament(ResultSet resultSet) throws SQLException {
		Department  departamentTemporaria= new Department();
		departamentTemporaria.setId(resultSet.getInt("DepartmentId"));
		departamentTemporaria.setName(resultSet.getString("DepName"));
		return departamentTemporaria;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
