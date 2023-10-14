package com.sabersinfin.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sabersinfin.entity.Enlace;
import com.sabersinfin.entity.Rol;
import com.sabersinfin.entity.Usuario;
import com.sabersinfin.repository.UsuarioRepository;

@Service
public class UsuarioServices extends ICRUDImpl<Usuario, Integer>{
	
	@Autowired
	private UsuarioRepository repo;

	@Override
	public JpaRepository<Usuario, Integer> getRepository() {
		return repo;
	}
	
	public Usuario loginUsuario(String email, String clave) {
	    Usuario usuario = repo.findByEmail(email);

	    if (usuario != null && usuario.getClave().equals(clave)) {
	        return usuario;
	    }

	    return null;
	}


    public List<Rol> traerRolesDeUsuario(int idUsuario) {
        return repo.traerRolesDeUsuario(idUsuario);
    }

    public List<Enlace> traerEnlacesDeUsuario(int idRol) {
        return repo.traerEnlacesDeUsuario(idRol);
    }

    public Usuario findByEmail(String email) {
        return repo.findByEmail(email);
    }
    
    public boolean existeEmail(String email) {
		return repo.existsByEmail(email);
	}

    
    public Usuario buscarPorEmail(String correo) {
		return repo.findByCorreo(correo);
	}
}
