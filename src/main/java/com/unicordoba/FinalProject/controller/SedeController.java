package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.entity.Sede;
import com.unicordoba.FinalProject.service.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes") // La URL será: http://localhost:8080/api/sedes
public class SedeController {

    @Autowired
    private SedeService sedeService;

    // 1. GET: Traer todas las sedes
    @GetMapping
    public List<Sede> getAllSedes() {
        return sedeService.obtenerTodas();
    }

    // 2. GET: Traer una sede por ID
    @GetMapping("/{id}")
    public ResponseEntity<Sede> getSedeById(@PathVariable Integer id) {
        return sedeService.obtenerPorId(id)
                .map(sede -> ResponseEntity.ok(sede)) // Si la encuentra, devuelve 200 OK y la sede
                .orElse(ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    // 3. POST: Crear una nueva sede
    @PostMapping
    public Sede createSede(@RequestBody Sede sede) {
        return sedeService.guardarSede(sede);
    }

    // 4. DELETE: Eliminar una sede
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSede(@PathVariable Integer id) {
        sedeService.eliminarSede(id);
        return ResponseEntity.noContent().build();
    }
}