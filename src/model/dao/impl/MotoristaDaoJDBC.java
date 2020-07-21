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
import model.dao.MotoristaDao;
import model.entities.Motorista;
import model.entities.Transportadora;

public class MotoristaDaoJDBC implements MotoristaDao {
	
private Connection conn;
	
	public MotoristaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public Motorista findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Motorista WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Motorista obj = new Motorista();
				Transportadora transportadora = new Transportadora();
				transportadora.setId(rs.getInt("FK_Transportadora"));
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome_Motorista"));
				obj.setPlaca(rs.getString("Placa"));
				obj.setTelefone(rs.getString("Telefone"));
				obj.setCpf(rs.getString("CPF"));
				obj.setTransportadora(transportadora);
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
	public Motorista findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Motorista where Nome_Motorista = ?");
			st.setString(1, nome);
			rs = st.executeQuery();
			if (rs.next()) {
				Motorista obj = new Motorista();
				Transportadora transportadora = new Transportadora();
				transportadora.setId(rs.getInt("FK_Transportadora"));
				obj.setId(rs.getInt("Id"));
				obj.setPlaca(rs.getString("placa"));
				obj.setNome(rs.getString("Nome_Motorista"));
				obj.setTelefone(rs.getString("Telefone"));
				obj.setCpf(rs.getString("CPF"));
				obj.setTransportadora(transportadora);
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
	public Motorista findByCPF(String cpf) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM Motorista where CPF = ?");
			st.setString(1, cpf);
			rs = st.executeQuery();
			if (rs.next()) {
				Motorista obj = new Motorista();
				Transportadora transportadora = new Transportadora();
				transportadora.setId(rs.getInt("FK_Transportadora"));
				obj.setId(rs.getInt("Id"));
				obj.setPlaca(rs.getString("placa"));
				obj.setNome(rs.getString("Nome_Motorista"));
				obj.setTelefone(rs.getString("Telefone"));
				obj.setCpf(rs.getString("CPF"));
				obj.setTransportadora(transportadora);
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
	private Transportadora instantiateTransportadora(ResultSet rs) throws SQLException {
		Transportadora transportadora = new Transportadora();
		transportadora.setId(rs.getInt("FK_Transportadora"));
		transportadora.setNome(rs.getString("Transportadora.Nome_Transportadora"));
		transportadora.setTipo(rs.getString("Transportadora.tipo"));
		return transportadora;
	}
	private Motorista instantiateMotorista(ResultSet rs, Transportadora transportadora) throws SQLException {
		Motorista motorista = new Motorista();
		motorista.setId(rs.getInt("Motorista.Id"));
		motorista.setNome(rs.getString("Motorista.nome_motorista"));
		motorista.setPlaca(rs.getString("motorista.placa"));
		motorista.setTelefone(rs.getString("Telefone"));
		motorista.setCpf(rs.getString("motorista.CPF"));
		motorista.setTransportadora(transportadora);
		return motorista;
	}

	@Override
	public List<Motorista> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT motorista.*,transportadora.* from motorista "
			    + "INNER JOIN transportadora ON motorista.FK_transportadora = transportadora.id "
			    + "ORDER BY Transportadora.nome_transportadora");
			rs = st.executeQuery();
			List<Motorista> list = new ArrayList<>();
			Map<Integer,Transportadora > mapTransportadora  = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}

				Motorista obj = instantiateMotorista(rs, transportadora);
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
	public void insert(Motorista obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO Motorista " +
				"(Nome_Motorista, placa, Telefone, CPF, FK_transportadora) " +
				"VALUES " +
				"(?,?,?,?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getPlaca());
			st.setString(3, obj.getTelefone());
			st.setString(4, obj.getCpf());
			st.setInt(5, obj.getTransportadora().getId());

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
	public void update(Motorista obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE Motorista " +
				"SET Nome_Motorista = ?,placa =?, telefone = ?, CPF = ?, FK_transportadora = ? " +
				"WHERE ID = ?");

			st.setString(1, obj.getNome());
			st.setString(2, obj.getPlaca());
			st.setString(3, obj.getTelefone());
			st.setString(4, obj.getCpf());
			st.setInt(5, obj.getTransportadora().getId());
			st.setInt(6, obj.getId());

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
				"DELETE FROM Motorista WHERE ID = ?");

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
