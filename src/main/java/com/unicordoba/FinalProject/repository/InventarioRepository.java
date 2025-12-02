package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Inventario;
import com.unicordoba.FinalProject.entity.Producto;
import com.unicordoba.FinalProject.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.cantidad < 10")
    Long contarProductosBajoStock();
    Optional<Inventario> findBySedeAndProducto(Sede sede, Producto producto);


}