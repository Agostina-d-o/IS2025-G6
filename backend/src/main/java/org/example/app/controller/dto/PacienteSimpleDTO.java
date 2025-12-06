package org.example.app.controller.dto;

import org.example.domain.Paciente;

public record PacienteSimpleDTO(String cuil, String nombreCompleto) {
    public static PacienteSimpleDTO from(Paciente p) {
        String nom = (p.getNombre()==null? "" : p.getNombre());
        String ape = (p.getApellido()==null? "" : p.getApellido());
        return new PacienteSimpleDTO(p.getCuil(), (nom + " " + ape).trim());
    }
}
