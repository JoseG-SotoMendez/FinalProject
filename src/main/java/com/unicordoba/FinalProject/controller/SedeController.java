package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.entity.Sede;
import com.unicordoba.FinalProject.service.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
public class SedeController {

    @Autowired
    private SedeService sedeService;

    @GetMapping
    public List<Sede> getAllSedes() {
        return sedeService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sede> getSedeById(@PathVariable Integer id) {
        return sedeService.obtenerPorId(id)
                .map(sede -> ResponseEntity.ok(sede))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sede createSede(@RequestBody Sede sede) {
        return sedeService.guardarSede(sede);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSede(@PathVariable Integer id) {
        sedeService.eliminarSede(id);
        return ResponseEntity.noContent().build();
    }
}