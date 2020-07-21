package model.dao;

import java.util.List;

import model.entities.Expedicao;

public interface ExpedicaoDao {
	void insert(Expedicao obj);
	void update(Expedicao obj);
	void deleteById(Integer id);
	Expedicao findById(Integer id);
	List<Expedicao> findAll();
	List<Expedicao> findPerDate(String data);
	List<Expedicao> findPerMonth(String data);
	List<Expedicao> findPerYear(String data);
	List<Expedicao> findByMotorista(String nome);
	List<Expedicao> findByTransportadora(String nome);
	List<Expedicao> findPerToday();
	
	

}
