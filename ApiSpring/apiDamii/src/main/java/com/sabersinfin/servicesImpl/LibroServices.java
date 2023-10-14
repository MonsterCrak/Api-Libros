package com.sabersinfin.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.sabersinfin.entity.Libro;
import com.sabersinfin.repository.LibroRepository;

@Service
public class LibroServices extends ICRUDImpl<Libro, Integer>{
	
	@Autowired
	private LibroRepository repo;

	@Override
	public JpaRepository<Libro, Integer> getRepository() {
		return repo;
	}
	
	public List<Libro> buscarPorGenero(int codigoGenero) {
	    return repo.findByCodigoGenero(codigoGenero);
	}

}
