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
import model.dao.PorteiroDao;
import model.entities.Porteiro;

public class PorteiroDaoJDBC implements PorteiroDao {
	private Connection conn;

	public PorteiroDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Porteiro findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM Porteiro WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Porteiro obj = new Porteiro();
				obj.setId(rs.getInt("id"));
				obj.setNome(rs.getString("Nome_Porteiro"));
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
	
	@Override
	public Porteiro findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Porteiro WHERE Nome_Porteiro = ?");
			st.setString(1, nome);
			rs = st.executeQuery();
			if (rs.next()) {
				Porteiro obj = new Porteiro();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Porteiro"));
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
	public List<Porteiro> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM Porteiro ORDER BY Nome_Porteiro");
			rs = st.executeQuery();

			List<Porteiro> list = new ArrayList<>();

			while (rs.next()) {
				Porteiro obj = new Porteiro();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Porteiro"));
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
	public void insert(Porteiro obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO Porteiro " 
		    + "(Nome_Porteiro) " 
			+ "VALUES " 
			+ "(?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());


			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Porteiro obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Porteiro " + "SET Nome_Porteiro = ? " + "WHERE id = ?");

			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM porteiro WHERE id = ?");

			st.setInt(1,id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

}
