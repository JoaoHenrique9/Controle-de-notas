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
import db.DbIntegrityException;
import model.dao.SetorDao;
import model.entities.Setor;

public class SetorDaoJDBC implements SetorDao {
	
private Connection conn;
	
	public SetorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Setor obj) {
		PreparedStatement st = null;
		if (obj.getSupervisor() == null) {
			obj.setSupervisor(" ");
		}
		try {
			st = conn.prepareStatement(
				"INSERT INTO setor " +
				"(Nome_Setor, supervisor) " +
				"VALUES " +
				"(?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getSupervisor());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Setor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE Setor " +
				"SET Nome_Setor = ?, Supervisor = ? " +
				"WHERE Id = ?");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getSupervisor());
			st.setInt(3, obj.getId());

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
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM setor WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Setor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Setor "
				+ "ORDER BY Nome_setor");
			rs = st.executeQuery();

			List<Setor> list = new ArrayList<>();

			while (rs.next()) {
				Setor obj = new Setor();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Setor"));
				obj.setSupervisor(rs.getString("supervisor"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}	
	}
	@Override
	public Setor findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Setor WHERE Nome_Setor = ?");
			st.setString(1, nome);
			rs = st.executeQuery();
			if (rs.next()) {
				Setor obj = new Setor();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Setor"));
				obj.setSupervisor(rs.getString("supervisor"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
