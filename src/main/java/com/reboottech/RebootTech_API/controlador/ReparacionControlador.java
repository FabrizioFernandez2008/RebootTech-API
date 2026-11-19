package com.reboottech.RebootTech_API.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reboottech.RebootTech_API.configuracion.UsuarioAutenticado;
import com.reboottech.RebootTech_API.dto.ConsultaPublicaReparacionDTO;
import com.reboottech.RebootTech_API.dto.DiagnosticoDTO;
import com.reboottech.RebootTech_API.dto.FinalizacionReparacionDTO;
import com.reboottech.RebootTech_API.dto.ReparacionRespuestaDTO;
import com.reboottech.RebootTech_API.dto.ReparacionSolicitudDTO;
import com.reboottech.RebootTech_API.dto.RepuestosDTO;
import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Reparacion;
import com.reboottech.RebootTech_API.servicio.ReparacionServicio;

@RestController
@RequestMapping("/api/reparaciones")
public class ReparacionControlador {

    private final ReparacionServicio reparacionServicio;

    public ReparacionControlador(ReparacionServicio reparacionServicio) {
        this.reparacionServicio = reparacionServicio;
    }

    @GetMapping
    public ResponseEntity<List<ReparacionRespuestaDTO>> obtenerReparaciones() {

        List<ReparacionRespuestaDTO> respuesta =
                reparacionServicio.obtenerReparaciones()
                        .stream()
                        .map(this::convertirARespuesta)
                        .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ReparacionRespuestaDTO> buscarPorCodigo(
            @PathVariable String codigo) {

        Reparacion reparacion = reparacionServicio.buscarPorCodigo(codigo)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Reparación no encontrada."));

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @GetMapping("/publica/{codigo}")
    public ResponseEntity<ConsultaPublicaReparacionDTO> consultarPublicamente(
            @PathVariable String codigo) {

        Reparacion reparacion = reparacionServicio.buscarPorCodigo(codigo)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró una reparación con ese código de atención."));

        ConsultaPublicaReparacionDTO respuesta =
                new ConsultaPublicaReparacionDTO(
                        reparacion.getCodigoAtencion(),
                        reparacion.getTipoEquipo(),
                        reparacion.getMarca(),
                        reparacion.getModelo(),
                        reparacion.getProblemaReportado(),
                        reparacion.getFechaIngreso(),
                        reparacion.getEstado()
                );

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ReparacionRespuestaDTO>> buscarReparaciones(
            @RequestParam String criterio,
            @RequestParam String busqueda) {

        List<ReparacionRespuestaDTO> respuesta =
                reparacionServicio.buscarReparaciones(criterio, busqueda)
                        .stream()
                        .map(this::convertirARespuesta)
                        .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ReparacionRespuestaDTO>> obtenerDisponibles() {

        List<ReparacionRespuestaDTO> respuesta =
                reparacionServicio.obtenerReparacionesDisponibles()
                        .stream()
                        .map(this::convertirARespuesta)
                        .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mis-reparaciones")
    public ResponseEntity<List<ReparacionRespuestaDTO>> obtenerAsignadas(
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        List<ReparacionRespuestaDTO> respuesta =
                reparacionServicio.obtenerReparacionesAsignadas(usuario.dni())
                        .stream()
                        .map(this::convertirARespuesta)
                        .toList();

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<ReparacionRespuestaDTO> registrarReparacion(
            @RequestBody ReparacionSolicitudDTO datos) {

        validarDatos(datos);

        Reparacion reparacion = new Reparacion();

        reparacion.setDniCliente(datos.getDniCliente());
        reparacion.setTipoEquipo(datos.getTipoEquipo());
        reparacion.setMarca(datos.getMarca());
        reparacion.setModelo(datos.getModelo());
        reparacion.setNumeroSerie(datos.getNumeroSerie());
        reparacion.setAccesorios(datos.getAccesorios());
        reparacion.setProblemaReportado(datos.getProblemaReportado());
        reparacion.setObservaciones(datos.getObservaciones());

        Reparacion registrada =
                reparacionServicio.registrarReparacion(reparacion);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirARespuesta(registrada));
    }

    @PatchMapping("/{codigo}/asignar")
    public ResponseEntity<ReparacionRespuestaDTO> asignarReparacion(
            @PathVariable String codigo,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        Reparacion reparacion =
                reparacionServicio.asignarReparacion(codigo, usuario.dni());

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @PatchMapping("/{codigo}/examinar")
    public ResponseEntity<ReparacionRespuestaDTO> marcarComoExaminando(
            @PathVariable String codigo,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        Reparacion reparacion =
                reparacionServicio.marcarComoExaminando(
                        codigo,
                        usuario.dni());

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @PatchMapping("/{codigo}/diagnostico")
    public ResponseEntity<ReparacionRespuestaDTO> guardarDiagnostico(
            @PathVariable String codigo,
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            @RequestBody DiagnosticoDTO datos) {

        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar los datos del diagnóstico.");
        }

        Reparacion reparacion = reparacionServicio.guardarDiagnostico(
                codigo,
                usuario.dni(),
                datos.getDiagnostico()
        );

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @PatchMapping("/{codigo}/repuestos")
    public ResponseEntity<ReparacionRespuestaDTO> guardarRepuestos(
            @PathVariable String codigo,
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            @RequestBody RepuestosDTO datos) {

        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar los datos de los repuestos.");
        }

        Reparacion reparacion = reparacionServicio.guardarRepuestos(
                codigo,
                usuario.dni(),
                datos.getObservaciones()
        );

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @PatchMapping("/{codigo}/confirmar")
    public ResponseEntity<ReparacionRespuestaDTO> confirmarReparacion(
            @PathVariable String codigo) {

        Reparacion reparacion =
                reparacionServicio.confirmarReparacion(codigo);

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @PatchMapping("/{codigo}/finalizar")
    public ResponseEntity<ReparacionRespuestaDTO> finalizarReparacion(
            @PathVariable String codigo,
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            @RequestBody FinalizacionReparacionDTO datos) {

        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar los datos de finalización.");
        }

        Reparacion reparacion = reparacionServicio.finalizarReparacion(
                codigo,
                usuario.dni(),
                datos.getSolucionRealizada(),
                datos.getPruebasRealizadas()
        );

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @PatchMapping("/{codigo}/terminar")
    public ResponseEntity<ReparacionRespuestaDTO> terminarReparacion(
            @PathVariable String codigo) {

        Reparacion reparacion =
                reparacionServicio.terminarReparacion(codigo);

        return ResponseEntity.ok(convertirARespuesta(reparacion));
    }

    @GetMapping("/estadisticas/registradas")
    public ResponseEntity<Long> contarRegistradas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        return ResponseEntity.ok(
                reparacionServicio.contarSolicitudesRegistradas(inicio, fin)
        );
    }

    @GetMapping("/estadisticas/completadas")
    public ResponseEntity<Long> contarCompletadas(
            @RequestParam String inicio,
            @RequestParam String fin) {

        return ResponseEntity.ok(
                reparacionServicio.contarSolicitudesCompletadas(inicio, fin)
        );
    }

    @GetMapping("/estadisticas/en-espera")
    public ResponseEntity<Long> contarEnEspera(
            @RequestParam String inicio,
            @RequestParam String fin) {

        return ResponseEntity.ok(
                reparacionServicio.contarSolicitudesEnEspera(inicio, fin)
        );
    }

    private void validarDatos(ReparacionSolicitudDTO datos) {
        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar los datos de la reparación.");
        }
    }

    private ReparacionRespuestaDTO convertirARespuesta(
            Reparacion reparacion) {

        return new ReparacionRespuestaDTO(
                reparacion.getCodigoAtencion(),
                reparacion.getDniCliente(),
                reparacion.getTipoEquipo(),
                reparacion.getMarca(),
                reparacion.getModelo(),
                reparacion.getNumeroSerie(),
                reparacion.getAccesorios(),
                reparacion.getProblemaReportado(),
                reparacion.getObservaciones(),
                reparacion.getFechaIngreso(),
                reparacion.getEstado(),
                reparacion.getDniTecnico(),
                reparacion.getDiagnostico(),
                reparacion.getSolucionRealizada(),
                reparacion.getPruebasRealizadas(),
                reparacion.getFechaFinalizacion()
        );
    }
}
