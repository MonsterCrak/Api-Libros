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
@Table(name = "tb_vistas")
@Data
public class Vistas {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_vista")
    private int id;
	
	@Column(name = "fecha_registro_v", nullable = false)
    private LocalDateTime vista;
	
	@ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_libro")
    private Libro libro;
	
}
