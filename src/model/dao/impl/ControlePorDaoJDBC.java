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
import model.dao.ControlePorDao;
import model.entities.ControlePor;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Setor;
import model.entities.Transportadora;

public class ControlePorDaoJDBC implements ControlePorDao {

	private Connection conn;

	public ControlePorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public ControlePor findByNumeroNota(Integer NumeroNota) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select controlepor.*, transportadora.*, motorista.*, " + "setor.* , porteiro.* from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ "where NumeroNota = ?");
			st.setInt(1, NumeroNota);
			rs = st.executeQuery();
			if (rs.next()) {
				Porteiro porteiro = instantiatePorteiro(rs);
				Transportadora transportadora = instantiateTransportadora(rs);
				Setor setor = instantiateSetor(rs);
				Motorista motorista = instantiateMotorista(rs, transportadora);
				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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
	public ControlePor findById(Integer Id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select controlepor.*, transportadora.Nome_Transportadora, motorista.nome_motorista,"
							+ "setor.nome_Setor, porteiro.nome_porteiro, motorista.placa from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ "where Id = ?");
			st.setInt(1, Id);
			rs = st.executeQuery();
			if (rs.next()) {
				Porteiro porteiro = instantiatePorteiro(rs);
				Transportadora transportadora = instantiateTransportadora(rs);
				Setor setor = instantiateSetor(rs);
				Motorista motorista = instantiateMotorista(rs, transportadora);
				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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
	public void insert(ControlePor obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO ControlePor "
					+ "(numeronota, Data, HoraEntrada, Horasaida, FK_porteiro, FK_motorista, FK_setor) " + "VALUES "
					+ "(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNumeroNota());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, new java.sql.Time(obj.getHoraEntrada().getTime()));
			st.setTime(4, new java.sql.Time(obj.getHoraSaida().getTime()));
			st.setInt(5, obj.getPorteiro().getId());
			st.setInt(6, obj.getMotorista().getId());
			st.setInt(7, obj.getSetor().getId());

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
	public void update(ControlePor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE ControlePor "
					+ "SET numeroNota = ?, Data = ?, horaEntrada = ?, horaSaida = ?, FK_motorista = ?, FK_porteiro = ?, FK_Setor = ? "
					+ " WHERE Id  = ?");

			st.setInt(1, obj.getNumeroNota());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, new java.sql.Time(obj.getHoraEntrada().getTime()));
			st.setTime(4, new java.sql.Time(obj.getHoraSaida().getTime()));
			st.setInt(5, obj.getMotorista().getId());
			st.setInt(6, obj.getPorteiro().getId());
			st.setInt(7, obj.getSetor().getId());
			st.setInt(8, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteByNota(Integer nota) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM ControlePor WHERE NumeroNota = ?");

			st.setInt(1, nota);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	private Motorista instantiateMotorista(ResultSet rs, Transportadora transportadora) throws SQLException {
		Motorista motorista = new Motorista();
		motorista.setId(rs.getInt("FK_Motorista"));
		motorista.setNome(rs.getString("Motorista.nome_motorista"));
		motorista.setPlaca(rs.getString("motorista.placa"));
		motorista.setTelefone(rs.getString("Motorista.telefone"));
		motorista.setCpf(rs.getString("motorista.CPF"));
		motorista.setTransportadora(transportadora);
		return motorista;
	}

	private Porteiro instantiatePorteiro(ResultSet rs) throws SQLException {
		Porteiro porteiro = new Porteiro();
		porteiro.setId(rs.getInt("FK_porteiro"));
		porteiro.setNome(rs.getString("Porteiro.nome_porteiro"));
		return porteiro;
	}

	private Transportadora instantiateTransportadora(ResultSet rs) throws SQLException {
		Transportadora transportadora = new Transportadora();
		transportadora.setId(rs.getInt("FK_Transportadora"));
		transportadora.setNome(rs.getString("Transportadora.Nome_Transportadora"));
		transportadora.setTipo(rs.getString("Transportadora.tipo"));
		return transportadora;
	}

	private Setor instantiateSetor(ResultSet rs) throws SQLException {
		Setor setor = new Setor();
		setor.setNome(rs.getString("setor.nome_Setor"));
		setor.setId(rs.getInt("FK_Setor"));
		return setor;
	}

	private ControlePor instantiateControlepor(ResultSet rs, Porteiro porteiro, Motorista motorista, Setor setor)
			throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = rs.getDate("data");
		String format = sdf.format(date);

		ControlePor obj = new ControlePor();
		obj.setId(rs.getInt("Id"));
		obj.setNumeroNota(rs.getInt("NumeroNota"));
		obj.setData(rs.getDate("data"));
		obj.setDataFormatada(format);
		obj.setHoraEntrada(rs.getTime("HoraEntrada"));
		obj.setHoraSaida(rs.getTime("HoraSaida"));
		obj.setSetor(setor);
		obj.setPorteiro(porteiro);
		obj.setMotorista(motorista);
		return obj;
	}

	@Override
	public List<ControlePor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select controlepor.*, motorista.*, transportadora.*, setor.*, porteiro.* from controlepor "
							+ "INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ "order by  controlepor.id desc");
			rs = st.executeQuery();

			List<ControlePor> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("controlepor.FK_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("controlepor.FK_motorista"));
				Setor setor = mapSetor.get(rs.getInt("controlepor.FK_setor"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("controlepor.FK_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("controlepor.FK_motorista"), motorista);
				}

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("controlepor.FK_setor"), setor);
				}

				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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

	public List<ControlePor> findPerToday() {
		PreparedStatement st = null;
		ResultSet rs = null;
		java.util.Date dataUtil = new java.util.Date();
		java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

		try {
			st = conn.prepareStatement(
					"select controlepor.*, motorista.*, transportadora.*, setor.*, porteiro.* from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ "Where controlePor.Data = ? " + "order by  controlepor.id desc");
			st.setDate(1, dataSql);
			rs = st.executeQuery();

			List<ControlePor> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("controlepor.FK_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("controlepor.FK_motorista"));
				Setor setor = mapSetor.get(rs.getInt("controlepor.FK_setor"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("controlepor.FK_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("controlepor.FK_motorista"), motorista);
				}

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("controlepor.FK_setor"), setor);
				}

				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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

	public List<ControlePor> findPerMonth(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select controlepor.*, motorista.*, transportadora.*, setor.*, porteiro.* from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ " WHERE MONTH(controlepor.data) = ? AND YEAR(controlepor.data) = ? "
							+ "order by  controlepor.id desc");
			st.setString(1, data.substring(0, 2));
			st.setString(2, data.substring(3, 7));

			rs = st.executeQuery();

			List<ControlePor> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("controlepor.FK_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("controlepor.FK_motorista"));
				Setor setor = mapSetor.get(rs.getInt("controlepor.FK_setor"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("controlepor.FK_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("controlepor.FK_motorista"), motorista);
				}

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("controlepor.FK_setor"), setor);
				}

				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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

	public List<ControlePor> findPerYear(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select controlepor.*, motorista.*, transportadora.*, " + "setor.* , porteiro.* from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ " WHERE YEAR(controlepor.data) = ? " 
							+ "order by  controlepor.id desc");
			st.setString(1, data);
			rs = st.executeQuery();

			List<ControlePor> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("controlepor.FK_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("controlepor.FK_motorista"));
				Setor setor = mapSetor.get(rs.getInt("controlepor.FK_setor"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("controlepor.FK_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("controlepor.FK_motorista"), motorista);
				}

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("controlepor.FK_setor"), setor);
				}

				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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

	public List<ControlePor> findPerDate(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Date date = Utils.TryParseToDate(data);

		try {
			st = conn.prepareStatement(
					"select controlepor.*, motorista.*, transportadora.*, " + "setor.* , porteiro.* from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ " WHERE controlepor.data = ? " + "order by  controlepor.id desc");
			st.setDate(1, new java.sql.Date(date.getTime()));
			rs = st.executeQuery();

			List<ControlePor> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("controlepor.FK_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("controlepor.FK_motorista"));
				Setor setor = mapSetor.get(rs.getInt("controlepor.FK_setor"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("controlepor.FK_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("controlepor.FK_motorista"), motorista);
				}

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("controlepor.FK_setor"), setor);
				}

				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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

	public List<ControlePor> findByMotorista(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select controlepor.*, motorista.*, transportadora.*, " + "setor.* , porteiro.* from "
							+ "controlepor INNER JOIN motorista ON controlepor.FK_motorista = motorista.id "
							+ "INNER JOIN setor ON controlepor.FK_setor = setor.id "
							+ "INNER JOIN porteiro ON controlepor.FK_porteiro = porteiro.id "
							+ "inner join transportadora on Motorista.FK_transportadora = transportadora.id "
							+ " WHERE motorista.nome_motorista = ?  " + "order by  controlepor.id desc");
			st.setString(1, nome);
			rs = st.executeQuery();

			List<ControlePor> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Setor> mapSetor = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("controlepor.FK_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("controlepor.FK_motorista"));
				Setor setor = mapSetor.get(rs.getInt("controlepor.FK_setor"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("controlepor.FK_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("controlepor.FK_motorista"), motorista);
				}

				if (setor == null) {
					setor = instantiateSetor(rs);
					mapSetor.put(rs.getInt("controlepor.FK_setor"), setor);
				}

				ControlePor obj = instantiateControlepor(rs, porteiro, motorista, setor);

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
