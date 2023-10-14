package com.sabersinfin.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.sabersinfin.entity.Genero;
import com.sabersinfin.repository.GeneroRepository;

@Service
public class GeneroServices extends ICRUDImpl<Genero, Integer>{
	
	@Autowired
	private GeneroRepository repo;

	@Override
	public JpaRepository<Genero, Integer> getRepository() {
		return repo;
	}
	
	

}
