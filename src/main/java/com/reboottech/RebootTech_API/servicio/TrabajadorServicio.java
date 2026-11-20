package com.reboottech.RebootTech_API.servicio;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Credencial;
import com.reboottech.RebootTech_API.modelo.Trabajador;
import com.reboottech.RebootTech_API.repositorio.CredencialRepositorio;
import com.reboottech.RebootTech_API.repositorio.ReparacionRepositorio;
import com.reboottech.RebootTech_API.repositorio.TrabajadorRepositorio;

@Service
@Transactional
public class TrabajadorServicio {

    private final TrabajadorRepositorio trabajadorRepositorio;
    private final CredencialRepositorio credencialRepositorio;
    private final ReparacionRepositorio reparacionRepositorio;
    private final PasswordEncoder passwordEncoder;

    public TrabajadorServicio(
            TrabajadorRepositorio trabajadorRepositorio,
            CredencialRepositorio credencialRepositorio,
            ReparacionRepositorio reparacionRepositorio,
            PasswordEncoder passwordEncoder) {

        this.trabajadorRepositorio = trabajadorRepositorio;
        this.credencialRepositorio = credencialRepositorio;
        this.reparacionRepositorio = reparacionRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Trabajador> obtenerTrabajadores() {
        return trabajadorRepositorio.findAll();
    }

    public Optional<Trabajador> buscarPorDni(String dni) {
        return trabajadorRepositorio.findById(dni);
    }

    public Trabajador guardarTrabajador(
            Trabajador trabajador,
            String dniOriginal,
            String contrasena,
            String usuarioActual) {

        validarTrabajador(trabajador);

        boolean esNuevo = dniOriginal == null;

        if (esNuevo && trabajadorRepositorio.existsById(trabajador.getDni())) {
            throw new ReglaNegocioException(
                    "El DNI ya se encuentra registrado.");
        }

        Trabajador anterior = null;

        if (!esNuevo) {
            anterior = obtenerPorDni(dniOriginal);

            if (!dniOriginal.equals(trabajador.getDni())) {
                throw new ReglaNegocioException(
                        "No se permite modificar el DNI del trabajador.");
            }
        }

        String usuario = trabajador.getUsuario()
                .trim()
                .toLowerCase(Locale.ROOT);

        String rol = trabajador.getRol()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (trabajadorRepositorio.existsByUsuarioIgnoreCase(usuario)
                && (anterior == null
                || !anterior.getUsuario().equalsIgnoreCase(usuario))) {

            throw new ReglaNegocioException(
                    "El nombre de usuario ya está registrado.");
        }

        validarModificacionPropia(
                anterior,
                usuario,
                rol,
                trabajador.isEstado(),
                usuarioActual
        );

        if (anterior != null
                && anterior.isEstado()
                && anterior.getRol().equalsIgnoreCase("administrador")
                && (!rol.equals("administrador") || !trabajador.isEstado())) {

            validarUltimoAdministrador(anterior);
        }

        validarContrasena(contrasena, esNuevo);

        Credencial credencialAnterior = anterior == null
                ? null
                : credencialRepositorio.findByUsuarioIgnoreCase(
                        anterior.getUsuario())
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "No se encontró la credencial del trabajador."));

        trabajador.setNombres(trabajador.getNombres().trim());
        trabajador.setApellidos(trabajador.getApellidos().trim());
        trabajador.setTelefono(trabajador.getTelefono().trim());
        trabajador.setDireccion(trabajador.getDireccion().trim());
        trabajador.setUsuario(usuario);
        trabajador.setRol(rol);

        Trabajador trabajadorGuardado =
                trabajadorRepositorio.save(trabajador);

        String contrasenaCifrada;

        if (contrasena != null && !contrasena.isBlank()) {
            contrasenaCifrada = passwordEncoder.encode(contrasena);
        } else {
            contrasenaCifrada = credencialAnterior.getContrasena();
        }

        Credencial credencial = new Credencial(
                trabajadorGuardado.getUsuario(),
                contrasenaCifrada,
                trabajadorGuardado.getRol(),
                trabajadorGuardado.isEstado()
        );

        if (credencialAnterior != null
                && !credencialAnterior.getUsuario()
                        .equals(trabajadorGuardado.getUsuario())) {

            credencialRepositorio.delete(credencialAnterior);
        }

        credencialRepositorio.save(credencial);

        return trabajadorGuardado;
    }

    public Trabajador desactivarTrabajador(
            String dni,
            String usuarioActual) {

        Trabajador trabajador = obtenerPorDni(dni);

        validarDesactivacion(trabajador, usuarioActual);

        trabajador.setEstado(false);
        trabajadorRepositorio.save(trabajador);

        Credencial credencial = credencialRepositorio
                .findByUsuarioIgnoreCase(trabajador.getUsuario())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Credencial no encontrada."));

        credencial.setEstado(false);
        credencialRepositorio.save(credencial);

        return trabajador;
    }

    public Trabajador activarTrabajador(
            String dni,
            String usuarioActual) {

        Trabajador trabajador = obtenerPorDni(dni);

        if (trabajador.isEstado()) {
            throw new ReglaNegocioException(
                    "El trabajador ya está activo.");
        }

        validarQueNoSeaLaCuentaActual(trabajador, usuarioActual);

        Credencial credencial = credencialRepositorio
                .findByUsuarioIgnoreCase(trabajador.getUsuario())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Credencial no encontrada."));

        trabajador.setEstado(true);
        trabajadorRepositorio.save(trabajador);

        credencial.setEstado(true);
        credencialRepositorio.save(credencial);

        return trabajador;
    }

    public void eliminarTrabajador(String dni, String usuarioActual) {
        Trabajador trabajador = obtenerPorDni(dni);

        validarQueNoSeaLaCuentaActual(trabajador, usuarioActual);
        validarUltimoAdministrador(trabajador);

        if (reparacionRepositorio.existsByDniTecnico(dni)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar un trabajador que tiene reparaciones asociadas. Desactive su cuenta en su lugar.");
        }

        credencialRepositorio.deleteById(trabajador.getUsuario());
        trabajadorRepositorio.delete(trabajador);
    }

    private void validarTrabajador(Trabajador trabajador) {

        if (trabajador == null
                || trabajador.getDni() == null
                || !trabajador.getDni().matches("\\d{8}")
                || trabajador.getNombres() == null
                || trabajador.getNombres().isBlank()
                || trabajador.getApellidos() == null
                || trabajador.getApellidos().isBlank()
                || trabajador.getTelefono() == null
                || !trabajador.getTelefono().matches("\\d{9}")
                || trabajador.getDireccion() == null
                || trabajador.getDireccion().isBlank()
                || trabajador.getUsuario() == null
                || trabajador.getUsuario().isBlank()
                || trabajador.getRol() == null
                || !List.of("administrador", "recepcionista", "tecnico")
                        .contains(trabajador.getRol().trim().toLowerCase(Locale.ROOT))) {

            throw new ReglaNegocioException(
                    "Los datos del trabajador son inválidos.");
        }
    }

    private void validarContrasena(String contrasena, boolean esNuevo) {

        if (esNuevo && (contrasena == null || contrasena.length() < 8)) {
            throw new ReglaNegocioException(
                    "La contraseña debe tener al menos 8 caracteres.");
        }

        if (!esNuevo && contrasena != null
                && !contrasena.isBlank()
                && contrasena.length() < 8) {

            throw new ReglaNegocioException(
                    "La nueva contraseña debe tener al menos 8 caracteres.");
        }
    }

    private void validarModificacionPropia(
            Trabajador anterior,
            String nuevoUsuario,
            String nuevoRol,
            boolean nuevoEstado,
            String usuarioActual) {

        if (anterior == null || usuarioActual == null
                || !anterior.getUsuario().equalsIgnoreCase(usuarioActual)) {
            return;
        }

        boolean modificaUsuario = !anterior.getUsuario()
                .equalsIgnoreCase(nuevoUsuario);

        boolean modificaRol = !anterior.getRol()
                .equalsIgnoreCase(nuevoRol);

        boolean modificaEstado = anterior.isEstado() != nuevoEstado;

        if (modificaUsuario || modificaRol || modificaEstado) {
            throw new ReglaNegocioException(
                    "No puedes modificar tu nombre de usuario, rol o estado desde tu propia cuenta.");
        }
    }

    private void validarDesactivacion(
            Trabajador trabajador,
            String usuarioActual) {

        if (!trabajador.isEstado()) {
            throw new ReglaNegocioException(
                    "El trabajador ya está desactivado.");
        }

        validarQueNoSeaLaCuentaActual(trabajador, usuarioActual);
        validarUltimoAdministrador(trabajador);
    }

    private void validarQueNoSeaLaCuentaActual(
            Trabajador trabajador,
            String usuarioActual) {

        if (usuarioActual != null
                && trabajador.getUsuario().equalsIgnoreCase(usuarioActual)) {

            throw new ReglaNegocioException(
                    "No puedes modificar tu propia cuenta mediante esta operación.");
        }
    }

    private void validarUltimoAdministrador(Trabajador trabajador) {

        if (trabajador.getRol().equalsIgnoreCase("administrador")
                && trabajador.isEstado()) {

            long administradoresActivos = trabajadorRepositorio.findAll()
                    .stream()
                    .filter(actual ->
                            actual.getRol().equalsIgnoreCase("administrador")
                            && actual.isEstado())
                    .count();

            if (administradoresActivos <= 1) {
                throw new ReglaNegocioException(
                        "No se puede eliminar, desactivar o quitar el rol al último administrador activo.");
            }
        }
    }

    private Trabajador obtenerPorDni(String dni) {
        return trabajadorRepositorio.findById(dni)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Trabajador no encontrado."));
    }
}
