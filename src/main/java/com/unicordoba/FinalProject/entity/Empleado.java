package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empleado_id")
    private Integer empleadoId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String documento;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String rol; // Ej: MESERO, COCINERO

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    private Boolean activo;

    // RELACIÓN: Un empleado pertenece a una Sede
    // En la BD esto creará la columna "sede_id" automáticamente
    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;
}