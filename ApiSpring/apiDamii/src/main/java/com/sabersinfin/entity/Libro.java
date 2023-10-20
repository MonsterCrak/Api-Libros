package com.sabersinfin.entity;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private int id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "archivo", nullable = false)
    private String archivo;
    
    @Column(name = "portada", nullable = false)
    private String portada;
    
    @Column(name = "registro", nullable = false)
    private LocalDate registro;

    @Column(name = "estado", nullable = false)
    private int estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "libro")
    @JsonIgnore
    private List<Vistas> vistas;
    
    @OneToMany(mappedBy = "libro")
    @JsonIgnore
    private List<Favoritos> favoritos;
    
    @ManyToOne
    @JoinColumn(name = "id_genero") 
    private Genero genero;

}