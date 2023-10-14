package com.sabersinfin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_enlace")
public class Enlace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_enlace")
	private int id;
	
	@Column(name = "Descripcion", nullable = false)
	private String descripcion;
	
	@Column(name = "ruta", nullable = false)
	private String ruta;
	
	@Column(name = "estado", nullable = false)
	private int estado;
	
}
