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
import model.dao.ExpedicaoDao;
import model.entities.Expedicao;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Transportadora;

public class ExpedicaoDaoJDBC implements ExpedicaoDao {

	private Connection conn;

	public ExpedicaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Expedicao findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "Expedicao INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ "where NumeroNota = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Porteiro porteiro = instantiatePorteiro(rs);
				Transportadora transportadora = instantiateTransportadora(rs);
				Motorista motorista = instantiateMotorista(rs, transportadora);
				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
	public void insert(Expedicao obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO Expedicao "
					+ "(Data, H_Chegada, H_Entrada, H_saida, NumeroCarga, Tipo_Carga, Destino, "
					+ "NumeroBox, NumeroPecas, Expedicao_porteiro, Expedicao_motorista) " + "VALUES "
					+ "(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getData().getTime()));
			st.setTime(2, obj.getHoraChegada());
			st.setTime(3, obj.getHoraEntrada());
			st.setTime(4, obj.getHoraSaida());
			st.setInt(5, obj.getNumeroCarga());
			st.setString(6, obj.getTipoCarga());
			st.setString(7, obj.getDestino());
			st.setInt(8, obj.getNumeroBox());
			st.setInt(9, obj.getNumeroPecas());
			st.setInt(10, obj.getPorteiro().getId());
			st.setInt(11, obj.getMotorista().getId());

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
	public void update(Expedicao obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Expedicao "
					+ "SET Data = ?, H_Chegada = ?, H_Entrada = ?, H_saida = ?, NumeroCarga = ?, Tipo_Carga = ?, Destino = ?, "
					+ "NumeroBox = ?, NumeroPecas =?, Expedicao_porteiro = ?, Expedicao_motorista = ? "
					+ " WHERE Id  = ?");

			st.setDate(1, new java.sql.Date(obj.getData().getTime()));
			st.setTime(2, obj.getHoraChegada());
			st.setTime(3, obj.getHoraEntrada());
			st.setTime(4, obj.getHoraSaida());
			st.setInt(5, obj.getNumeroCarga());
			st.setString(6, obj.getTipoCarga());
			st.setString(7, obj.getDestino());
			st.setInt(8, obj.getNumeroBox());
			st.setInt(9, obj.getNumeroPecas());
			st.setInt(10, obj.getPorteiro().getId());
			st.setInt(11, obj.getMotorista().getId());
			st.setInt(12, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM Expedicao WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	private Motorista instantiateMotorista(ResultSet rs, Transportadora transportadora) throws SQLException {
		Motorista motorista = new Motorista();
		motorista.setId(rs.getInt("Expedicao_Motorista"));
		motorista.setNome(rs.getString("Motorista.nome_motorista"));
		motorista.setPlaca(rs.getString("motorista.placa"));
		motorista.setTelefone(rs.getString("Motorista.telefone"));
		motorista.setCpf(rs.getString("motorista.CPF"));
		motorista.setTransportadora(transportadora);
		return motorista;
	}

	private Porteiro instantiatePorteiro(ResultSet rs) throws SQLException {
		Porteiro porteiro = new Porteiro();
		porteiro.setId(rs.getInt("Expedicao_porteiro"));
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

	private Expedicao instantiateExpedicao(ResultSet rs, Porteiro porteiro, Motorista motorista)
			throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = rs.getDate("data");
		String format = sdf.format(date);

		Expedicao obj = new Expedicao();
		obj.setId(rs.getInt("Id"));
		obj.setData(rs.getDate("data"));
		obj.setDataFormatada(format);
		obj.setHoraChegada(rs.getTime("h_chegada"));
		obj.setHoraEntrada(rs.getTime("H_Entrada"));
		obj.setHoraSaida(rs.getTime("H_Saida"));
		obj.setNumeroCarga(rs.getInt("NumeroCarga"));
		obj.setTipoCarga(rs.getString("tipo_Carga"));
		obj.setDestino(rs.getString("Destino"));
		obj.setNumeroBox(rs.getInt("NumeroBox"));
		obj.setNumeroPecas(rs.getInt("NumeroPecas"));
		obj.setPorteiro(porteiro);
		obj.setMotorista(motorista);
		return obj;
	}

	@Override
	public List<Expedicao> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "Expedicao INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ "order by  Expedicao.id desc");
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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

	public List<Expedicao> findPerToday() {
		PreparedStatement st = null;
		ResultSet rs = null;
		java.util.Date dataUtil = new java.util.Date();
		java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ "where Expedicao.Data = ? " + "order by  Expedicao.id desc");
			st.setDate(1, dataSql);
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
	public List<Expedicao> findPerDate(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Date date = Utils.TryParseToDate(data);

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ "WHERE Expedicao.data = ? "
					+ "order by  Expedicao.id desc");
			st.setDate(1, new java.sql.Date(date.getTime())); 
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
	public List<Expedicao> findPerMonth(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ " WHERE MONTH(Expedicao.data) = ? AND YEAR(Expedicao.data) = ? "
					+ "order by  Expedicao.id desc");
			st.setString(1, data.substring(0,2));
			st.setString(2, data.substring(3,7));
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
	public List<Expedicao> findPerYear(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ " WHERE YEAR(Expedicao.data) = ? "
					+ "order by  Expedicao.id desc");
			st.setString(1, data);
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
	public List<Expedicao> findByMotorista(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ " WHERE motorista.nome_motorista = ? "
					+ "order by  Expedicao.id desc");
			st.setString(1, nome);
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
	public List<Expedicao> findByTransportadora(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select Expedicao.*, transportadora.*, motorista.*,Porteiro.* from Expedicao "
					+ "INNER JOIN motorista ON Expedicao.Expedicao_motorista = motorista.id "
					+ "INNER JOIN porteiro ON Expedicao.expedicao_porteiro = porteiro.id "
					+ "INNER JOIN transportadora on Motorista.FK_transportadora = transportadora.id "
					+ " WHERE Transportadora.nome_Transportadora = ? "
					+ "order by  Expedicao.id desc");
			st.setString(1, nome);
			rs = st.executeQuery();

			List<Expedicao> list = new ArrayList<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("Motorista.FK_transportadora"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Expedicao.Expedicao_porteiro"));
				Motorista motorista = mapMotorista.get(rs.getInt("Expedicao.Expedicao_motorista"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("Motorista.FK_transportadora"), transportadora);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Expedicao.Expedicao_porteiro"), porteiro);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Expedicao.Expedicao_motorista"), motorista);
				}

				Expedicao obj = instantiateExpedicao(rs, porteiro, motorista);

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
