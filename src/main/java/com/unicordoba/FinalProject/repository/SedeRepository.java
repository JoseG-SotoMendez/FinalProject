package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    // ¡Listo! Ya tienes métodos para guardar, buscar y eliminar Sedes.
}