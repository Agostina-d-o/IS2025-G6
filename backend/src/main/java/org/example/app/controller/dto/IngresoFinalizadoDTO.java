package org.example.app.controller.dto;

import java.time.format.DateTimeFormatter;

public record IngresoFinalizadoDTO(
        long id,
        String nivelEmergencia,
        String paciente,
        String fechaIngreso,
        String diagnostico
) {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static IngresoFinalizadoDTO from(org.example.domain.Ingreso i) {
        String nombreCompleto = i.getPaciente().getNombre() + " " + i.getPaciente().getApellido();
        String fechaHora = i.getFechaIngreso().format(FORMATTER);

        return new IngresoFinalizadoDTO(
                i.getId(),
                i.getNivelEmergencia().name(),
                nombreCompleto,
                fechaHora,
                i.getDiagnostico()
        );
    }
}
