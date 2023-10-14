package com.sabersinfin.security;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = getToken(req);

			if (token != null && jwtProvider.validateToken(token)) {
				String nombreUsuario = jwtProvider.getNombreUsuarioFromToken(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		filterChain.doFilter(req, res);
	}
	
	private String getToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer"))
			return header.replace("Bearer ", "");
		return null;
	}
	
}
