package com.reboottech.RebootTech_API.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reboottech.RebootTech_API.configuracion.JwtServicio;
import com.reboottech.RebootTech_API.dto.InicioSesionDTO;
import com.reboottech.RebootTech_API.dto.InicioSesionRespuestaDTO;
import com.reboottech.RebootTech_API.dto.TrabajadorRespuestaDTO;
import com.reboottech.RebootTech_API.excepcion.CredencialesInvalidasException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;
import com.reboottech.RebootTech_API.modelo.Trabajador;
import com.reboottech.RebootTech_API.servicio.AutenticacionServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/autenticacion")
public class AutenticacionControlador {

    private final AutenticacionServicio autenticacionServicio;
    private final JwtServicio jwtServicio;

    public AutenticacionControlador(
            AutenticacionServicio autenticacionServicio,
            JwtServicio jwtServicio) {
        this.autenticacionServicio = autenticacionServicio;
        this.jwtServicio = jwtServicio;
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<InicioSesionRespuestaDTO> iniciarSesion(
            @Valid @RequestBody InicioSesionDTO datos) {

        if (datos == null) {
            throw new ReglaNegocioException(
                    "Debe proporcionar las credenciales de acceso.");
        }

        Trabajador trabajador = autenticacionServicio.iniciarSesion(
                datos.getUsuario(),
                datos.getContrasena()
        ).orElseThrow(() ->
                new CredencialesInvalidasException(
                        "Usuario o contraseña incorrectos."));

        String token = jwtServicio.generarToken(trabajador);

        TrabajadorRespuestaDTO trabajadorDTO = new TrabajadorRespuestaDTO(
                trabajador.getDni(),
                trabajador.getNombres(),
                trabajador.getApellidos(),
                trabajador.getTelefono(),
                trabajador.getDireccion(),
                trabajador.getUsuario(),
                trabajador.getRol(),
                trabajador.isEstado()
        );

        InicioSesionRespuestaDTO respuesta = new InicioSesionRespuestaDTO(
                token,
                "Bearer",
                trabajadorDTO
        );

        return ResponseEntity.ok(respuesta);
    }
}
