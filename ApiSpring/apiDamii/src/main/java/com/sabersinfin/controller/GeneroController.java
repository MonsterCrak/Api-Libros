package com.sabersinfin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabersinfin.entity.Genero;
import com.sabersinfin.servicesImpl.GeneroServices;
import com.sabersinfin.utils.NotFoundException;

@RestController
@RequestMapping("api/genero")
@CrossOrigin(originPatterns = "*")
public class GeneroController {

	@Autowired
	private GeneroServices serGenero;
	
	@GetMapping("/lista")
	public ResponseEntity<List<Genero>> lista(){
		return  new ResponseEntity<>(serGenero.listarTodos(),HttpStatus.OK);
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<String> registrar(@RequestBody Genero bean) {
		Genero g = serGenero.registrar(bean);
		return ResponseEntity.status(HttpStatus.CREATED).body("Genero " + g.getNombre() + " registrado correctamente");
	}

	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestBody Genero bean) {
		Genero g = validarGenero(bean.getId());
		
		serGenero.actualizar(g);
		
		return ResponseEntity.status(HttpStatus.OK).body("Genero " + g.getNombre() + " actualizado correctamente");
	}
	
	@DeleteMapping("/eliminar/{codigo}")
	public ResponseEntity<Void> eliminar(@PathVariable("codigo") Integer codigo) { 
		Genero g = validarGenero(codigo);
 
		serGenero.eliminarPorId(g.getId());
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<Genero> buscar(@PathVariable("codigo") Integer codigo) {
		Genero g = validarGenero(codigo);
		
		serGenero.buscarPorId(g.getId());
		
		return new ResponseEntity<>(g, HttpStatus.OK);
	}
	
	private Genero validarGenero(Integer codigo) {
		Genero g = serGenero.buscarPorId(codigo);
		if (g == null) {
			throw new NotFoundException();
		}
		return g;
	}
}

