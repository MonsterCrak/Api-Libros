package com.sabersinfin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_rol_has_enlace")
@Data
public class RolHasEnlace {
	@Id
	@ManyToOne
	@JoinColumn(name = "id_rol")
	private Rol rol;
	@Id
	@ManyToOne
	@JoinColumn(name = "id_enlace")
	private Enlace enlace;
}


