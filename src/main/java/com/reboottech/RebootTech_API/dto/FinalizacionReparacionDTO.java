package com.reboottech.RebootTech_API.dto;

public class FinalizacionReparacionDTO {

    private String solucionRealizada;
    private String pruebasRealizadas;

    public FinalizacionReparacionDTO() {
    }

    public FinalizacionReparacionDTO(
            String solucionRealizada,
            String pruebasRealizadas) {

        this.solucionRealizada = solucionRealizada;
        this.pruebasRealizadas = pruebasRealizadas;
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
}
