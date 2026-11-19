/*
 * Este servicio se encarga de generar y validar tokens JWT para la autenticación
 * de los trabajadores. Permite identificar al usuario que inició sesión y
 * mantener su acceso a los recursos protegidos de la API según su rol.
 */

package com.reboottech.RebootTech_API.configuracion;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.reboottech.RebootTech_API.modelo.Trabajador;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServicio {

    private final SecretKey claveSecreta;
    private final long duracionToken;

    public JwtServicio(
            @Value("${jwt.secret}") String secreto,
            @Value("${jwt.expiration}") long duracionToken) {

        this.claveSecreta = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secreto)
        );

        this.duracionToken = duracionToken;
    }

    public String generarToken(Trabajador trabajador) {

        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(
                fechaActual.getTime() + duracionToken
        );

        return Jwts.builder()
                .subject(trabajador.getUsuario())
                .claim("dni", trabajador.getDni())
                .claim("rol", trabajador.getRol())
                .issuedAt(fechaActual)
                .expiration(fechaExpiracion)
                .signWith(claveSecreta)
                .compact();
    }

    public String obtenerUsuario(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }

    public String obtenerDni(String token) {
        return obtenerClaims(token).get("dni", String.class);
    }

    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException excepcion) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {

        return Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
