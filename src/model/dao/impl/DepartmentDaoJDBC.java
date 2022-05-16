package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connection;

	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("Insert into department (id,name) values(?,?)",
					Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setInt(1, obj.getId());
			preparedStatement.setString(2, obj.getName());

			int qtdeRegistrosAfetados = preparedStatement.executeUpdate();

			if (qtdeRegistrosAfetados > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					int id = resultSet.getInt(1);
					obj.setId(id);

				}

				DB.closeResulSet(resultSet);
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		}

		finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement("Update department set name = ? where id = ?");

			preparedStatement.setString(1, obj.getName());
			preparedStatement.setInt(2, obj.getId());

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
			preparedStatement = connection.prepareStatement("Delete from department where id = ?");

			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement("Select * from department where id = ? ");

			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Department department = new Department();
				department.setId(resultSet.getInt(1));
				department.setName(resultSet.getString("Name"));
				;
				return department;
			}

			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeResulSet(resultSet);
			DB.closeStatement(preparedStatement);
		}

	}

	@Override
	public List<Department> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection.prepareStatement("Select * from department");

			resultSet = preparedStatement.executeQuery();

			List<Department> listDepartments = new ArrayList<Department>();

			while (resultSet.next()) {
				Department department = instantiateDepartament(resultSet);
				listDepartments.add(department);
			}

			return listDepartments;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeResulSet(resultSet);
			DB.closeStatement(preparedStatement);
		}

	}

	private Department instantiateDepartament(ResultSet resultSet) throws SQLException {
		Department departamentTemporaria = new Department();
		departamentTemporaria.setId(resultSet.getInt("Id"));
		departamentTemporaria.setName(resultSet.getString("Name"));
		return departamentTemporaria;
	}
}
