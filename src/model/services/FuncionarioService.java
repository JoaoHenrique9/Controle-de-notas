package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.FuncionarioDao;
import model.entities.Funcionario;

public class FuncionarioService {
	
private FuncionarioDao dao = DaoFactory.CreateFuncionarioDao();
	
	public List<Funcionario> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Funcionario obj) {
		if (obj.getId()== null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}public Funcionario findByMatricula(Funcionario obj) {
		return dao.findByMatricula(obj.getMatricula());
	}
	
	
	public void remove(Funcionario obj) {
		dao.deleteByMatricula(obj.getMatricula());
	}

}
