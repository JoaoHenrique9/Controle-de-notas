package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TransferenciaDao;
import model.entities.Transferencia;


public class TransferenciaService {
	
private TransferenciaDao dao = DaoFactory.CreateTransferenciaDao();
	
	public List<Transferencia> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Transferencia obj) {
		if (obj.getId()== null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Transferencia obj) {
		dao.deleteById(obj.getId());
	}
	
	public Transferencia FindById(Transferencia obj) {
		return dao.findById(obj.getId());
	}
	
	public List<Transferencia> findPerToday() {
		return dao.findPerToday();
	}
	
	
	public List<Transferencia> findNotaFiscal(Integer notaFical) {
		return dao.findByNotaFical(notaFical);
	}


	public List<Transferencia> findPerDate(String data) {
		return dao.findPerDate(data);
	}

	public List<Transferencia> findPerMonth(String data) {
		return dao.findPerMonth(data);
	}

	public List<Transferencia> findPerYear(String data) {
		return dao.findPerYear(data);
	}

	public List<Transferencia> findByMotorista(String nome) {
		return dao.findByMotorista(nome);
	}

}
