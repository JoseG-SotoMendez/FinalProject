package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_id")
    private Integer pagoId;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago; // EFECTIVO, TARJETA, NEQUI

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "referencia_transaccion", length = 100)
    private String referenciaTransaccion;

    @Column(name = "estado_pago", length = 20)
    private String estadoPago; // APROBADO, RECHAZADO

    @PrePersist
    public void prePersist() {
        this.fechaHora = LocalDateTime.now();
    }
}