package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // Aquí podríamos buscar por cédula o email después
}