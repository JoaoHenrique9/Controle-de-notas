package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ExpedicaoDao;
import model.entities.Expedicao;

public class ExpedicaoService {

	private ExpedicaoDao dao = DaoFactory.CreateExpedicaoDao();

	public List<Expedicao> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Expedicao obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Expedicao obj) {
		dao.deleteById(obj.getId());
	}

	public Expedicao FindById(Expedicao obj) {
		return dao.findById(obj.getId());
	}

	public List<Expedicao> findPerToday() {
		return dao.findPerToday();
	}

	public List<Expedicao> findByMotorista(String nome) {
		return dao.findByMotorista(nome);
	}

	public List<Expedicao> findByTransportadora(String nome) {
		return dao.findByTransportadora(nome);
	}

	public List<Expedicao> findPerYear(String data) {
		return dao.findPerYear(data);
	}

	public List<Expedicao> findPerMonth(String data) {
		return dao.findPerMonth(data);
	}

	public List<Expedicao> findPerDate(String data) {
		return dao.findPerDate(data);
	}

}
