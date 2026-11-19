package com.reboottech.RebootTech_API.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reboottech.RebootTech_API.configuracion.UsuarioAutenticado;
import com.reboottech.RebootTech_API.dto.TrabajadorSolicitudDTO;
import com.reboottech.RebootTech_API.dto.TrabajadorRespuestaDTO;
import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Trabajador;
import com.reboottech.RebootTech_API.servicio.TrabajadorServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorControlador {

    private final TrabajadorServicio trabajadorServicio;

    public TrabajadorControlador(TrabajadorServicio trabajadorServicio) {
        this.trabajadorServicio = trabajadorServicio;
    }

    @GetMapping
    public ResponseEntity<List<TrabajadorRespuestaDTO>> obtenerTrabajadores() {

        List<TrabajadorRespuestaDTO> respuesta = trabajadorServicio.obtenerTrabajadores()
                .stream()
                .map(this::convertirARespuesta)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<TrabajadorRespuestaDTO> buscarPorDni(
            @PathVariable String dni) {

        Trabajador trabajador = trabajadorServicio.buscarPorDni(dni)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Trabajador no encontrado."));

        return ResponseEntity.ok(convertirARespuesta(trabajador));
    }

    @PostMapping
    public ResponseEntity<TrabajadorRespuestaDTO> registrarTrabajador(
            @Valid @RequestBody TrabajadorSolicitudDTO datos) {

        Trabajador trabajador = convertirAEntidad(datos);

        Trabajador registrado = trabajadorServicio.guardarTrabajador(
                trabajador,
                null,
                datos.getContrasena(),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirARespuesta(registrado));
    }

    @PutMapping("/{dni}")
    public ResponseEntity<TrabajadorRespuestaDTO> actualizarTrabajador(
            @PathVariable String dni,
            @Valid @RequestBody TrabajadorSolicitudDTO datos,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        Trabajador trabajador = convertirAEntidad(datos);

        Trabajador actualizado = trabajadorServicio.guardarTrabajador(
                trabajador,
                dni,
                datos.getContrasena(),
                usuario.usuario()
        );

        return ResponseEntity.ok(convertirARespuesta(actualizado));
    }

    @PatchMapping("/{dni}/desactivar")
    public ResponseEntity<TrabajadorRespuestaDTO> desactivarTrabajador(
            @PathVariable String dni,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        Trabajador trabajador = trabajadorServicio.desactivarTrabajador(
                dni,
                usuario.usuario()
        );

        return ResponseEntity.ok(convertirARespuesta(trabajador));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> eliminarTrabajador(
            @PathVariable String dni,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        trabajadorServicio.eliminarTrabajador(dni, usuario.usuario());

        return ResponseEntity.noContent().build();
    }

    private Trabajador convertirAEntidad(TrabajadorSolicitudDTO datos) {

        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar los datos del trabajador.");
        }

        return new Trabajador(
                datos.getDni(),
                datos.getNombres(),
                datos.getApellidos(),
                datos.getTelefono(),
                datos.getDireccion(),
                datos.getUsuario(),
                datos.getRol(),
                datos.getEstado()
        );
    }

    private TrabajadorRespuestaDTO convertirARespuesta(Trabajador trabajador) {

        return new TrabajadorRespuestaDTO(
                trabajador.getDni(),
                trabajador.getNombres(),
                trabajador.getApellidos(),
                trabajador.getTelefono(),
                trabajador.getDireccion(),
                trabajador.getUsuario(),
                trabajador.getRol(),
                trabajador.isEstado()
        );
    }
}
