package com.reboottech.RebootTech_API.dto;

public class ClienteRespuestaDTO {

    private String dni;
    private String nombreCompleto;
    private String telefono;
    private String correo;

    public ClienteRespuestaDTO() {
    }

    public ClienteRespuestaDTO(
            String dni,
            String nombreCompleto,
            String telefono,
            String correo) {

        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
