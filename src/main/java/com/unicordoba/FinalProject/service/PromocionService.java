package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.entity.Promocion;
import com.unicordoba.FinalProject.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    public List<Promocion> obtenerTodas() { return promocionRepository.findAll(); }

    public Optional<Promocion> obtenerPorId(Integer id) { return promocionRepository.findById(id); }

    public Promocion guardar(Promocion promocion) { return promocionRepository.save(promocion); }

    public void eliminar(Integer id) { promocionRepository.deleteById(id); }
}