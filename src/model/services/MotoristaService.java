package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MotoristaDao;
import model.entities.Motorista;

public class MotoristaService {
	
private MotoristaDao dao = DaoFactory.CreateMotoristaDao();
	
	public List<Motorista> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Motorista obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Motorista obj) {
		dao.deleteById(obj.getId());
	}
	public Motorista findMotorista(Motorista obj) {
		return dao.findByCPF(obj.getCpf());
	}

}

