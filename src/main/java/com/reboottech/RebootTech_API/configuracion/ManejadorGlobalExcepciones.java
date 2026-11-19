/*
 * Esta clase centraliza el manejo de excepciones del backend para evitar
 * repetir el mismo tratamiento de errores en cada controlador. Permite
 * devolver respuestas HTTP con mensajes claros cuando ocurre un problema
 * durante el procesamiento de una solicitud.
 */

package com.reboottech.RebootTech_API.configuracion;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.reboottech.RebootTech_API.excepcion.CredencialesInvalidasException;
import com.reboottech.RebootTech_API.excepcion.RecursoNoEncontradoException;
import com.reboottech.RebootTech_API.excepcion.ReglaNegocioException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, Object>> manejarCredencialesInvalidas(
            CredencialesInvalidasException excepcion,
            HttpServletRequest solicitud) {

        return construirRespuesta(
                HttpStatus.UNAUTHORIZED,
                excepcion.getMessage(),
                solicitud.getRequestURI());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException excepcion,
            HttpServletRequest solicitud) {

        return construirRespuesta(
                HttpStatus.NOT_FOUND,
                excepcion.getMessage(),
                solicitud.getRequestURI());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio(
            ReglaNegocioException excepcion,
            HttpServletRequest solicitud) {

        return construirRespuesta(
                HttpStatus.CONFLICT,
                excepcion.getMessage(),
                solicitud.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarIntegridadDatos(
            DataIntegrityViolationException excepcion,
            HttpServletRequest solicitud) {

        return construirRespuesta(
                HttpStatus.CONFLICT,
                "La operación no pudo completarse porque los datos entran en conflicto con un registro existente.",
                solicitud.getRequestURI());
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<Map<String, Object>> manejarSolicitudInvalida(
            Exception excepcion,
            HttpServletRequest solicitud) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "Los datos enviados no son válidos.",
                solicitud.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            Exception excepcion,
            HttpServletRequest solicitud) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor.",
                solicitud.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus estado,
            String mensaje,
            String ruta) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("fecha", LocalDateTime.now().toString());
        respuesta.put("estado", estado.value());
        respuesta.put("error", estado.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        respuesta.put("ruta", ruta);

        return ResponseEntity.status(estado).body(respuesta);
    }
}
