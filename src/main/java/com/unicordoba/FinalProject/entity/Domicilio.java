package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "domicilio")
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domicilio_id")
    private Integer domicilioId;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Empleado repartidor;

    @Column(name = "hora_asignacion")
    private LocalDateTime horaAsignacion;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Column(name = "hora_entrega")
    private LocalDateTime horaEntrega;

    @Column(name = "estado_entrega", length = 20)
    private String estadoEntrega; // EN_CAMINO, ENTREGADO

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}