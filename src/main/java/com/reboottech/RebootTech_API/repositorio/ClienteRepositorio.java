package com.reboottech.RebootTech_API.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reboottech.RebootTech_API.modelo.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

}
