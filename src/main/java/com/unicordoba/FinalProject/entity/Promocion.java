package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "promocion")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_id")
    private Integer promoId;

    @Column(length = 50, unique = true)
    private String codigo; // Ej: VERANO2025

    @Column(length = 200)
    private String descripcion;

    @Column(name = "tipo_descuento", length = 20)
    private String tipoDescuento; // PORCENTAJE, MONTO_FIJO

    @Column(precision = 10, scale = 2)
    private BigDecimal valor; // Ej: 10.00 (si es 10%)

    private LocalDate inicio;
    private LocalDate fin;

    @Column(columnDefinition = "TEXT")
    private String condiciones;
}