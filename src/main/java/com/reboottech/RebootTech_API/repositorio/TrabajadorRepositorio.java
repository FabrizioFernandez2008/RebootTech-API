package com.reboottech.RebootTech_API.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reboottech.RebootTech_API.modelo.Trabajador;

public interface TrabajadorRepositorio extends JpaRepository<Trabajador, String> {

    Optional<Trabajador> findByUsuarioIgnoreCase(String usuario);

    boolean existsByUsuarioIgnoreCase(String usuario);
}
