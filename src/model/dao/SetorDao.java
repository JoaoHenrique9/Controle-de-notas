package model.dao;


import java.util.List;

import model.entities.Setor;

public interface SetorDao {
	
		void insert(Setor obj);
		void update(Setor obj);
		void deleteById(Integer ID);
		List<Setor> findAll();
		Setor findByNome(String nome);
		

}
