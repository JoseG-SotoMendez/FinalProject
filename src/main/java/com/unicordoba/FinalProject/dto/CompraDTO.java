package com.unicordoba.FinalProject.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraDTO {
    private Integer proveedorId;
    private Integer sedeId;
    private List<DetalleCompraDTO> items;
}