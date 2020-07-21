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
import model.dao.TransferenciaDao;
import model.entities.Motorista;
import model.entities.Porteiro;
import model.entities.Transferencia;
import model.entities.Transportadora;

public class TransferenciaDaoJDBC implements TransferenciaDao {

	private Connection conn;

	public TransferenciaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Transferencia findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ "where Transferencia.id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Transportadora transportadora = instantiateTransportadora(rs);
				Porteiro porteiro = instantiatePorteiro(rs);
				Motorista motorista = instantiateMotorista(rs, transportadora);
				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
	public void insert(Transferencia obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"INSERT INTO Transferencia "
							+ "(numero, Data, hora_Chegada, Numero_Nota_Fiscal, Data_Descarregamento, destino, "
							+ "Transferencia_Motorista, Transferencia_porteiro) " 
							+ " values (?,?,?,?,?,?,?,?) ",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNumero());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, obj.getHoraChegada());
			st.setInt(4, obj.getNumeroNotaFiscal());
			st.setDate(5, new java.sql.Date(obj.getDataDescarregamento().getTime()));
			st.setString(6, obj.getDestino());
			st.setInt(7, obj.getMotorista().getId());
			st.setInt(8, obj.getPorteiro().getId());

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
	public void update(Transferencia obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			   "UPDATE Transferencia " 
			    + "SET numero = ?, Data = ?, hora_Chegada = ?, Numero_Nota_Fiscal = ?, "
				+ "Data_Descarregamento = ?, destino = ?, Transferencia_Motorista = ?, Transferencia_porteiro = ? " 
			    + "WHERE Id  = ?");

			st.setInt(1, obj.getNumero());
			st.setDate(2, new java.sql.Date(obj.getData().getTime()));
			st.setTime(3, obj.getHoraChegada());
			st.setInt(4, obj.getNumeroNotaFiscal());
			st.setDate(5, new java.sql.Date(obj.getDataDescarregamento().getTime()));
			st.setString(6, obj.getDestino());
			st.setInt(7, obj.getMotorista().getId());
			st.setInt(8, obj.getPorteiro().getId());
			st.setInt(9, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM Transferencia WHERE Id = ?");

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
		motorista.setId(rs.getInt("Transferencia_Motorista"));
		motorista.setNome(rs.getString("Motorista.nome_motorista"));
		motorista.setPlaca(rs.getString("motorista.placa"));
		motorista.setCpf(rs.getString("motorista.CPF"));
		motorista.setTransportadora(transportadora);
		return motorista;
	}

	private Porteiro instantiatePorteiro(ResultSet rs) throws SQLException {
		Porteiro porteiro = new Porteiro();
		porteiro.setId(rs.getInt("Transferencia_porteiro"));
		porteiro.setNome(rs.getString("Porteiro.nome_porteiro"));
		return porteiro;
	}

	private Transportadora instantiateTransportadora(ResultSet rs) throws SQLException {
		Transportadora transportadora = new Transportadora();
		transportadora.setId(rs.getInt("FK_Transportadora"));
		transportadora.setTipo(rs.getString("Transportadora.tipo"));
		transportadora.setNome(rs.getString("Transportadora.Nome_Transportadora"));
		return transportadora;
	}

	private Transferencia instantiateTransferencia(ResultSet rs, Motorista motorista, Porteiro porteiro)
			throws SQLException, ParseException {
		Transferencia obj = new Transferencia();
		SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date = rs.getDate("data");
		String formatData = data.format(date);
		date = rs.getDate("data_descarregamento");
		String formataDataDes = data.format(date);
		obj.setId(rs.getInt("Transferencia.id"));
		obj.setNumero(rs.getInt("Transferencia.Numero"));
		obj.setNumeroNotaFiscal(rs.getInt("Numero_Nota_Fiscal"));
		obj.setHoraChegada(rs.getTime("Hora_chegada"));
		obj.setData(rs.getDate("data"));
		obj.setDestino(rs.getString("destino"));
		obj.setDataDescarregamento(rs.getDate("data_descarregamento"));
		obj.setDataDescarregamentoFormatada(formataDataDes);
		obj.setDataFormatada(formatData);
		obj.setMotorista(motorista);
		obj.setPorteiro(porteiro);
		return obj;
	}

	@Override
	public List<Transferencia> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ "order by  Transferencia.id desc");
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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

	public List<Transferencia> findPerToday() {
		PreparedStatement st = null;
		ResultSet rs = null;
		java.util.Date dataUtil = new java.util.Date();
		java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ "where Transferencia.Data = ? "
							+ "order by  Transferencia.id desc");
			st.setDate(1, dataSql);
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
	public List<Transferencia> findPerDate(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Date date = Utils.TryParseToDate(data);

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ "WHERE Transferencia.data = ? " 
							+ "order by  Transferencia.id desc");
			st.setDate(1, new java.sql.Date(date.getTime()));
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
	public List<Transferencia> findPerMonth(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ " WHERE MONTH(Transferencia.data) = ? AND YEAR(Transferencia.data) = ? "
							+ "order by  Transferencia.id desc");
			st.setString(1, data.substring(0, 2));
			st.setString(2, data.substring(3, 7));
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
	public List<Transferencia> findPerYear(String data) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ " WHERE YEAR(Transferencia.data) = ? " 
							+ "order by  Transferencia.id desc");
			st.setString(1, data);
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
	public List<Transferencia> findByMotorista(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ " WHERE motorista.nome_motorista = ? " 
							+ "order by  Transferencia.id desc");
			st.setString(1, nome);
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
	public List<Transferencia> findByNotaFical(Integer notaFical) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select Transferencia.*,motorista.*,porteiro.*,Transportadora.* from Transferencia  "
							+ "inner join Motorista on Transferencia.Transferencia_Motorista = Motorista.id "
							+ "inner join Porteiro on Transferencia.Transferencia_porteiro =  porteiro.id "
							+ "inner join Transportadora on motorista.Fk_Transportadora = Transportadora.id "
							+ " WHERE Transferencia.Numero_Nota_Fiscal = ? " 
							+ "order by  Transferencia.id desc");
			st.setInt(1, notaFical);
			rs = st.executeQuery();

			List<Transferencia> list = new ArrayList<>();
			Map<Integer, Motorista> mapMotorista = new HashMap<>();
			Map<Integer, Transportadora> mapTransportadora = new HashMap<>();
			Map<Integer, Porteiro> mapPorteiro = new HashMap<>();

			while (rs.next()) {
				Transportadora transportadora = mapTransportadora.get(rs.getInt("FK_Transportadora"));
				Motorista motorista = mapMotorista.get(rs.getInt("Transferencia_Motorista"));
				Porteiro porteiro = mapPorteiro.get(rs.getInt("Transferencia_porteiro"));

				if (transportadora == null) {
					transportadora = instantiateTransportadora(rs);
					mapTransportadora.put(rs.getInt("FK_Transportadora"), transportadora);
				}
				if (motorista == null) {
					motorista = instantiateMotorista(rs, transportadora);
					mapMotorista.put(rs.getInt("Transferencia_motorista"), motorista);
				}
				if (porteiro == null) {
					porteiro = instantiatePorteiro(rs);
					mapPorteiro.put(rs.getInt("Transferencia_porteiro"), porteiro);
				}

				Transferencia obj = instantiateTransferencia(rs, motorista, porteiro);

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
