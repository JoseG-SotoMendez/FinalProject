package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Integer productoId;

    @Column(unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT") // Para descripciones largas
    private String descripcion;

    @Column(name = "precio_base", precision = 10, scale = 2) // 10 dígitos, 2 decimales
    private BigDecimal precioBase;

    @Column(length = 50)
    private String tipo; // Ej: COMIDA, BEBIDA

    private Boolean activo;
}