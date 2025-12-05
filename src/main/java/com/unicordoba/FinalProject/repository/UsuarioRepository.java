package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u FROM Usuario u WHERE " +
            "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:rol IS NULL OR u.rol = :rol)")
    List<Usuario> filtrar(@Param("username") String username, @Param("rol") String rol);
}