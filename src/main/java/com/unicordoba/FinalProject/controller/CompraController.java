package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.dto.CompraDTO;
import com.unicordoba.FinalProject.entity.Compra;
import com.unicordoba.FinalProject.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public ResponseEntity<Compra> registrarCompra(@RequestBody CompraDTO compraDTO) {
        Compra nuevaCompra = compraService.registrarCompra(compraDTO);
        return ResponseEntity.ok(nuevaCompra);
    }
}