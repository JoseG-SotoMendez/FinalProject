package com.unicordoba.FinalProject;

import com.unicordoba.FinalProject.entity.Proveedor;
import com.unicordoba.FinalProject.entity.Sede;
import com.unicordoba.FinalProject.repository.ProveedorRepository;
import com.unicordoba.FinalProject.service.SedeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class FinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

    // Modificamos aquí para pedir también el Repositorio de Proveedores
    @Bean
    CommandLineRunner initData(SedeService sedeService, ProveedorRepository proveedorRepository) {
        return args -> {

            // --- 1. Crear SEDE (Si no existe) ---
            // Un truco: verificamos si ya hay sedes para no duplicar cada vez que reinicias
            if (sedeService.obtenerTodas().isEmpty()) {
                Sede sedePrincipal = new Sede();
                sedePrincipal.setNombre("Sede Centro - Montería");
                sedePrincipal.setDireccion("Calle 27 con 3");
                sedePrincipal.setCiudad("Montería");
                sedePrincipal.setTelefono("3001234567");
                sedePrincipal.setLatitud(new BigDecimal("8.7500000"));
                sedePrincipal.setLongitud(new BigDecimal("-75.8800000"));
                sedePrincipal.setHorario("8:00 AM - 10:00 PM");

                sedeService.guardarSede(sedePrincipal);
                System.out.println(">>> ¡SEDE DE PRUEBA GUARDADA! <<<");
            }

            // --- 2. Crear PROVEEDOR (Necesario para Compras) ---
            if (proveedorRepository.count() == 0) {
                Proveedor prov = new Proveedor();
                prov.setNombre("Distribuidora Cordoba SAS");
                prov.setContacto("Carlos Gerente");
                prov.setTelefono("3005559988");
                prov.setEmail("ventas@distribuidora.com");

                proveedorRepository.save(prov);
                System.out.println(">>> ¡PROVEEDOR DE PRUEBA GUARDADO! <<<");
            }
        };
    }
}