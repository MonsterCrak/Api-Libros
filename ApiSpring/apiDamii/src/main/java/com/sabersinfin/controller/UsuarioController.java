package com.sabersinfin.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabersinfin.entity.CambioClaveDTO;
import com.sabersinfin.entity.Rol;
import com.sabersinfin.entity.Usuario;
import com.sabersinfin.servicesImpl.UsuarioServices;
import com.sabersinfin.utils.Mensaje;
import com.sabersinfin.utils.NotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/usuario")
//@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@CrossOrigin(originPatterns = "*")
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
	public ResponseEntity<?> registrar(@Valid @RequestBody Usuario bean, BindingResult result) {

		ResponseEntity<?> error = validarUsuario(bean);
		if (error != null) {
			return error;
		}

		// Limpiar espacios en blanco de los campos
		bean.setNombre(bean.getNombre().trim());
		bean.setPaterno(bean.getPaterno().trim());
		bean.setMaterno(bean.getMaterno().trim());
		bean.setClave(bean.getClave().trim());

		bean.setRegistro(LocalDate.now());
		bean.setEstado(1);

		Rol r = new Rol();
		r.setId(2);
		bean.setRol(r);

		bean.setClave(encoder.encode(bean.getClave()));

		serUsuario.registrar(bean);
    	
		return ResponseEntity.status(HttpStatus.OK).body("{\"mensaje\": \"Usuario registrado correctamente\"}");
	}

	@PutMapping("/actualizar")
	public ResponseEntity<?> actualizar(@Valid @RequestBody Usuario bean, BindingResult result) {
		
		
		
		Usuario u = serUsuario.buscarPorId(bean.getId());

		if (u == null) {
			throw new NotFoundException();
		}

		bean.setEstado(u.getEstado());
		bean.setRegistro(u.getRegistro());
		bean.setRol(u.getRol());
		
		bean.setClave(u.getClave());
		
		ResponseEntity<?> error = validarUsuario(bean);
		if (error != null) {
			return error;
		}

		
		serUsuario.actualizar(bean);

		return ResponseEntity.status(HttpStatus.OK).body("{\"mensaje\": \"Usuario actualizado correctamente\"}");
	}
	
	@PutMapping("/cambiar-clave/{id}")
	public ResponseEntity<?> cambiarContrasena(@PathVariable Integer id, @RequestBody CambioClaveDTO request) {
	    Usuario usuario = serUsuario.buscarPorId(id);
	    
	    if (usuario == null) {
	        throw new NotFoundException();
	    }

	    if (!encoder.matches(request.getContrasenaActual(), usuario.getClave())) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"La contraseña actual es incorrecta\"}");
	    }
	    
	    if (request.getNuevaContrasena().equals(request.getContrasenaActual())) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"La nueva contraseña no puede ser igual a la actual\"}");
	    }

	    if (!request.getNuevaContrasena().equals(request.getConfirmacionContrasena())) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"Las contraseñas no coinciden\"}");
	    }

	    usuario.setClave(encoder.encode(request.getNuevaContrasena()));
	    serUsuario.actualizar(usuario);

	    return ResponseEntity.status(HttpStatus.OK).body("{\"mensaje\": \"Contraseña actualizada correctamente\"}");
	}


	private ResponseEntity<?> validarUsuario(Usuario bean) {
		boolean emailExistente = serUsuario.existeEmail(bean.getEmail());

		if (bean.getId() == 0) {
	       
	        if (emailExistente) {
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"El correo ya está registrado\"}");
	        }
	    } else {
	        
	        Usuario usuarioExistente = serUsuario.buscarPorId(bean.getId());
	        if (!usuarioExistente.getEmail().equals(bean.getEmail()) && emailExistente) {
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"El correo ya está registrado\"}");
	        }
	    }

		if (!isValidEmail(bean.getEmail())) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"Ingrese un correo valido\"}");
		}

		// Eliminar espacios en blanco de nombre
		if (bean.getNombre() == null || bean.getNombre().trim().isEmpty()) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"Ingrese un nombre valido\"}");
		}

		// Eliminar espacios en blanco de apellido paterno
		if (bean.getPaterno() == null || bean.getPaterno().trim().isEmpty()) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"Ingrese un apellido paterno valido\"}");

		}

		// Eliminar espacios en blanco de apellido materno
		if (bean.getMaterno() == null || bean.getMaterno().trim().isEmpty()) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"Ingrese un apellido materno valido\"}");
		}

		// Eliminar espacios en blanco de clave
		if (bean.getClave() == null || bean.getClave().trim().isEmpty()) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"Ingrese una clave valida\"}");
		}

		// Limpiar espacios en blanco de los campos
		bean.setNombre(bean.getNombre().trim());
		bean.setPaterno(bean.getPaterno().trim());
		bean.setMaterno(bean.getMaterno().trim());
		bean.setClave(bean.getClave().trim());

		return null;
	}

	private boolean isValidEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email.matches(regex);
	}

	@GetMapping("/buscar/{codigo}")
	public ResponseEntity<Usuario> buscar(@PathVariable("codigo") Integer codigo) {
		Usuario u = validandoUsuario(codigo);

		serUsuario.buscarPorId(u.getId());

		return new ResponseEntity<>(u, HttpStatus.OK);
	}

	private Usuario validandoUsuario(Integer codigo) {
		Usuario u = serUsuario.buscarPorId(codigo);
		if (u == null) {
			throw new NotFoundException();
		}
		return u;
	}

}
