/*
 * Esta clase configura la seguridad de la API y define las reglas de acceso
 * a sus endpoints según el rol del trabajador autenticado. También establece
 * qué rutas son públicas y cuáles requieren autenticación mediante JWT.
 */

package com.reboottech.RebootTech_API.configuracion;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {

    private final JwtAutenticacionFiltro jwtAutenticacionFiltro;

    public SeguridadConfiguracion(JwtAutenticacionFiltro jwtAutenticacionFiltro) {
        this.jwtAutenticacionFiltro = jwtAutenticacionFiltro;
    }

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfiguracion()))
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(autorizacion -> autorizacion

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/autenticacion/iniciar-sesion"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/reparaciones/publica/**"
                ).permitAll()

                .requestMatchers("/api/trabajadores/**")
                    .hasRole("ADMINISTRADOR")

                .requestMatchers("/api/clientes/**")
                    .hasRole("RECEPCIONISTA")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/reparaciones/estadisticas/**"
                ).hasRole("ADMINISTRADOR")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/reparaciones/disponibles",
                    "/api/reparaciones/mis-reparaciones"
                ).hasRole("TECNICO")

                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/reparaciones/*/asignar",
                    "/api/reparaciones/*/examinar",
                    "/api/reparaciones/*/diagnostico",
                    "/api/reparaciones/*/repuestos",
                    "/api/reparaciones/*/finalizar"
                ).hasRole("TECNICO")

                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/reparaciones/*/confirmar",
                    "/api/reparaciones/*/terminar"
                ).hasRole("RECEPCIONISTA")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/reparaciones"
                ).hasRole("RECEPCIONISTA")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/reparaciones",
                    "/api/reparaciones/buscar"
                ).hasRole("RECEPCIONISTA")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/reparaciones/*"
                ).hasAnyRole("RECEPCIONISTA", "TECNICO")

                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAutenticacionFiltro,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguracion() {

        CorsConfiguration configuracion = new CorsConfiguration();

        configuracion.setAllowedOrigins(
            List.of("http://localhost:4200")
        );

        configuracion.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuracion.setAllowedHeaders(
            List.of("Authorization", "Content-Type")
        );

        configuracion.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource origen =
            new UrlBasedCorsConfigurationSource();

        origen.registerCorsConfiguration("/**", configuracion);

        return origen;
    }

    @Bean
    public FilterRegistrationBean<JwtAutenticacionFiltro> registroFiltroJwt(
            JwtAutenticacionFiltro filtro) {

        FilterRegistrationBean<JwtAutenticacionFiltro> registro =
            new FilterRegistrationBean<>(filtro);

        registro.setEnabled(false);

        return registro;
    }
}