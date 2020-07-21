package model.dao;

import java.util.List;

import model.entities.Cop;


public interface CopDao {
	
	List<Cop> findAll();
	List<Cop> findPerDate(String data);
	List<Cop> findPerMonth(String data);
	List<Cop> findPerYear(String data);
	List<Cop> findBySetor(String nome);
	List<Cop> findByMatricula(Integer matricula);
	List<Cop> findPerToday();
	Cop findByid(Integer id);
	void insert(Cop obj);
	void update(Cop obj);
	void deleteById(Integer id);
	
	
	

}
