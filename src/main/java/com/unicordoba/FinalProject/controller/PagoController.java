package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.dto.PagoDTO;
import com.unicordoba.FinalProject.entity.Factura;
import com.unicordoba.FinalProject.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private FacturacionService facturacionService;

    @PostMapping
    public ResponseEntity<Factura> realizarPago(@RequestBody PagoDTO pagoDTO) {
        Factura facturaGenerada = facturacionService.registrarPago(pagoDTO);
        return ResponseEntity.ok(facturaGenerada);
    }
}