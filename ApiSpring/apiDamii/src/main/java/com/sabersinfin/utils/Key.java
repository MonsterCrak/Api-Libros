package com.sabersinfin.utils;

import java.util.Random;

public class Key {
	
	private static String secret_key = null;
	
	// Caracteres válidos para generar la clave
    private static final String VALID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Longitud de cada sección de la clave
    private static final int SECTION_LENGTH = 4;

    public static void generateKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        // Generar 12 caracteres aleatorios
        for (int i = 0; i < 12; i++) {
            // Elegir un carácter aleatorio de la lista de caracteres válidos
            char randomChar = VALID_CHARS.charAt(random.nextInt(VALID_CHARS.length()));
            key.append(randomChar);

            // Insertar guiones "-" después de cada sección de 4 caracteres
            if ((i + 1) % SECTION_LENGTH == 0 && i != 11) {
                key.append("-");
            }
        }
        secret_key = key.toString();
    }

	public static String getSecret_key() {
		return secret_key;
	}
	public static void limpiar() {
		secret_key=null;
	}
}
