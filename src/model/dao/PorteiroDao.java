package model.dao;

import java.util.List;

import model.entities.Porteiro;



public interface PorteiroDao {
	void insert(Porteiro obj);
	void update(Porteiro obj);
	List<Porteiro> findAll();
	Porteiro findById(Integer id);
	void deleteById(Integer id);
	Porteiro findByNome(String nome);

}
