package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data // Lombok genera automáticamente gets, sets y toString
@Entity // Esto le dice a Spring: "Convierte esta clase en una tabla"
@Table(name = "sede")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementable
    @Column(name = "sede_id")
    private Integer sedeId;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 20)
    private String telefono;

    // Usamos BigDecimal para coordenadas (latitud/longitud) por precisión
    @Column(precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(length = 100)
    private String horario;
}