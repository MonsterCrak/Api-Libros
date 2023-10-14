package com.sabersinfin.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.sabersinfin.entity.Rol;
import com.sabersinfin.entity.Usuario;
import com.sabersinfin.servicesImpl.UsuarioServices;
import com.sabersinfin.utils.NotFoundException;


@RestController
@RequestMapping("api/usuario")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UsuarioController {

	@Autowired
	private UsuarioServices serUsuario;

	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping("/lista")
	public ResponseEntity<List<Usuario>> lista() {
		return new ResponseEntity<>(serUsuario.listarTodos(), HttpStatus.OK);
	}

	@PostMapping("/registrar")
	public ResponseEntity<String> registrar(@RequestBody Usuario bean) {
	    bean.setRegistro(LocalDate.now());
	    
	    // Asignar rol 2 - visitante
	    Rol r = new Rol();
	    r.setId(2);
	    bean.setRol(r);

	    bean.setEstado(1);
	    bean.setClave(encoder.encode(bean.getClave()));

	    // Registrar el usuario
	    Usuario u = serUsuario.registrar(bean);

	    return ResponseEntity.status(HttpStatus.CREATED).body("Usuario " + u.getNombre() + " registrado correctamente");
	}


	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@RequestBody Usuario bean) {
		Usuario u = validarUsuario(bean.getId());

		Usuario usu = serUsuario.buscarPorId(bean.getId());

		u = serUsuario.actualizar(bean);
		u.setRegistro(LocalDate.now());
		u.setRol(usu.getRol());
		
		//agregar logica de cambio de clave verificando la anterior

		serUsuario.actualizar(u);

		return ResponseEntity.status(HttpStatus.OK).body("Usuario " + u.getNombre() + " actualizado correctamente");
	}
	
	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<Usuario> buscar(@PathVariable("codigo") Integer codigo) {
		Usuario u = validarUsuario(codigo);
		
		serUsuario.buscarPorId(u.getId());
		
		return new ResponseEntity<>(u, HttpStatus.OK);
	}
	
	

	private Usuario validarUsuario(Integer codigo) {
		Usuario u = serUsuario.buscarPorId(codigo);
		if (u == null) {
			throw new NotFoundException();
		}
		return u;
	}

}
