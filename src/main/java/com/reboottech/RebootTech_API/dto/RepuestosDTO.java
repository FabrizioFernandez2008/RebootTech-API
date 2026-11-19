package com.reboottech.RebootTech_API.dto;

public class RepuestosDTO {

    private String observaciones;

    public RepuestosDTO() {
    }

    public RepuestosDTO(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
