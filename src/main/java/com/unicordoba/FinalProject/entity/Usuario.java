package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255) // Largo por si encriptamos la clave
    private String passwordHash;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String rol; // Ej: ADMIN, CAJERO

    private Boolean activo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist // Se ejecuta antes de guardar para poner la fecha actual sola
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if(this.activo == null) this.activo = true;
    }
}