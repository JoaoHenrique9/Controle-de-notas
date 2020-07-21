package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ControlePorDao;
import model.entities.ControlePor;

public class ControlePorService {

	private ControlePorDao dao = DaoFactory.createControlePorDao();

	public List<ControlePor> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(ControlePor obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(ControlePor obj) {
		dao.deleteByNota(obj.getNumeroNota());
	}

	public ControlePor FindById(ControlePor obj) {
		return dao.findById(obj.getId());
	}

	public List<ControlePor> findPerToday() {
		return dao.findPerToday();
	}
	
	public ControlePor findNotas(Integer numeroNota) {
		return dao.findByNumeroNota(numeroNota);
	}


	public List<ControlePor> findPerDate(String data) {
		return dao.findPerDate(data);
	}

	public List<ControlePor> findPerMonth(String data) {
		return dao.findPerMonth(data);
	}

	public List<ControlePor> findPerYear(String data) {
		return dao.findPerYear(data);
	}

	public List<ControlePor> findByMotorista(String nome) {
		return dao.findByMotorista(nome);
	}

}
