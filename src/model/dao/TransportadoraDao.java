package model.dao;

import java.util.List;

import model.entities.Transportadora;

public interface TransportadoraDao {
	
	void insert(Transportadora obj);
	void update(Transportadora obj);
	void deleteById(Integer id);
	Transportadora findByNome(String nome);
	Transportadora findById(Integer id);
	List<Transportadora> findAll();
	
	
	

}
