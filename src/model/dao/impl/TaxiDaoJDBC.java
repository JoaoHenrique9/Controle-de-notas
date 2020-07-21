package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import gui.util.Utils;
import model.dao.TaxiDao;
import model.entities.Funcionario;
import model.entities.Porteiro;
import model.entities.Setor;
import model.entities.Taxi;

public class TaxiDaoJDBC implements TaxiDao {

	private Connection conn;

	public TaxiDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Taxi findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Taxi.*, funcionario.*,setor.*,porteiro.* from Taxi "
					+ "INNER JOIN funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "INNER JOIN setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id " 
					+ "WHERE Taxi.id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Setor setor = instantiateSetor(rs);
				Funcionario funcionario = instantiateFuncionario(rs, setor);
				Porteiro porteiro = instantiatePorteiro(rs);
				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void insert(Taxi obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO Taxi "
					+ "(numeroVale, Data, Hora, Valor, Taxa, Motivo, Trajeto, Taxi_funcionario, Taxi_porteiro) "
					+ " VAlUES (?,?,?,?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNumeroVale());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, obj.getHora());
			st.setDouble(4, obj.getValor());
			st.setDouble(5, obj.getTaxa());
			st.setString(6, obj.getMotivo());
			st.setString(7, obj.getTrajeto());
			st.setInt(8, obj.getFuncionario().getId());
			st.setInt(9, obj.getPorteiro().getId());
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
	public void update(Taxi obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Taxi "
					+ " SET numeroVale = ?, Data = ?, Hora = ?, Valor = ?, Taxa = ?, Motivo = ?, Trajeto = ?, Taxi_funcionario = ?, Taxi_porteiro = ? "
					+ " WHERE Id  = ?");

			st.setInt(1, obj.getNumeroVale());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, obj.getHora());
			st.setDouble(4, obj.getValor());
			st.setDouble(5, obj.getTaxa());
			st.setString(6, obj.getMotivo());
			st.setString(7, obj.getTrajeto());
			st.setInt(8, obj.getFuncionario().getId());
			st.setInt(9, obj.getPorteiro().getId());
			st.setInt(10, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM Taxi WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	private Setor instantiateSetor(ResultSet rs) throws SQLException {
		Setor setor = new Setor();
		setor.setNome(rs.getString("nome_Setor"));
		setor.setId(rs.getInt("funcionario_Setor"));
		setor.setSupervisor(rs.getString("supervisor"));
		return setor;
	}

	private Funcionario instantiateFuncionario(ResultSet rs, Setor setor) throws SQLException {
		Funcionario Funcionario = new Funcionario();
		Funcionario.setId(rs.getInt("Taxi_funcionario"));
		Funcionario.setNome(rs.getString("Funcionario.nome"));
		Funcionario.setMatricula(rs.getInt("funcionario.matricula"));
		Funcionario.setSetor(setor);
		return Funcionario;
	}

	private Porteiro instantiatePorteiro(ResultSet rs) throws SQLException {
		Porteiro porteiro = new Porteiro();
		porteiro.setId(rs.getInt("Taxi_porteiro"));
		porteiro.setNome(rs.getString("Porteiro.nome_porteiro"));
		return porteiro;
	}

	private Taxi instantiateTaxi(ResultSet rs, Funcionario funcionario, Porteiro porteiro)
			throws SQLException, ParseException {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = rs.getDate("data");
		String formatData = data.format(date);
		Taxi obj = new Taxi();
		obj.setId(rs.getInt("Taxi.Id"));
		obj.setNumeroVale(rs.getInt("NumeroVale"));
		obj.setData(rs.getDate("data"));
		obj.setHora(rs.getTime("hora"));
		obj.setValor(rs.getDouble("Valor"));
		obj.setTaxa(rs.getDouble("Taxa"));
		obj.setMotivo(rs.getString("motivo"));
		obj.setTrajeto(rs.getString("Trajeto"));
		obj.setDataFormatada(formatData);
		obj.setFuncionario(funcionario);
		obj.setPorteiro(porteiro);
		return obj;
	}

	@Override
	public List<Taxi> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT Taxi.*, funcionario.*,setor.*, porteiro.* from Taxi "
					+ "INNER JOIN funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "INNER JOIN setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id "
					+ "order by  Taxi.id desc");
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	public List<Taxi> findPerToday() {
		PreparedStatement st = null;
		ResultSet rs = null;
		java.util.Date dataUtil = new java.util.Date();
		java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

		try {
			st = conn.prepareStatement("SELECT Taxi.*, funcionario.*,setor.*, Porteiro.* from Taxi "
					+ "INNER JOIN funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "INNER JOIN setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id " 
					+ "where Taxi.Data = ?  ");
			st.setDate(1, dataSql);
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			while (rs.next()) {
				Setor Setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (Setor == null) {
					Setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), Setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, Setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Taxi> findPerDate(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Date date = Utils.TryParseToDate(data);

		try {
			st = conn.prepareStatement("select Taxi.*, funcionario.*,setor.*, porteiro.* from Taxi "
					+ "inner join funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id " 
					+ "WHERE Taxi.data = ? "
					+ "order by  Taxi.id desc");
			st.setDate(1, new java.sql.Date(date.getTime()));
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Taxi> findPerMonth(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Taxi.*, funcionario.*,setor.*, porteiro.* from Taxi "
					+ "inner join funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id "
					+ " WHERE MONTH(Taxi.data) = ? AND YEAR(Taxi.data) = ? " 
					+ "order by  Taxi.id desc");
			st.setString(1, data.substring(0, 2));
			st.setString(2, data.substring(3, 7));
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	@Override
	public List<Taxi> findPerYear(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Taxi.*, funcionario.*,setor.*, porteiro.* from Taxi "
					+ "inner join funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id " 
					+ " WHERE YEAR(Taxi.data) = ? "
					+ "order by  Taxi.id desc");
			st.setString(1, data);
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Taxi> findBySetor(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Taxi.*, funcionario.*,setor.*, porteiro.* from Taxi "
					+ "inner join funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id "
					+ " WHERE Setor.Nome_setor = ? "
					+ "order by  Taxi.id desc");
			st.setString(1, nome);
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Taxi> findByMatricula(Integer matricula) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Taxi.*, funcionario.*,setor.*, porteiro.* from Taxi "
					+ "inner join funcionario on Taxi.Taxi_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on Taxi.Taxi_porteiro =  porteiro.id " 
					+ " WHERE Funcionario.Matricula = ? "
					+ "order by  Taxi.id desc");
			st.setInt(1, matricula);
			rs = st.executeQuery();

			List<Taxi> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Taxi_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Taxi_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Taxi_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Taxi_porteiro"), porteiro);
				}

				Taxi obj = instantiateTaxi(rs, funcionario, porteiro);

				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} catch (ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
