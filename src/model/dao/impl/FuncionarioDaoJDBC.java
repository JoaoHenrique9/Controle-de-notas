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
import db.DbIntegrityException;
import model.dao.FuncionarioDao;
import model.entities.Funcionario;
import model.entities.Setor;

public class FuncionarioDaoJDBC implements FuncionarioDao {

private Connection conn;
	
	public FuncionarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	//falta corrigir 
	@Override
	public Funcionario findByMatricula(Integer matricula) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"select Funcionario.*, Setor.* from funcionario "
				+ "INNER JOIN setor on funcionario.funcionario_setor = setor.id "
				+ "WHERE matricula = ?");
			st.setInt(1, matricula);
			rs = st.executeQuery();
			if (rs.next()) {
				Setor setor = instantiateSetor(rs);
				Funcionario obj = instantiateFuncionario(rs,setor);
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
	private Setor instantiateSetor(ResultSet rs) throws SQLException {
		Setor setor = new Setor();
		setor.setNome(rs.getString("nome_Setor"));
		setor.setId(rs.getInt("funcionario_Setor"));
		setor.setSupervisor(rs.getString("Supervisor"));
		return setor;
	}
	
	private Funcionario instantiateFuncionario(ResultSet rs, Setor setor) throws SQLException {
		Funcionario Funcionario = new Funcionario();
		Funcionario.setId(rs.getInt("funcionario.id"));
		Funcionario.setNome(rs.getString("Funcionario.nome"));
		Funcionario.setMatricula(rs.getInt("funcionario.Matricula"));
		Funcionario.setSetor(setor);
		return Funcionario;
	}
	@Override
	public List<Funcionario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"Select Funcionario.*, Setor.* from funcionario "
				+ "INNER JOIN setor on funcionario.funcionario_setor = setor.id");
			rs = st.executeQuery();

			List<Funcionario> list = new ArrayList<>();
			Map<Integer,Setor > mapSetor = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				
				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				Funcionario obj = instantiateFuncionario(rs,setor);
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
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO funcionario " +
				"(Nome, matricula, Funcionario_setor) " +
				"VALUES " +
				"(?,?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setInt(2, obj.getMatricula());
			st.setInt(3, obj.getSetor().getId());

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
	public void update(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE funcionario " +
				" SET Nome = ?,matricula = ?, Funcionario_setor = ? " +
				" WHERE Id = ?");

			st.setString(1, obj.getNome());
			st.setInt(2, obj.getMatricula());
			st.setInt(3, obj.getSetor().getId());
			st.setInt(4, obj.getId());

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
	public void deleteByMatricula(Integer matricula) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM funcionario WHERE matricula = ?");

			st.setInt(1, matricula);

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
