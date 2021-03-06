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

	private Connection connection;

	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("INSERT INTO seller"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)" + "VALUES" + "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, obj.getNome());
			preparedStatement.setString(2, obj.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
			preparedStatement.setDouble(4, obj.getBaseSalary());
			preparedStatement.setInt(5, obj.getDepartment().getId());

			int linhasAfetadas = preparedStatement.executeUpdate();

			if (linhasAfetadas > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					int id = resultSet.getInt(1);
					obj.setId(id);

				}
				DB.closeResulSet(resultSet);

			} else {
				throw new DbException("ERRO INEXPERADO NENHUMA LINHA INSERIDA");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

			preparedStatement.setString(1, obj.getNome());
			preparedStatement.setString(2, obj.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
			preparedStatement.setDouble(4, obj.getBaseSalary());
			preparedStatement.setInt(5, obj.getDepartment().getId());
			preparedStatement.setInt(6, obj.getId());

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");

			preparedStatement.setInt(1, id);
			
			int linhasAfetadas =  preparedStatement.executeUpdate();
			
			if (linhasAfetadas == 0) {
				throw new DbException("ID NAO EXISTE!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			preparedStatement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

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
		} finally {

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
		Department departamentTemporaria = new Department();
		departamentTemporaria.setId(resultSet.getInt("DepartmentId"));
		departamentTemporaria.setName(resultSet.getString("DepName"));
		return departamentTemporaria;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			preparedStatement = connection.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department " + "ON seller.DepartmentId = department.Id Order by name");

			resultSet = preparedStatement.executeQuery();

			List<Seller> listaSellers = new ArrayList<Seller>();

			Map<Integer, Department> map = new HashMap<Integer, Department>(); // controlar pra nao repetir o
																				// departamento

			while (resultSet.next()) {

				Department dep = map.get(resultSet.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartament(resultSet);
					map.put(resultSet.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(resultSet, dep);
				listaSellers.add(obj);
			}
			return listaSellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {

			DB.closeStatement(preparedStatement);
			DB.closeResulSet(resultSet);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			preparedStatement = connection.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? Order by name");

			preparedStatement.setInt(1, department.getId());
			resultSet = preparedStatement.executeQuery();

			List<Seller> listaSellers = new ArrayList<Seller>();

			Map<Integer, Department> map = new HashMap<Integer, Department>();

			while (resultSet.next()) {

				Department dep = map.get(resultSet.getInt("DepartmentId"));

				if (dep == null) {
					dep = instantiateDepartament(resultSet);
					map.put(resultSet.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(resultSet, dep);
				listaSellers.add(obj);
			}
			return listaSellers;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {

			DB.closeStatement(preparedStatement);
			DB.closeResulSet(resultSet);
		}
	}

}
