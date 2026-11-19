package com.reboottech.RebootTech_API.dto;

public class ReparacionRespuestaDTO {

    private String codigoAtencion;
    private String dniCliente;
    private String tipoEquipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String accesorios;
    private String problemaReportado;
    private String observaciones;
    private String fechaIngreso;
    private String estado;
    private String dniTecnico;
    private String diagnostico;
    private String solucionRealizada;
    private String pruebasRealizadas;
    private String fechaFinalizacion;

    public ReparacionRespuestaDTO() {
    }

    public ReparacionRespuestaDTO(
            String codigoAtencion,
            String dniCliente,
            String tipoEquipo,
            String marca,
            String modelo,
            String numeroSerie,
            String accesorios,
            String problemaReportado,
            String observaciones,
            String fechaIngreso,
            String estado,
            String dniTecnico,
            String diagnostico,
            String solucionRealizada,
            String pruebasRealizadas,
            String fechaFinalizacion) {

        this.codigoAtencion = codigoAtencion;
        this.dniCliente = dniCliente;
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.accesorios = accesorios;
        this.problemaReportado = problemaReportado;
        this.observaciones = observaciones;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
        this.dniTecnico = dniTecnico;
        this.diagnostico = diagnostico;
        this.solucionRealizada = solucionRealizada;
        this.pruebasRealizadas = pruebasRealizadas;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getCodigoAtencion() {
        return codigoAtencion;
    }

    public void setCodigoAtencion(String codigoAtencion) {
        this.codigoAtencion = codigoAtencion;
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

    public String getDniTecnico() {
        return dniTecnico;
    }

    public void setDniTecnico(String dniTecnico) {
        this.dniTecnico = dniTecnico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getSolucionRealizada() {
        return solucionRealizada;
    }

    public void setSolucionRealizada(String solucionRealizada) {
        this.solucionRealizada = solucionRealizada;
    }

    public String getPruebasRealizadas() {
        return pruebasRealizadas;
    }

    public void setPruebasRealizadas(String pruebasRealizadas) {
        this.pruebasRealizadas = pruebasRealizadas;
    }

    public String getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(String fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}
