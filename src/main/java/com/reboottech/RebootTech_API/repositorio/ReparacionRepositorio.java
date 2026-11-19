package com.reboottech.RebootTech_API.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reboottech.RebootTech_API.modelo.Reparacion;

public interface ReparacionRepositorio extends JpaRepository<Reparacion, String> {

    boolean existsByDniCliente(String dniCliente);

    boolean existsByDniTecnico(String dniTecnico);
}
