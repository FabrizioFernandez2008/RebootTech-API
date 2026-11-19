package com.reboottech.RebootTech_API.dto;

public class DiagnosticoDTO {

    private String diagnostico;

    public DiagnosticoDTO() {
    }

    public DiagnosticoDTO(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
