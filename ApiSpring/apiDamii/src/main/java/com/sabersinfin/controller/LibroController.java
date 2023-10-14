package com.sabersinfin.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabersinfin.entity.Genero;
import com.sabersinfin.entity.Libro;
import com.sabersinfin.servicesImpl.GeneroServices;
import com.sabersinfin.servicesImpl.LibroServices;
import com.sabersinfin.utils.NotFoundException;

@RestController
@RequestMapping("api/libro")
@CrossOrigin(originPatterns = "*")
public class LibroController {

	@Autowired
	private LibroServices serLibro;

	@Autowired
	private GeneroServices serGenero;

	@GetMapping("/lista")
	public ResponseEntity<List<Libro>> lista() {
		return new ResponseEntity<>(serLibro.listarTodos(), HttpStatus.OK);
	}

	@PostMapping("/registrar")
	public ResponseEntity<String> registrar(@RequestBody Libro bean) {
		Libro lib = serLibro.registrar(bean);

		lib.setRegistro(LocalDate.now());

		lib.setEstado(1);

		serLibro.registrar(lib);

		return ResponseEntity.status(HttpStatus.CREATED).body("Libro " + lib.getTitulo() + " registrado correctamente");
	}

	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestBody Libro bean) {
		Libro lib = validarLibro(bean.getId());

		lib = serLibro.actualizar(bean);
		lib.setRegistro(LocalDate.now());
		// dar valor de estado por frontent para modificar
		// 1 visible
		// 2 oculto
		// lib.setEstado(l.getEstado());

		serLibro.actualizar(lib);

		return ResponseEntity.status(HttpStatus.OK).body("Libro " + lib.getTitulo() + " actualizado correctamente");
	}

	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<Libro> buscar(@PathVariable("codigo") Integer codigo) {
		Libro lib = validarLibro(codigo);

		serLibro.buscarPorId(lib.getId());

		return new ResponseEntity<>(lib, HttpStatus.OK);
	}

	@GetMapping("/buscarPorGenero/{codigoGenero}")
	public ResponseEntity<List<Libro>> buscarPorGenero(@PathVariable("codigoGenero") Integer codigoGenero) {
		Genero g = validarGenero(codigoGenero);

		List<Libro> libros = serLibro.buscarPorGenero(g.getId());

		return new ResponseEntity<>(libros, HttpStatus.OK);
	}

	private Libro validarLibro(Integer codigo) {
		Libro lib = serLibro.buscarPorId(codigo);
		if (lib == null) {
			throw new NotFoundException();
		}
		return lib;
	}

	private Genero validarGenero(Integer codigo) {
		Genero g = serGenero.buscarPorId(codigo);
		if (g == null) {
			throw new NotFoundException();
		}
		return g;
	}
}
