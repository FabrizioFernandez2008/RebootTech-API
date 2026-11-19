package com.reboottech.RebootTech_API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteSolicitudDTO {

    @NotBlank(message = "El DNI es obligatorio.")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener 8 dígitos.")
    private String dni;

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombreCompleto;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(
            regexp = "[0-9+() -]{7,15}",
            message = "El teléfono debe tener entre 7 y 15 caracteres válidos."
    )
    private String telefono;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;

    public ClienteSolicitudDTO() {
    }

    public ClienteSolicitudDTO(
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
