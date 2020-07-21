package model.dao;

import db.DB;
import model.dao.impl.ControlePorDaoJDBC;
import model.dao.impl.CopDaoJDBC;
import model.dao.impl.ExpedicaoDaoJDBC;
import model.dao.impl.FuncionarioDaoJDBC;
import model.dao.impl.MotoristaDaoJDBC;
import model.dao.impl.PorteiroDaoJDBC;
import model.dao.impl.SetorDaoJDBC;
import model.dao.impl.TaxiDaoJDBC;
import model.dao.impl.TransferenciaDaoJDBC;
import model.dao.impl.TransportadoraDaoJDBC;

public class DaoFactory {

	
	
	public static ControlePorDao createControlePorDao() {
		return new ControlePorDaoJDBC(DB.getConnection());
	}
	public static PorteiroDao createPorteiroDao() {
		return new PorteiroDaoJDBC(DB.getConnection());
	}
	public static TransportadoraDao createTransportadoraDao() {
		return new TransportadoraDaoJDBC(DB.getConnection());
	}
	
	public static PorteiroDao CreatePorteiroDao() {
		return new PorteiroDaoJDBC(DB.getConnection());
	}
	
	public static SetorDao CreateSetorDao() {
		return new SetorDaoJDBC(DB.getConnection());
	}

	public static MotoristaDao CreateMotoristaDao() {
		return new MotoristaDaoJDBC(DB.getConnection());
	}
	
	public static FuncionarioDao CreateFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());
	}
	
	public static CopDao CreateCopDao() {
		return new CopDaoJDBC(DB.getConnection());
	}
	public static TransferenciaDao CreateTransferenciaDao() {
		return new  TransferenciaDaoJDBC(DB.getConnection());
	}
	public static ExpedicaoDao CreateExpedicaoDao() {
		return new  ExpedicaoDaoJDBC(DB.getConnection());
	}
	public static TaxiDao CreateTaxiDao() {
		return new TaxiDaoJDBC(DB.getConnection());
	}
}
