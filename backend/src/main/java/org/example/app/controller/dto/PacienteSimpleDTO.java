package org.example.app.controller.dto;

import org.example.domain.Paciente;
import org.example.domain.valueobject.AfiliacionObraSocial;

public record PacienteSimpleDTO(
        String cuil,
        String nombreCompleto,
        String codigoObraSocial,
        String nombreObraSocial,
        String numeroAfiliado
) {
    public static PacienteSimpleDTO from(Paciente p) {
        String nom = (p.getNombre() == null ? "" : p.getNombre());
        String ape = (p.getApellido() == null ? "" : p.getApellido());
        String nombreCompleto = (nom + " " + ape).trim();

        String codOS = null;
        String nomOS = null;
        String nroAf = null;

        AfiliacionObraSocial af = p.getAfiliacionObraSocial();
        if (af != null && af.getObraSocial() != null) {
            codOS = af.getObraSocial().getCodigo();
            nomOS = af.getObraSocial().getNombre();
            nroAf = af.getNumeroAfiliado();
        }

        return new PacienteSimpleDTO(
                p.getCuil(),
                nombreCompleto,
                codOS,
                nomOS,
                nroAf
        );
    }
}
