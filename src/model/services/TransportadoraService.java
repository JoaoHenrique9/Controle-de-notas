package model.services;

import java.util.List;

import model.dao.TransportadoraDao;
import model.dao.DaoFactory;
import model.entities.Transportadora;

public class TransportadoraService {
	
private TransportadoraDao dao = DaoFactory.createTransportadoraDao();
	
	public List<Transportadora> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Transportadora obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Transportadora obj) {
		dao.deleteById(obj.getId());
	}
	public Transportadora findTransportadora(Transportadora obj) {
		return dao.findByNome(obj.getNome());
	}


}
