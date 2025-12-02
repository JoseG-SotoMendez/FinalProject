package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Integer proveedorId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String contacto; // Nombre de la persona de contacto

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;
}