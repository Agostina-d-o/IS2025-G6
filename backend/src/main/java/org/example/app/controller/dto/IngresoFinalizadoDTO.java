package org.example.app.controller.dto;

import java.time.format.DateTimeFormatter;

public record IngresoFinalizadoDTO(
        long id,
        String informe,
        String nivelEmergencia,
        String estado,
        String paciente,
        String fechaIngreso,
        String diagnostico,
        String enfermera,
        String medico,
        String temperatura,
        String frecuenciaCardiaca,
        String frecuenciaRespiratoria,
        String tensionArterial
) {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static IngresoFinalizadoDTO from(org.example.domain.Ingreso i) {

        String nombrePaciente = i.getPaciente().getNombre() + " " + i.getPaciente().getApellido();
        String nombreEnfermera = i.getEnfermera() != null ? i.getEnfermera().getNombreCompleto() : "—";
        String nombreMedico    = i.getMedico()    != null ? i.getMedico().getNombreCompleto()    : "—";

        String fechaHora = i.getFechaIngreso().format(FORMATTER);

        return new IngresoFinalizadoDTO(
                i.getId(),
                i.getInforme(),
                i.getNivelEmergencia().name(),
                i.getEstado().name(),
                nombrePaciente,
                fechaHora,
                i.getDiagnostico(),
                nombreEnfermera,
                nombreMedico,
                (i.getTemperatura() != null ? i.getTemperatura().getValorFormateado() : "—"),
                (i.getFrecuenciaCardiaca() != null ? i.getFrecuenciaCardiaca().getValorFormateado() : "—"),
                (i.getFrecuenciaRespiratoria() != null ? i.getFrecuenciaRespiratoria().getValorFormateado() : "—"),
                (i.getTensionArterial() != null ? i.getTensionArterial().getValorFormateado() : "—")
        );
    }
}
