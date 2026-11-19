package com.reboottech.RebootTech_API.dto;

public class TrabajadorRespuestaDTO {

    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String usuario;
    private String rol;
    private boolean estado;

    public TrabajadorRespuestaDTO() {
    }

    public TrabajadorRespuestaDTO(
            String dni,
            String nombres,
            String apellidos,
            String telefono,
            String direccion,
            String usuario,
            String rol,
            boolean estado) {

        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.usuario = usuario;
        this.rol = rol;
        this.estado = estado;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
