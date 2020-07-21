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
import model.dao.CopDao;
import model.entities.Cop;
import model.entities.Funcionario;
import model.entities.Porteiro;
import model.entities.Setor;

public class CopDaoJDBC implements CopDao {

	private Connection conn;

	public CopDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Cop findByid(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*,porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "inner join Porteiro on cop.cop_porteiro =  porteiro.id " + "where cop.id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Setor setor = instantiateSetor(rs);
				Funcionario funcionario = instantiateFuncionario(rs, setor);
				Porteiro porteiro = instantiatePorteiro(rs);
				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
	public void insert(Cop obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"INSERT INTO Cop " + "(numero_cop, Data, Hora, acesso, motivo, cop_funcionario, cop_porteiro) "
							+ " values (?,?,?,?,?,?,?) ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNumeroCop());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, obj.getHora());
			st.setString(4, obj.getAcesso());
			st.setString(5, obj.getMotivo());
			st.setInt(6, obj.getFuncionario().getId());
			st.setInt(7, obj.getPorteiro().getId());
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
	public void update(Cop obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Cop "
					+ " SET numero_cop = ?, Data = ?, hora = ?, acesso = ?, motivo = ?, cop_funcionario = ?, cop_porteiro = ? "
					+ " WHERE Id  = ?");

			st.setInt(1, obj.getNumeroCop());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, obj.getHora());
			st.setString(4, obj.getAcesso());
			st.setString(5, obj.getMotivo());
			st.setInt(6, obj.getFuncionario().getId());
			st.setInt(7, obj.getPorteiro().getId());
			st.setInt(8, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM Cop WHERE Id = ?");

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
		Funcionario.setId(rs.getInt("Cop_funcionario"));
		Funcionario.setNome(rs.getString("Funcionario.nome"));
		Funcionario.setMatricula(rs.getInt("funcionario.matricula"));
		Funcionario.setSetor(setor);
		return Funcionario;
	}

	private Porteiro instantiatePorteiro(ResultSet rs) throws SQLException {
		Porteiro porteiro = new Porteiro();
		porteiro.setId(rs.getInt("cop_porteiro"));
		porteiro.setNome(rs.getString("Porteiro.nome_porteiro"));
		return porteiro;
	}

	private Cop instantiateCop(ResultSet rs, Funcionario funcionario, Porteiro porteiro)
			throws SQLException, ParseException {
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = rs.getDate("data");
		String formatData = data.format(date);
		Cop obj = new Cop();
		obj.setId(rs.getInt("cop.Id"));
		obj.setNumeroCop(rs.getInt("Numero_cop"));
		obj.setData(rs.getDate("data"));
		obj.setHora(rs.getTime("hora"));
		obj.setAcesso(rs.getString("acesso"));
		obj.setMotivo(rs.getString("motivo"));
		obj.setDataFormatada(formatData);
		obj.setFuncionario(funcionario);
		obj.setPorteiro(porteiro);
		return obj;
	}

	@Override
	public List<Cop> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id "
					+ "order by  Cop.id desc");
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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

	public List<Cop> findPerToday() {
		PreparedStatement st = null;
		ResultSet rs = null;
		java.util.Date dataUtil = new java.util.Date();
		java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, Porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id " 
					+ "where Cop.Data = ?  "
					+ "order by  Cop.id desc");
			st.setDate(1, dataSql);
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			while (rs.next()) {
				Setor Setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (Setor == null) {
					Setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), Setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, Setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
	public List<Cop> findPerDate(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Date date = Utils.TryParseToDate(data);

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id " 
					+ "WHERE Cop.data = ? "
					+ "order by  Cop.id desc");
			st.setDate(1, new java.sql.Date(date.getTime()));
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
	public List<Cop> findPerMonth(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id "
					+ " WHERE MONTH(Cop.data) = ? AND YEAR(Cop.data) = ? " 
					+ "order by  Cop.id desc");
			st.setString(1, data.substring(0, 2));
			st.setString(2, data.substring(3, 7));
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
	public List<Cop> findPerYear(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id " 
					+ " WHERE YEAR(Cop.data) = ? "
					+ "order by  Cop.id desc");
			st.setString(1, data);
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
	public List<Cop> findBySetor(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id "
					+ " WHERE Setor.Nome_setor = ? "
					+ "order by  Cop.id desc");
			st.setString(1, nome);
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
	public List<Cop> findByMatricula(Integer matricula) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select cop.*, funcionario.*,setor.*, porteiro.* from cop "
					+ "inner join funcionario on cop.cop_funcionario = funcionario.id "
					+ "inner join setor on funcionario_setor = setor.id "
					+ "INNER JOIN Porteiro on cop.cop_porteiro =  porteiro.id " 
					+ " WHERE Funcionario.Matricula = ? "
					+ "order by  Cop.id desc");
			st.setInt(1, matricula);
			rs = st.executeQuery();

			List<Cop> list = new ArrayList<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Setor setor = mapSetor.get(rs.getInt("Funcionario_Setor"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("Cop_Funcionario"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("cop_porteiro"));

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("Funcionario_Setor"), setor);
				}
				if (funcionario == null) {
					funcionario = instantiateFuncionario(rs, setor);
					mapFuncionario.put(rs.getInt("Cop_Funcionario"), funcionario);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("cop_porteiro"), porteiro);
				}

				Cop obj = instantiateCop(rs, funcionario, porteiro);

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
