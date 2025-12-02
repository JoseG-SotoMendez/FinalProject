package com.unicordoba.FinalProject.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleCompraDTO {
    private Integer productoId;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
}