package com.sabersinfin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sabersinfin.entity.Enlace;
import com.sabersinfin.entity.Rol;
import com.sabersinfin.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	@Query("SELECT u FROM Usuario u WHERE u.email = :#{#usu.email} and u.clave = :#{#usu.clave}")
	public abstract Usuario login(@Param(value = "usu") Usuario bean);
	
	@Query("SELECT u.rol FROM Usuario u WHERE u.id = :var_idUsuario")
	List<Rol> traerRolesDeUsuario(@Param("var_idUsuario") int idUsuario);
	
	@Query("SELECT re.enlace FROM RolHasEnlace re WHERE re.rol.id = :var_idRol")
	public abstract List<Enlace> traerEnlacesDeUsuario(@Param("var_idRol") int idRol);
	
	// Modificar para buscar por email al usuario
	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
	public Usuario findByCorreo(@Param("email") String email);


	
	public abstract Usuario findByEmail(String email);
	
	public boolean existsByEmail(String correo);
	
	
}
