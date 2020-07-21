package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TaxiDao;
import model.entities.Taxi;

public class TaxiService {

	private TaxiDao dao = DaoFactory.CreateTaxiDao();

	public List<Taxi> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Taxi obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Taxi obj) {
		dao.deleteById(obj.getId());
	}

	public Taxi FindById(Taxi obj) {
		return dao.findById(obj.getId());
	}

	public List<Taxi> findPerToday() {
		return dao.findPerToday();
	}

	public List<Taxi> findMatricula(Integer matricula) {
		return dao.findByMatricula(matricula);
	}

	public List<Taxi> findPerDate(String data) {
		return dao.findPerDate(data);
	}

	public List<Taxi> findPerMonth(String data) {
		return dao.findPerMonth(data);
	}

	public List<Taxi> findPerYear(String data) {
		return dao.findPerYear(data);
	}

	public List<Taxi> findBySetor(String nome) {
		return dao.findBySetor(nome);
	}

}
