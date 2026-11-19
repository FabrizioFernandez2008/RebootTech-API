package com.reboottech.RebootTech_API.dto;

public class ReparacionSolicitudDTO {

    private String dniCliente;
    private String tipoEquipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String accesorios;
    private String problemaReportado;
    private String observaciones;

    public ReparacionSolicitudDTO() {
    }

    public ReparacionSolicitudDTO(
            String dniCliente,
            String tipoEquipo,
            String marca,
            String modelo,
            String numeroSerie,
            String accesorios,
            String problemaReportado,
            String observaciones) {

        this.dniCliente = dniCliente;
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.accesorios = accesorios;
        this.problemaReportado = problemaReportado;
        this.observaciones = observaciones;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(String accesorios) {
        this.accesorios = accesorios;
    }

    public String getProblemaReportado() {
        return problemaReportado;
    }

    public void setProblemaReportado(String problemaReportado) {
        this.problemaReportado = problemaReportado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
