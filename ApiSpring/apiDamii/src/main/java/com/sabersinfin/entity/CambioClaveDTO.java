package com.sabersinfin.entity;

import lombok.Data;

@Data
public class CambioClaveDTO {

	private String contrasenaActual;
    private String nuevaContrasena;
    private String confirmacionContrasena;
}
