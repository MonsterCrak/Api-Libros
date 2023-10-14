package com.sabersinfin.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_favoritos")
@Data
public class Favoritos {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_favorito")
    private int id;
	
	@Column(name = "fecha_registro_f", nullable = false)
    private LocalDateTime favorito;
	
	@Column(name = "estado", nullable = false)
	private int estado;
	
	@ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_libro")
    private Libro libro;
	
}
