package model.dao;

import java.util.List;

import model.entities.Transferencia;

public interface TransferenciaDao {
	List<Transferencia> findAll();
	List<Transferencia> findPerDate(String data);
	List<Transferencia> findPerMonth(String data);
	List<Transferencia> findPerYear(String data);
	List<Transferencia> findByNotaFical(Integer notaFical);
	List<Transferencia> findByMotorista(String nome);
	List<Transferencia> findPerToday();
	void insert(Transferencia obj);
	void update(Transferencia obj);
	void deleteById(Integer id);
	Transferencia findById(Integer id);
}
