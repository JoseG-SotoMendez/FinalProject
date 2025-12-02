package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Integer pedidoId;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    // El usuario (cajero) que registró el pedido
    @ManyToOne
    @JoinColumn(name = "usuario_caja_id")
    private Usuario usuarioCaja;

    // El repartidor (si es domicilio). Puede ser NULL (nullable = true)
    @ManyToOne
    @JoinColumn(name = "repartidor_id", nullable = true)
    private Empleado repartidor;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Column(length = 20)
    private String estado; // Ej: PENDIENTE, ENTREGADO, CANCELADO

    @Column(length = 20)
    private String tipo; // Ej: MESA, DOMICILIO, RECOGIDA

    @Column(name = "direccion_envio", length = 200)
    private String direccionEnvio;

    @PrePersist
    public void prePersist() {
        this.fechaHora = LocalDateTime.now();
        if(this.estado == null) this.estado = "PENDIENTE";
    }
}