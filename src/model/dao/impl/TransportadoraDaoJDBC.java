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
import model.dao.TransportadoraDao;
import model.entities.Transportadora;

public class TransportadoraDaoJDBC implements TransportadoraDao {

private Connection conn;
	
	public TransportadoraDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Transportadora findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Transportadora WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Transportadora obj = new Transportadora();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Transportadora"));
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
	
	public Transportadora findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Transportadora WHERE Nome_Transportadora = ?");
			st.setString(1, nome);
			rs = st.executeQuery();
			if (rs.next()) {
				Transportadora obj = new Transportadora();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Transportadora"));
				obj.setTipo(rs.getString("tipo"));
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


	@Override
	public List<Transportadora> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Transportadora ORDER BY Nome_Transportadora");
			rs = st.executeQuery();

			List<Transportadora> list = new ArrayList<>();

			while (rs.next()) {
				Transportadora obj = new Transportadora();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Transportadora"));
				obj.setTipo(rs.getString("tipo"));
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
	public void insert(Transportadora obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO transportadora " +
				"(Nome_Transportadora, Tipo) " +
				"VALUES " +
				"(?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getTipo());

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
	public void update(Transportadora obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE Transportadora " +
				"SET Nome_Transportadora = ?, tipo = ? " +
				"WHERE id = ?");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getTipo());
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
				"DELETE FROM Transportadora WHERE Id = ?");

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

}
