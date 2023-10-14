package com.sabersinfin.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.sabersinfin.entity.Enlace;

import lombok.Getter;
import lombok.Setter;





@Getter
@Setter
public class JwtDto {
	private String token;
    private String bearer = "Bearer";
    private String username;
    private int idUsuario;
    private Collection<? extends GrantedAuthority> authorities;
    private List<Enlace> enlaces;
    
    public JwtDto(String token,String username, int idUsuario, Collection<? extends GrantedAuthority> authorities,List<Enlace> lstEnlaces) {
        this.token = token;
        this.username = username;
        this.authorities = authorities;
        this.idUsuario = idUsuario;
        this.enlaces = lstEnlaces;
    }
}
