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


    public Sede guardarSede(Sede sede) {
        return sedeRepository.save(sede);
    }


    public List<Sede> obtenerTodas() {
        return sedeRepository.findAll();
    }


    public Optional<Sede> obtenerPorId(Integer id) {
        return sedeRepository.findById(id);
    }


    public void eliminarSede(Integer id) {
        sedeRepository.deleteById(id);
    }
}