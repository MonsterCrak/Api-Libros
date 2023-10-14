package com.sabersinfin.security;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private String secret = "sistema-prestamo";
	private int expiration = 36000;
	
	public String generateToken(Authentication authentication) {
		UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject(usuarioPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
	}
	
	public String getNombreUsuarioFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
