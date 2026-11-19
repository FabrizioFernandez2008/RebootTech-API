package com.reboottech.RebootTech_API.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reboottech.RebootTech_API.modelo.Credencial;

public interface CredencialRepositorio extends JpaRepository<Credencial, String> {

    Optional<Credencial> findByUsuarioIgnoreCase(String usuario);
}
