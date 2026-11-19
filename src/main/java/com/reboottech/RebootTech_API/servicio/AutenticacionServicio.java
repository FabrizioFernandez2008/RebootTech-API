package com.reboottech.RebootTech_API.servicio;

import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reboottech.RebootTech_API.modelo.Credencial;
import com.reboottech.RebootTech_API.modelo.Trabajador;
import com.reboottech.RebootTech_API.repositorio.CredencialRepositorio;
import com.reboottech.RebootTech_API.repositorio.TrabajadorRepositorio;

@Service
@Transactional(readOnly = true)
public class AutenticacionServicio {

    private final CredencialRepositorio credencialRepositorio;
    private final TrabajadorRepositorio trabajadorRepositorio;
    private final PasswordEncoder passwordEncoder;

    public AutenticacionServicio(
            CredencialRepositorio credencialRepositorio,
            TrabajadorRepositorio trabajadorRepositorio,
            PasswordEncoder passwordEncoder) {

        this.credencialRepositorio = credencialRepositorio;
        this.trabajadorRepositorio = trabajadorRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Trabajador> iniciarSesion(
            String usuario,
            String contrasena) {

        if (usuario == null || usuario.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            return Optional.empty();
        }

        String usuarioNormalizado = usuario.trim().toLowerCase(Locale.ROOT);

        Optional<Credencial> resultado =
                credencialRepositorio.findByUsuarioIgnoreCase(
                        usuarioNormalizado);

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        Credencial credencial = resultado.get();

        if (!credencial.isEstado()) {
            return Optional.empty();
        }

        if (!passwordEncoder.matches(
                contrasena,
                credencial.getContrasena())) {
            return Optional.empty();
        }

        Optional<Trabajador> resultadoTrabajador =
                trabajadorRepositorio.findByUsuarioIgnoreCase(
                        usuarioNormalizado);

        if (resultadoTrabajador.isEmpty()) {
            return Optional.empty();
        }

        Trabajador trabajador = resultadoTrabajador.get();

        if (!trabajador.isEstado()
                || !trabajador.getRol().equalsIgnoreCase(credencial.getRol())) {
            return Optional.empty();
        }

        return Optional.of(trabajador);
    }
}
