package com.sabersinfin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sabersinfin.entity.Libro;

public interface LibroRepository extends JpaRepository<Libro, Integer>{

	 @Query("SELECT l FROM Libro l WHERE l.genero.id = :codigoGenero")
	    List<Libro> findByCodigoGenero(int codigoGenero);
	
}
