package com.reboottech.RebootTech_API.dto;

public class InicioSesionRespuestaDTO {

    private String token;
    private String tipo;
    private TrabajadorRespuestaDTO trabajador;

    public InicioSesionRespuestaDTO() {
    }

    public InicioSesionRespuestaDTO(
            String token,
            String tipo,
            TrabajadorRespuestaDTO trabajador) {
        this.token = token;
        this.tipo = tipo;
        this.trabajador = trabajador;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public TrabajadorRespuestaDTO getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(TrabajadorRespuestaDTO trabajador) {
        this.trabajador = trabajador;
    }
}
