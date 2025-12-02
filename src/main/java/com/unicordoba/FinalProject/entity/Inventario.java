package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventario_id")
    private Integer inventarioId;

    // Relación: Un registro de inventario pertenece a UNA Sede
    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    // Relación: Un registro de inventario pertenece a UN Producto
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(precision = 10, scale = 2) // Decimal, por si venden por kilos (ej. carne)
    private BigDecimal cantidad;

    @Column(length = 20)
    private String unidad; // Ej: UNIDAD, KG, LITRO

    @Column(name = "fecha_ultimo_movimiento")
    private LocalDateTime fechaUltimoMovimiento;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.fechaUltimoMovimiento = LocalDateTime.now();
    }
}