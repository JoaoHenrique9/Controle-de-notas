package model.dao;

import java.util.List;

import model.entities.Motorista;

public interface MotoristaDao {
	void insert(Motorista obj);
	void update(Motorista obj);
	Motorista findById(Integer id);
	List<Motorista> findAll();
	Motorista findByNome(String nome);
	Motorista findByCPF(String cpf);
	void deleteById(Integer id);
	

}
