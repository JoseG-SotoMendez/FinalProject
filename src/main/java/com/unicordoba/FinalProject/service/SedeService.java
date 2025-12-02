package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.entity.Sede;
import com.unicordoba.FinalProject.repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SedeService {

    @Autowired
    private SedeRepository sedeRepository;

    // 1. Guardar o Actualizar una Sede
    public Sede guardarSede(Sede sede) {
        return sedeRepository.save(sede);
    }

    // 2. Listar todas las sedes
    public List<Sede> obtenerTodas() {
        return sedeRepository.findAll();
    }

    // 3. Buscar una sede por ID
    public Optional<Sede> obtenerPorId(Integer id) {
        return sedeRepository.findById(id);
    }

    // 4. Eliminar una sede
    public void eliminarSede(Integer id) {
        sedeRepository.deleteById(id);
    }
}