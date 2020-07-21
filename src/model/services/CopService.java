package model.services;

import java.util.List;

import model.dao.CopDao;
import model.dao.DaoFactory;
import model.entities.Cop;

public class CopService {
	
private CopDao dao = DaoFactory.CreateCopDao();
	
	public List<Cop> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Cop obj) {
		if (obj.getId()== null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Cop obj) {
		dao.deleteById(obj.getId());
	}
	
	public List<Cop> findPerToday() {
		return dao.findPerToday();
	}
	
	public List<Cop> findMatricula(Integer matricula) {
		return dao.findByMatricula(matricula);
	}


	public List<Cop> findPerDate(String data) {
		return dao.findPerDate(data);
	}

	public List<Cop> findPerMonth(String data) {
		return dao.findPerMonth(data);
	}

	public List<Cop> findPerYear(String data) {
		return dao.findPerYear(data);
	}

	public List<Cop> findBySetor(String nome) {
		return dao.findBySetor(nome);
	}

}
