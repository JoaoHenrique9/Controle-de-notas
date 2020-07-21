package model.dao;

import java.util.List;

import model.entities.ControlePor;

public interface ControlePorDao {

	List<ControlePor> findAll();
	List<ControlePor> findPerDate(String data);
	List<ControlePor> findPerMonth(String data);
	List<ControlePor> findPerYear(String data);
	List<ControlePor> findByMotorista(String nome);
	List<ControlePor> findPerToday();
	ControlePor findById(Integer Id);
	void update(ControlePor obj);
	void insert(ControlePor obj);
	void deleteByNota(Integer nota);
	ControlePor findByNumeroNota(Integer NumeroNota);
	

}
