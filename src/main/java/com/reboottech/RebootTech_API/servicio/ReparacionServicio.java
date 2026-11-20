package com.reboottech.RebootTech_API.servicio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Reparacion;
import com.reboottech.RebootTech_API.modelo.Trabajador;
import com.reboottech.RebootTech_API.repositorio.ReparacionRepositorio;
import com.reboottech.RebootTech_API.repositorio.ClienteRepositorio;
import com.reboottech.RebootTech_API.repositorio.TrabajadorRepositorio;

@Service
@Transactional
public class ReparacionServicio {

    private final ReparacionRepositorio reparacionRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final TrabajadorRepositorio trabajadorRepositorio;

    public ReparacionServicio(
            ReparacionRepositorio reparacionRepositorio,
            ClienteRepositorio clienteRepositorio,
            TrabajadorRepositorio trabajadorRepositorio) {

        this.reparacionRepositorio = reparacionRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.trabajadorRepositorio = trabajadorRepositorio;
    }

    public List<Reparacion> obtenerReparaciones() {
        return reparacionRepositorio.findAll();
    }

    public Optional<Reparacion> buscarPorCodigo(String codigo) {
        return reparacionRepositorio.findById(codigo);
    }

    public List<Reparacion> buscarPorDni(String dni) {
        if (dni == null || dni.isBlank()) {
            return List.of();
        }

        return reparacionRepositorio.findAll().stream()
                .filter(reparacion ->
                        dni.equals(reparacion.getDniCliente()))
                .toList();
    }

    public List<Reparacion> buscarReparaciones(
            String criterio,
            String busqueda) {

        if (criterio == null || busqueda == null || busqueda.isBlank()) {
            return List.of();
        }

        String texto = busqueda.trim().toLowerCase(Locale.ROOT);

        return reparacionRepositorio.findAll().stream()
                .filter(reparacion -> {
                    if (criterio.equalsIgnoreCase("codigo")) {
                        return reparacion.getCodigoAtencion() != null
                                && reparacion.getCodigoAtencion()
                                        .toLowerCase(Locale.ROOT)
                                        .contains(texto);
                    }

                    if (criterio.equalsIgnoreCase("dni")) {
                        return reparacion.getDniCliente() != null
                                && reparacion.getDniCliente().contains(texto);
                    }

                    return false;
                })
                .toList();
    }

    public Reparacion registrarReparacion(Reparacion reparacion) {
        if (reparacion == null
                || reparacion.getDniCliente() == null
                || reparacion.getDniCliente().isBlank()
                || reparacion.getTipoEquipo() == null
                || reparacion.getTipoEquipo().isBlank()
                || reparacion.getProblemaReportado() == null
                || reparacion.getProblemaReportado().isBlank()) {

            throw new ReglaNegocioException(
                    "Debe completar los datos obligatorios de la reparación.");
        }

        if (!clienteRepositorio.existsById(reparacion.getDniCliente())) {
            throw new RecursoNoEncontradoException(
                    "El cliente no se encuentra registrado.");
        }

        String codigo = generarCodigoAtencion();

        reparacion.setCodigoAtencion(codigo);
        reparacion.setFechaIngreso(LocalDate.now().toString());
        reparacion.setEstado("En espera");
        reparacion.setDniTecnico(null);
        reparacion.setDiagnostico(null);
        reparacion.setSolucionRealizada(null);
        reparacion.setPruebasRealizadas(null);
        reparacion.setFechaFinalizacion(null);

        return reparacionRepositorio.save(reparacion);
    }

    public List<Reparacion> obtenerReparacionesDisponibles() {
        return reparacionRepositorio.findAll().stream()
                .filter(reparacion ->
                        "En espera".equals(reparacion.getEstado())
                        && reparacion.getDniTecnico() == null)
                .toList();
    }

    public List<Reparacion> obtenerReparacionesAsignadas(String dniTecnico) {
        if (dniTecnico == null || dniTecnico.isBlank()) {
            return List.of();
        }

        return reparacionRepositorio.findAll().stream()
                .filter(reparacion ->
                        dniTecnico.equals(reparacion.getDniTecnico())
                        && List.of(
                                "En espera",
                                "Examinando",
                                "Reparando",
                                "Esperando confirmación",
                                "Confirmado"
                        ).contains(reparacion.getEstado()))
                .toList();
    }

    public boolean tieneReparacionActiva(String dniTecnico) {
        return obtenerReparacionesAsignadas(dniTecnico).stream()
                .anyMatch(reparacion ->
                        List.of("En espera", "Examinando", "Reparando", "Confirmado")
                                .contains(reparacion.getEstado()));
    }

    public Reparacion asignarReparacion(
            String codigo,
            String dniTecnico) {

        Reparacion reparacion = obtenerPorCodigo(codigo);

        Trabajador tecnico = trabajadorRepositorio.findById(dniTecnico)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Técnico no encontrado."));

        if (!tecnico.isEstado()
                || !"tecnico".equalsIgnoreCase(tecnico.getRol())) {

            throw new ReglaNegocioException(
                    "El trabajador no es un técnico activo.");
        }

        if (!"En espera".equals(reparacion.getEstado())) {
            throw new ReglaNegocioException(
                    "La reparación no está disponible para su atención.");
        }

        if (reparacion.getDniTecnico() != null) {

            if (!dniTecnico.equals(reparacion.getDniTecnico())) {
                throw new ReglaNegocioException(
                        "Esta reparación ya está asignada a otro técnico.");
            }

            return reparacion;
        }

        if (tieneReparacionActiva(dniTecnico)) {
            throw new ReglaNegocioException(
                    "El técnico ya tiene una reparación pendiente.");
        }

        reparacion.setDniTecnico(dniTecnico);

        return reparacionRepositorio.save(reparacion);
    }
    
    public Reparacion marcarComoExaminando(
                String codigo,
                String dniTecnico) {

        Reparacion reparacion = obtenerPorCodigo(codigo);

        validarTecnicoAsignado(reparacion, dniTecnico);

        if (!List.of("En espera", "Confirmado").contains(reparacion.getEstado())) {
                throw new ReglaNegocioException(
                        "La reparación no se encuentra en espera.");
        }

        if (reparacion.getDiagnostico() != null
                && !reparacion.getDiagnostico().isBlank()) {

                reparacion.setEstado("Reparando");

        } else {
                reparacion.setEstado("Examinando");
        }

        return reparacionRepositorio.save(reparacion);
    }


    public Reparacion guardarDiagnostico(
            String codigo,
            String dniTecnico,
            String diagnostico) {

        Reparacion reparacion = obtenerPorCodigo(codigo);

        validarTecnicoAsignado(reparacion, dniTecnico);

        if (!List.of("En espera", "Examinando", "Reparando")
                .contains(reparacion.getEstado())) {

            throw new ReglaNegocioException(
                    "No se puede modificar el diagnóstico en este estado.");
        }

        if (diagnostico == null || diagnostico.isBlank()) {
            throw new ReglaNegocioException(
                    "El diagnóstico es obligatorio.");
        }

        reparacion.setDiagnostico(diagnostico.trim());
        reparacion.setEstado("Reparando");

        return reparacionRepositorio.save(reparacion);
    }

    public Reparacion guardarRepuestos(
            String codigo,
            String dniTecnico,
            String observaciones) {

        Reparacion reparacion = obtenerPorCodigo(codigo);

        validarTecnicoAsignado(reparacion, dniTecnico);

        if (!"Reparando".equals(reparacion.getEstado())) {
            throw new ReglaNegocioException(
                    "La reparación no se encuentra en estado Reparando.");
        }

        if (observaciones == null || observaciones.isBlank()) {
            throw new ReglaNegocioException(
                    "Debe indicar los repuestos necesarios.");
        }

        reparacion.setObservaciones(observaciones.trim());
        reparacion.setEstado("Esperando confirmación");

        return reparacionRepositorio.save(reparacion);
    }

    public Reparacion confirmarReparacion(String codigo) {
        Reparacion reparacion = obtenerPorCodigo(codigo);

        if (!"Esperando confirmación".equals(reparacion.getEstado())) {
            throw new ReglaNegocioException(
                    "La reparación no está esperando confirmación.");
        }

        reparacion.setEstado("Confirmado");

        return reparacionRepositorio.save(reparacion);
    }

    public Reparacion finalizarReparacion(
            String codigo,
            String dniTecnico,
            String solucion,
            String pruebas) {

        Reparacion reparacion = obtenerPorCodigo(codigo);

        validarTecnicoAsignado(reparacion, dniTecnico);

        if (!"Reparando".equals(reparacion.getEstado())) {
            throw new ReglaNegocioException(
                    "La reparación no se encuentra en estado Reparando.");
        }

        if (reparacion.getDiagnostico() == null
                || reparacion.getDiagnostico().isBlank()) {
            throw new ReglaNegocioException(
                    "Debe registrar el diagnóstico antes de finalizar la reparación.");
        }

        if (solucion == null || solucion.isBlank()
                || pruebas == null || pruebas.isBlank()) {

            throw new ReglaNegocioException(
                    "Debe completar la solución y las pruebas realizadas.");
        }

        reparacion.setSolucionRealizada(solucion.trim());
        reparacion.setPruebasRealizadas(pruebas.trim());
        reparacion.setFechaFinalizacion(LocalDate.now().toString());
        reparacion.setEstado("Listo para recojo");

        return reparacionRepositorio.save(reparacion);
    }

    public Reparacion terminarReparacion(String codigo) {
        Reparacion reparacion = obtenerPorCodigo(codigo);

        if (!"Listo para recojo".equals(reparacion.getEstado())) {
            throw new ReglaNegocioException(
                    "La reparación debe estar lista para recojo antes de marcarse como terminada.");
        }

        reparacion.setEstado("Terminado");

        return reparacionRepositorio.save(reparacion);
    }

    public long contarSolicitudesRegistradas(String inicio, String fin) {
        LocalDate fechaInicio = validarRangoFechas(inicio, fin);
        LocalDate fechaFin = LocalDate.parse(fin);

        return reparacionRepositorio.findAll().stream()
                .filter(reparacion ->
                        reparacion.getFechaIngreso() != null)
                .filter(reparacion -> {
                    LocalDate fecha = LocalDate.parse(
                            reparacion.getFechaIngreso());

                    return !fecha.isBefore(fechaInicio)
                            && !fecha.isAfter(fechaFin);
                })
                .count();
    }

    public long contarSolicitudesCompletadas(String inicio, String fin) {
        LocalDate fechaInicio = validarRangoFechas(inicio, fin);
        LocalDate fechaFin = LocalDate.parse(fin);

        return reparacionRepositorio.findAll().stream()
                .filter(reparacion ->
                        List.of("Listo para recojo", "Terminado")
                                .contains(reparacion.getEstado())
                        && reparacion.getFechaFinalizacion() != null)
                .filter(reparacion -> {
                    LocalDate fecha = LocalDate.parse(
                            reparacion.getFechaFinalizacion());

                    return !fecha.isBefore(fechaInicio)
                            && !fecha.isAfter(fechaFin);
                })
                .count();
    }

    public long contarSolicitudesEnEspera(String inicio, String fin) {
        LocalDate fechaInicio = validarRangoFechas(inicio, fin);
        LocalDate fechaFin = LocalDate.parse(fin);

        return reparacionRepositorio.findAll().stream()
                .filter(reparacion ->
                        List.of("En espera", "Esperando confirmación")
                                .contains(reparacion.getEstado())
                        && reparacion.getFechaIngreso() != null)
                .filter(reparacion -> {
                    LocalDate fecha = LocalDate.parse(
                            reparacion.getFechaIngreso());

                    return !fecha.isBefore(fechaInicio)
                            && !fecha.isAfter(fechaFin);
                })
                .count();
    }

    private Reparacion obtenerPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new ReglaNegocioException(
                    "Debe proporcionar un código de atención.");
        }

        return reparacionRepositorio.findById(codigo)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Reparación no encontrada."));
    }

    private void validarTecnicoAsignado(
            Reparacion reparacion,
            String dniTecnico) {

        if (dniTecnico == null
                || !dniTecnico.equals(reparacion.getDniTecnico())) {

            throw new ReglaNegocioException(
                    "No tienes autorización para modificar esta reparación.");
        }
    }

    private LocalDate validarRangoFechas(String inicio, String fin) {
        try {
            if (inicio == null || fin == null) {
                throw new ReglaNegocioException(
                        "Debe indicar las fechas de inicio y fin.");
            }

            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);

            if (fechaInicio.isAfter(fechaFin)) {
                throw new ReglaNegocioException(
                        "La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            return fechaInicio;

        } catch (DateTimeParseException e) {
            throw new ReglaNegocioException(
                    "El formato de fecha debe ser AAAA-MM-DD.");
        }
    }

    private String generarCodigoAtencion() {
        int siguiente = reparacionRepositorio.findAll().stream()
                .mapToInt(reparacion -> {
                    try {
                        return Integer.parseInt(
                                reparacion.getCodigoAtencion()
                                        .replace("RT-", ""));
                    } catch (NumberFormatException | NullPointerException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0) + 1;

        String codigo;

        do {
            codigo = "RT-" + String.format("%03d", siguiente++);
        } while (reparacionRepositorio.existsById(codigo));

        return codigo;
    }
}
