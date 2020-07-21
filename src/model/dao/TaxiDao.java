package model.dao;

import java.util.List;

import model.entities.Taxi;


public interface TaxiDao {
	List<Taxi> findAll();
	List<Taxi> findPerDate(String data);
	List<Taxi> findPerMonth(String data);
	List<Taxi> findPerYear(String data);
	List<Taxi> findBySetor(String nome);
	List<Taxi> findByMatricula(Integer matricula);
	List<Taxi> findPerToday();
	void insert(Taxi obj);
	void update(Taxi obj);
	void deleteById(Integer id);
	Taxi findById(Integer id);
	
	

}
