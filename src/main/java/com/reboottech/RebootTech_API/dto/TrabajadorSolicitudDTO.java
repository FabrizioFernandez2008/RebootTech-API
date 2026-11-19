package com.reboottech.RebootTech_API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TrabajadorSolicitudDTO {

    @NotBlank(message = "El DNI es obligatorio.")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener 8 dígitos.")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres.")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe contener 9 dígitos.")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 150, message = "La dirección no puede superar los 150 caracteres.")
    private String direccion;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El usuario no puede superar los 50 caracteres.")
    private String usuario;

    @NotBlank(message = "El rol es obligatorio.")
    @Pattern(
        regexp = "(?i)administrador|recepcionista|tecnico",
        message = "El rol debe ser administrador, recepcionista o tecnico."
    )
    private String rol;

    @NotNull(message = "Debe indicar el estado de la cuenta.")
    private Boolean estado;

    private String contrasena;

    public TrabajadorSolicitudDTO() {
    }

    public TrabajadorSolicitudDTO(
            String dni,
            String nombres,
            String apellidos,
            String telefono,
            String direccion,
            String usuario,
            String rol,
            Boolean estado,
            String contrasena) {

        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.usuario = usuario;
        this.rol = rol;
        this.estado = estado;
        this.contrasena = contrasena;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
