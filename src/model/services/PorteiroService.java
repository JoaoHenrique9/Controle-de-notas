package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PorteiroDao;
import model.entities.Porteiro;

public class PorteiroService {
	
private PorteiroDao dao = DaoFactory.createPorteiroDao();
	
	public List<Porteiro> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Porteiro obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Porteiro obj) {
		dao.deleteById(obj.getId());
	}
	public Porteiro findPorteiro(Porteiro obj) {
		return dao.findByNome(obj.getNome());
	}

}
