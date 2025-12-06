package org.example.app.controller.dto;

public record IngresoFinalizadoDTO(
        long id,
        String nivelEmergencia,
        String paciente,
        String fechaIngreso,
        String diagnostico
) {
    public static IngresoFinalizadoDTO from(org.example.domain.Ingreso i) {
        String nombreCompleto = i.getPaciente().getNombre() + " " + i.getPaciente().getApellido();
        return new IngresoFinalizadoDTO(
                i.getId(),
                i.getNivelEmergencia().name(),
                nombreCompleto,
                i.getFechaIngreso().toString(),
                i.getDiagnostico()
        );
    }
}
