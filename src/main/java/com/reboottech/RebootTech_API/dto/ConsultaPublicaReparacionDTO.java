package com.reboottech.RebootTech_API.dto;

public class ConsultaPublicaReparacionDTO {

    private String codigoAtencion;
    private String tipoEquipo;
    private String marca;
    private String modelo;
    private String problemaReportado;
    private String fechaIngreso;
    private String estado;

    public ConsultaPublicaReparacionDTO() {
    }

    public ConsultaPublicaReparacionDTO(
            String codigoAtencion,
            String tipoEquipo,
            String marca,
            String modelo,
            String problemaReportado,
            String fechaIngreso,
            String estado) {

        this.codigoAtencion = codigoAtencion;
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.problemaReportado = problemaReportado;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public String getCodigoAtencion() {
        return codigoAtencion;
    }

    public void setCodigoAtencion(String codigoAtencion) {
        this.codigoAtencion = codigoAtencion;
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

    public String getProblemaReportado() {
        return problemaReportado;
    }

    public void setProblemaReportado(String problemaReportado) {
        this.problemaReportado = problemaReportado;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
