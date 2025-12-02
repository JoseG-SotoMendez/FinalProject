package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Método personalizado para buscar por username (para el login futuro)
    Optional<Usuario> findByUsername(String username);
}