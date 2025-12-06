package org.example.app.controller.dto;

public record IngresoPendienteDTO(
        long id,
        String informe,
        String nivelEmergencia,
        String estado,
        String fechaIngreso,
        String paciente,
        String enfermera,
        Float  temperatura,
        String frecuenciaCardiaca,
        String frecuenciaRespiratoria,
        String tensionArterial,
        int    posicion
) {
    public static IngresoPendienteDTO from(org.example.domain.Ingreso i, int posicion) {
        String nombreCompleto = i.getPaciente().getNombre() + " " + i.getPaciente().getApellido();
        String nombreCompletoEnfermera = i.getEnfermera().getNombre() + " " + i.getEnfermera().getApellido();
        return new IngresoPendienteDTO(
                i.getId(),
                i.getInforme(),
                i.getNivelEmergencia().name(),
                i.getEstado().name(),
                i.getFechaIngreso().toString(),
                nombreCompleto,
                nombreCompletoEnfermera,
                i.getTemperatura(),
                i.getFrecuenciaCardiaca().getValorFormateado(),
                i.getFrecuenciaRespiratoria().getValorFormateado(),
                i.getTensionArterial().getValorFormateado(),
                posicion
        );
    }
}

