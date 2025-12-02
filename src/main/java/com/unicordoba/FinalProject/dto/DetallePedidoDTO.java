package com.unicordoba.FinalProject.dto;

import lombok.Data;

@Data
public class DetallePedidoDTO {
    private Integer productoId;
    private Integer cantidad;
    private String observaciones;
}