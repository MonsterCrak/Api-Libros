package com.sabersinfin.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sabersinfin.entity.Enlace;
import com.sabersinfin.entity.Rol;
import com.sabersinfin.entity.Usuario;
import com.sabersinfin.repository.UsuarioRepository;



@Service
public class UsuarioSeguridadServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		try {
			Usuario objUsuario = usuarioRepository.findByEmail(login);
			if (objUsuario != null) {
				List<Rol> lstRol = usuarioRepository.traerRolesDeUsuario(objUsuario.getId());
				List<Enlace> lstOpciones = usuarioRepository.traerEnlacesDeUsuario((int) lstRol.get(0).getId());

				userDetails = UsuarioPrincipal.build(objUsuario, lstRol, lstOpciones);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new UsernameNotFoundException("Wrong username");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Database Error");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Unknown Error");
		}
		return userDetails;
	}
}
