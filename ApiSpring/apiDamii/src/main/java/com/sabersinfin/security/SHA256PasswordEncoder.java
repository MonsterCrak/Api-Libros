package com.sabersinfin.security;
import java.nio.charset.StandardCharsets;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.hash.Hashing;

public class SHA256PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		String hash = Hashing.sha256().hashString(rawPassword, StandardCharsets.UTF_8).toString();
		
		return hash;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}

}
