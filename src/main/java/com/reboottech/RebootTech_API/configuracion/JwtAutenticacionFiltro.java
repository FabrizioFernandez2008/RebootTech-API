/*
 * Este filtro intercepta las solicitudes HTTP para comprobar si contienen
 * un token JWT válido. Si el token es correcto, identifica al trabajador
 * y establece su autenticación para permitir el acceso a los recursos
 * protegidos según los permisos de su rol.
 */

package com.reboottech.RebootTech_API.configuracion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAutenticacionFiltro extends OncePerRequestFilter {

    private final JwtServicio jwtServicio;

    public JwtAutenticacionFiltro(JwtServicio jwtServicio) {
        this.jwtServicio = jwtServicio;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            FilterChain cadena) throws ServletException, IOException {

        String encabezado = solicitud.getHeader("Authorization");

        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            cadena.doFilter(solicitud, respuesta);
            return;
        }

        String token = encabezado.substring(7);

        if (jwtServicio.validarToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String usuario = jwtServicio.obtenerUsuario(token);
            String dni = jwtServicio.obtenerDni(token);
            String rol = jwtServicio.obtenerRol(token);

            UsuarioAutenticado usuarioAutenticado =
                    new UsuarioAutenticado(dni, usuario, rol);

            var autenticacion = new UsernamePasswordAuthenticationToken(
                    usuarioAutenticado,
                    null,
                    List.of(new SimpleGrantedAuthority(
                            "ROLE_" + rol.toUpperCase(Locale.ROOT)))
            );

            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

        cadena.doFilter(solicitud, respuesta);
    }
}
